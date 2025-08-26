package com.example.jobprocessor.model;

/**
 * Simple wrapper around a SOAP response string.
 */
public class XResponse {
    private String payload;

    public XResponse() {
    }

    public XResponse(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}

