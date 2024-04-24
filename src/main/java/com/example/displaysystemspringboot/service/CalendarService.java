package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.model.CalendarParser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CalendarService {

    @Autowired
    private CalendarParser calendarParser; // Autowiring CalendarParser

    @PostConstruct
    public CompletableFuture<List<String>> getCalendar() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        IcsFetcher.startFetching("https://webmail.kth.se/owa/calendar/sth_plan7_7319@ug.kth.se/Calendar/calendar.ics", content -> {
            // Transform the fetched content if needed
            List<String> events = parseCalendarContent(content);
            future.complete(events);
        });
        return future;
    }

    private List<String> parseCalendarContent(String content) throws ParseException {
        // Use the autowired CalendarParser instance to parse the content
        Calendar calendar = calendarParser.parseICalFile(content);

        // Print out details of each parsed event
        System.out.println("Parsed Events:");
        for (CalendarEvent event : calendar.getEvents()) {
            System.out.println("Summary: " + event.getSummary());
            System.out.println("Start Date: " + event.getStartDate());
            System.out.println("End Date: " + event.getEndDate());
            System.out.println("Location: " + event.getLocation());
            System.out.println("UID: " + event.getUid());
            System.out.println();
        }

        // Construct a list of event summaries to return
        List<String> eventSummaries = new ArrayList<>();
        for (CalendarEvent event : calendar.getEvents()) {
            eventSummaries.add(event.getSummary());
        }

        return eventSummaries;
    }

}
