package com.example.displaysystemspringboot.controller;

import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/")
    public String displayCalendar(Model model) {
        try {
            List<CalendarEvent> events = calendarService.getCalendarEvents();
            model.addAttribute("events", events);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            // Handle exception
        }
        return "calendar"; // Assuming you have a "calendar.html" template
    }
}
