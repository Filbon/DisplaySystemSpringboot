package com.example.displaysystemspringboot.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Calendar {
    private List<CalendarEvent> events;
    private String id;
    private String location;

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
