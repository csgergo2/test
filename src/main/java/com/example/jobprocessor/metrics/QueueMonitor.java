package com.example.jobprocessor.metrics;

import com.example.jobprocessor.service.JobQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueueMonitor {

    private static final Logger log = LoggerFactory.getLogger(QueueMonitor.class);
    private final JobQueueService queueService;

    public QueueMonitor(JobQueueService queueService) {
        this.queueService = queueService;
    }

    @Scheduled(fixedRate = 60000)
    public void logQueueSize() {
        log.info("Current queue size: {}", queueService.queueSize());
    }
}

