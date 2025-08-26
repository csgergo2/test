package com.example.jobprocessor.controller;

import com.example.jobprocessor.model.Job;
import com.example.jobprocessor.service.JobQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobQueueService queueService;

    public JobController(JobQueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/submit")
    public ResponseEntity<String> submitJob(@RequestParam String param1,
                                            @RequestParam String param2,
                                            @RequestParam String param3,
                                            @RequestParam String param4,
                                            @RequestParam String param5) {
        Job job = new Job(param1, param2, param3, param4, param5);
        boolean accepted = queueService.submit(job);
        if (accepted) {
            log.info("Job accepted {}", job);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Job queued");
        } else {
            log.info("Duplicate job rejected {}", job);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate job");
        }
    }
}

