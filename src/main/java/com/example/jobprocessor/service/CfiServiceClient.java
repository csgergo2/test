package com.example.jobprocessor.service;

import com.example.jobprocessor.model.CfiResponse;
import com.example.jobprocessor.model.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CfiServiceClient {

    private final RestClient restClient;

    public CfiServiceClient(RestClient.Builder builder,
                            @Value("${cfi.service.url:http://cfi.example.com}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public CfiResponse call(Job job, String token) {
        return restClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(CfiResponse.class);
    }
}

