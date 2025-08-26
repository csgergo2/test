package com.example.jobprocessor.model;

public class CfiResponse {
    private int status;

    public CfiResponse() {
    }

    public CfiResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

