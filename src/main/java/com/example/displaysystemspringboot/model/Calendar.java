package com.example.displaysystemspringboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "calendars")
public class Calendar {
    @Id // Marks the field as the primary identifier
    private String id;

    @Field("location") // Specifies the name of the field in the MongoDB document
    private String location;

    @Field("events") // Example of renaming the field in the MongoDB document
    private List<CalendarEvent> events;

    public Calendar(String location) {
        this.events = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void addEvent(CalendarEvent event) {
        this.events.add(event);
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "events=" + events +
                '}';
    }

    public String getLocation() {
        return location;
    }
}
