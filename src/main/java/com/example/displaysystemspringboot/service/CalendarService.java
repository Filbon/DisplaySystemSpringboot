package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.model.CalendarParser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CalendarService {

    @Autowired
    private CalendarParser calendarParser; // Autowiring CalendarParser

    @PostConstruct
    public CompletableFuture<List<String>> getCalendar() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        List<String> urls = Arrays.asList(
                "https://webmail.kth.se/owa/calendar/sth_plan7_7319@ug.kth.se/Calendar/calendar.ics",
                "https://webmail.kth.se/owa/calendar/sth_plan7_7005@ug.kth.se/Calendar/calendar.ics",
                "https://webmail.kth.se/owa/calendar/sth_plan7_7320@ug.kth.se/Calendar/calendar.ics"
        );

        List<String> allEvents = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(urls.size());

        for (String url : urls) {
            IcsFetcher.startFetching(url, content -> {
                try {
                    List<String> events = parseCalendarContent(content);
                    synchronized (allEvents) {
                        allEvents.addAll(events);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    if (count.decrementAndGet() == 0) {
                        future.complete(allEvents);
                    }
                }
            });
        }

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
