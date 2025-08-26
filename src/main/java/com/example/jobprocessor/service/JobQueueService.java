package com.example.jobprocessor.service;

import com.example.jobprocessor.model.Job;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class JobQueueService {

    private static final Logger log = LoggerFactory.getLogger(JobQueueService.class);

    private final BlockingQueue<Job> queue = new LinkedBlockingQueue<>();
    private final Set<Job> queuedJobs = ConcurrentHashMap.newKeySet();
    private final Set<Job> inProgressJobs = ConcurrentHashMap.newKeySet();

    public JobQueueService(MeterRegistry meterRegistry) {
        meterRegistry.gauge("job_queue_size", queue, BlockingQueue::size);
        meterRegistry.gauge("job_in_progress_size", inProgressJobs, Set::size);
    }

    public synchronized boolean submit(Job job) {
        if (queuedJobs.contains(job) || inProgressJobs.contains(job)) {
            return false;
        }
        boolean added = queue.offer(job);
        if (added) {
            queuedJobs.add(job);
            log.info("Job queued: {}", job);
        }
        return added;
    }

    public Job take() throws InterruptedException {
        Job job = queue.take();
        queuedJobs.remove(job);
        inProgressJobs.add(job);
        return job;
    }

    public void complete(Job job) {
        inProgressJobs.remove(job);
    }

    public int queueSize() {
        return queue.size();
    }
}

