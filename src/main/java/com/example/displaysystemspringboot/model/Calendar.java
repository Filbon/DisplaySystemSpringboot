package com.example.displaysystemspringboot.model;

import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private List<CalendarEvent> events;

    public Calendar() {
        this.events = new ArrayList<>();
    }

    public void addEvent(CalendarEvent event) {
        this.events.add(event);
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }
}
