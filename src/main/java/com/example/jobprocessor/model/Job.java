package com.example.jobprocessor.model;

import java.util.Objects;

/**
 * Represents a unit of work identified by five parameters.
 */
public class Job {
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;

    public Job() {
    }

    public Job(String param1, String param2, String param3, String param4, String param5) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(param1, job.param1) &&
                Objects.equals(param2, job.param2) &&
                Objects.equals(param3, job.param3) &&
                Objects.equals(param4, job.param4) &&
                Objects.equals(param5, job.param5);
    }

    @Override
    public int hashCode() {
        return Objects.hash(param1, param2, param3, param4, param5);
    }

    @Override
    public String toString() {
        return "Job{" +
                "param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", param3='" + param3 + '\'' +
                ", param4='" + param4 + '\'' +
                ", param5='" + param5 + '\'' +
                '}';
    }
}

