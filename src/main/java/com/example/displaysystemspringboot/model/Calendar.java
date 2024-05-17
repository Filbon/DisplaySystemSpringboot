package com.example.displaysystemspringboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "calendars")
public class Calendar {


    @Id // Specifies the name of the field in the MongoDB document
    private String location;

    private String roomNum;

    @Field("events") // Example of renaming the field in the MongoDB document
    private List<CalendarEvent> events;

    public Calendar(String location) {
        this.events = new ArrayList<>();
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean isEmpty() {
        return events.isEmpty() && Objects.equals(location, "");
    }

    public void setEvents(List<CalendarEvent> currentDayEvents) {
        this.events = currentDayEvents;
    }
    public void addEvents(List<CalendarEvent> events) {
        this.events.addAll(events);
    }

    public void setRoomNum(String num) {
        roomNum = num;
    }

    public String getRoomNum() {
        return roomNum;
    }
}
