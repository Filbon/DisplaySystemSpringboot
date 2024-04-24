package com.example.displaysystemspringboot.model;

import java.util.Date;

public class CalendarEvent {
    private String summary;
    private Date startDate;
    private Date endDate;
    private String location;
    private String uid;

    public CalendarEvent(String summary, Date startDate, Date endDate, String location, String uid) {
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.uid = uid;
    }
    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "summary='" + summary + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
