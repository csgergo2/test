package com.example.jobprocessor.model;

public class CombinedResponse {
    private CapResponse capResponse;
    private CfiResponse cfiResponse;
    private WalkingTimeResponse walkingTimeResponse;

    public CombinedResponse() {
    }

    public CombinedResponse(CapResponse capResponse, CfiResponse cfiResponse, WalkingTimeResponse walkingTimeResponse) {
        this.capResponse = capResponse;
        this.cfiResponse = cfiResponse;
        this.walkingTimeResponse = walkingTimeResponse;
    }

    public CapResponse getCapResponse() {
        return capResponse;
    }

    public void setCapResponse(CapResponse capResponse) {
        this.capResponse = capResponse;
    }

    public CfiResponse getCfiResponse() {
        return cfiResponse;
    }

    public void setCfiResponse(CfiResponse cfiResponse) {
        this.cfiResponse = cfiResponse;
    }

    public WalkingTimeResponse getWalkingTimeResponse() {
        return walkingTimeResponse;
    }

    public void setWalkingTimeResponse(WalkingTimeResponse walkingTimeResponse) {
        this.walkingTimeResponse = walkingTimeResponse;
    }
}

