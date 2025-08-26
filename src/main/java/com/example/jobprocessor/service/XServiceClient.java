package com.example.jobprocessor.service;

import com.example.jobprocessor.model.Job;
import com.example.jobprocessor.model.XResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class XServiceClient {

    private final RestClient restClient;
    private final String url;

    public XServiceClient(RestClient.Builder builder,
                          @Value("${x.service.url:http://x.example.com}") String url) {
        this.restClient = builder.build();
        this.url = url;
    }

    public XResponse call(Job job) {
        String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body><job>" + job.getParam1() + "</job></soapenv:Body></soapenv:Envelope>";
        String response = restClient.post()
                .uri(url)
                .contentType(MediaType.TEXT_XML)
                .body(requestBody)
                .retrieve()
                .body(String.class);
        return new XResponse(response);
    }
}

