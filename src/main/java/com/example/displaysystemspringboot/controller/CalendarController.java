package com.example.displaysystemspringboot.controller;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "api/v1/room")
public class CalendarController {

    @Autowired
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendars")
    public String showCalendars(@RequestParam(required = false) String location, Model model) {
        CompletableFuture<List<Calendar>> calendarsFuture = calendarService.getCalendars();
        List<Calendar> calendars = calendarsFuture.join(); // Wait for completion

        if (location != null && !location.isEmpty()) {
            // Filter calendars based on the provided location
            calendars = calendars.stream()
                    .filter(calendar -> location.equals(calendar.getLocation()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("calendars", calendars);
        return "firstVersion"; // Renders the HTML template
    }
}

@RestController
@RequestMapping(path = "api/v1/room")
class CalendarRestController {

    @Autowired
    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public CompletableFuture<List<Calendar>> getCal() {
        return calendarService.getCalendars(); // Returns the list of calendars as JSON
    }
}