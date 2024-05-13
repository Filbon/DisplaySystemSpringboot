package com.example.displaysystemspringboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "schema_events")
public class SchemaEvent {
    @Id // Marks the field as the primary identifier
    private String id;

    @Field("courseId") // Specifies the name of the field in the MongoDB document
    private String courseCode;

    @Field("description") // name of the lecture
    private String description;

    @Field("owner") // name of the teacher
    private String owner;

    @Field("start_date") // Example of renaming the field in the MongoDB document
    private Date startDate;

    @Field("end_date")
    private Date endDate;

    @Field("location")
    private String location;

    @Field("uid")
    private String uid;

    public SchemaEvent(String courseCode, String description, String owner, Date startDate, Date endDate, String location, String uid) {
        this.courseCode = courseCode;
        this.description = description;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.uid = uid;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "SchemaEvent{" +
                "id='" + id + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
