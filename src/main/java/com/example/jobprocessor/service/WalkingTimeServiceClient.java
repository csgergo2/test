package com.example.jobprocessor.service;

import com.example.jobprocessor.model.Job;
import com.example.jobprocessor.model.WalkingTimeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WalkingTimeServiceClient {

    private final RestClient restClient;

    public WalkingTimeServiceClient(RestClient.Builder builder,
                                    @Value("${walking.service.url:http://walking.example.com}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public WalkingTimeResponse call(Job job, String token) {
        return restClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(WalkingTimeResponse.class);
    }
}

