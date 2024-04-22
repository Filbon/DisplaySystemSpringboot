package com.example.displaysystemspringboot.controller;

import com.example.displaysystemspringboot.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/room")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public List<String> getCal() {
        List<String> list = new ArrayList<>();
        calendarService.getCalendar(content -> {
            // Add fetched content to the list
            list.add(content);
        });
        return list;
    }
}
