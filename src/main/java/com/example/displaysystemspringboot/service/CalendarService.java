package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.model.CalendarParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class CalendarService {

    public List<CalendarEvent> getCalendarEvents() throws ParseException, IOException {
        String filePath = "src/main/resources/calendar.ics";
        Calendar calendar = CalendarParser.parseICalFile(filePath);
        return calendar.getEvents();
    }
}