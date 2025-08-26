package com.example.jobprocessor.service;

import com.example.jobprocessor.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Service
public class WorkerService implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);

    private final int workerCount;
    private final JobQueueService queueService;
    private final CapServiceClient capServiceClient;
    private final CfiServiceClient cfiServiceClient;
    private final WalkingTimeServiceClient walkingServiceClient;
    private final XServiceClient xServiceClient;
    private final AuthServiceClient authServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private ExecutorService executorService;

    public WorkerService(@Value("${worker.count:2}") int workerCount,
                         JobQueueService queueService,
                         CapServiceClient capServiceClient,
                         CfiServiceClient cfiServiceClient,
                         WalkingTimeServiceClient walkingServiceClient,
                         XServiceClient xServiceClient,
                         AuthServiceClient authServiceClient,
                         RedisTemplate<String, Object> redisTemplate) {
        this.workerCount = workerCount;
        this.queueService = queueService;
        this.capServiceClient = capServiceClient;
        this.cfiServiceClient = cfiServiceClient;
        this.walkingServiceClient = walkingServiceClient;
        this.xServiceClient = xServiceClient;
        this.authServiceClient = authServiceClient;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void start() {
        executorService = Executors.newFixedThreadPool(workerCount);
        for (int i = 0; i < workerCount; i++) {
            executorService.submit(this::workerLoop);
        }
        log.info("Started {} worker threads", workerCount);
    }

    private void workerLoop() {
        while (true) {
            try {
                Job job = queueService.take();
                log.info("Processing job {}", job);
                try {
                    if ("SOAP".equalsIgnoreCase(job.getParam2())) {
                        XResponse xResp = retry(() -> xServiceClient.call(job));
                        redisTemplate.opsForValue().set(job.toString(), xResp);
                    } else {
                        String token = authServiceClient.getToken();
                        CapResponse cap = retry(() -> capServiceClient.call(job, token));
                        CfiResponse cfi = retry(() -> cfiServiceClient.call(job, token));
                        WalkingTimeResponse walk = retry(() -> walkingServiceClient.call(job, token));
                        CombinedResponse combined = new CombinedResponse(cap, cfi, walk);
                        redisTemplate.opsForValue().set(job.toString(), combined);
                    }
                    log.info("Job completed {}", job);
                } catch (Exception ex) {
                    log.error("Job failed {}", job, ex);
                } finally {
                    queueService.complete(job);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private <T> T retry(Supplier<T> supplier) throws Exception {
        int attempts = 0;
        while (true) {
            try {
                return supplier.get();
            } catch (Exception e) {
                attempts++;
                if (attempts >= 3) {
                    throw e;
                }
                log.warn("Retrying after failure", e);
                Thread.sleep(1000L * attempts);
            }
        }
    }

    @Override
    public void destroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}

