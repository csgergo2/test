package com.example.jobprocessor.model;

public class WalkingTimeResponse {
    private long seconds;

    public WalkingTimeResponse() {
    }

    public WalkingTimeResponse(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }
}

