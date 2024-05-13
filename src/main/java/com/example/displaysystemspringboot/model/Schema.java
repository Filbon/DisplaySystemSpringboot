package com.example.displaysystemspringboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "schema")
public class Schema {


    @Id // Specifies the name of the field in the MongoDB document
    private String location;

    @Field("events") // Example of renaming the field in the MongoDB document
    private List<SchemaEvent> events;

    public Schema(String location) {
        this.events = new ArrayList<>();
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void addEvent(SchemaEvent event) {
        this.events.add(event);
    }

    public List<SchemaEvent> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "events=" + events +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public boolean isEmpty() {
        return events.isEmpty() && Objects.equals(location, "");
    }

    public void setEvents(List<SchemaEvent> currentDayEvents) {
        this.events = currentDayEvents;
    }
    public void addEvents(List<SchemaEvent> events) {
        this.events.addAll(events);
    }
}
