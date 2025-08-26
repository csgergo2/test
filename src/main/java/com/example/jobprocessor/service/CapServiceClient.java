package com.example.jobprocessor.service;

import com.example.jobprocessor.model.CapResponse;
import com.example.jobprocessor.model.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CapServiceClient {

    private final RestClient restClient;

    public CapServiceClient(RestClient.Builder builder,
                            @Value("${cap.service.url:http://cap.example.com}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public CapResponse call(Job job, String token) {
        return restClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(CapResponse.class);
    }
}

