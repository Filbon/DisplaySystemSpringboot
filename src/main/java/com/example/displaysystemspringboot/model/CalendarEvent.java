package com.example.displaysystemspringboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "calendar_events")
public class CalendarEvent {
    @Id // Marks the field as the primary identifier
    private String id;

    @Field("summary") // Specifies the name of the field in the MongoDB document
    private String summary;

    @Field("start_date") // Example of renaming the field in the MongoDB document
    private Date startDate;

    @Field("end_date")
    private Date endDate;

    @Field("location")
    private String location;

    @Field("uid")
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
