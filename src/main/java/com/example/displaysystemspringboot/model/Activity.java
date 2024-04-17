package com.example.displaysystemspringboot.model;

import java.time.LocalDateTime;

public class Activity {

    private String actName;
    private LocalDateTime startTime, endTime;
    private String user;

    public Activity(LocalDateTime startTime, LocalDateTime endTime, String user) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

    public Activity(String actName, LocalDateTime startTime, LocalDateTime endTime, String user) {
        this.actName = actName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "actName='" + actName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", user='" + user + '\'' +
                '}';
    }
}
