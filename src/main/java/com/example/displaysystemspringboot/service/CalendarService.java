package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.model.CalendarParser;
import com.example.displaysystemspringboot.repository.CalendarEventRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    private CalendarParser calendarParser; // Autowiring CalendarParser

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @PostConstruct
    public CompletableFuture<List<Calendar>> getCalendars() {
        CompletableFuture<List<Calendar>> future = new CompletableFuture<>();

        List<String> urls = Arrays.asList(
                "https://webmail.kth.se/owa/calendar/sth_plan7_7319@ug.kth.se/Calendar/calendar.ics",
                "https://webmail.kth.se/owa/calendar/sth_plan7_7320@ug.kth.se/Calendar/calendar.ics",
                "https://webmail.kth.se/owa/calendar/sth_plan7_7327@ug.kth.se/Calendar/calendar.ics",
                "https://webmail.kth.se/owa/calendar/sth_plan9_9504@ug.kth.se/Calendar/calendar.ics"
        );

        List<CompletableFuture<Calendar>> calendarFutures = new ArrayList<>();

        for (String url : urls) {
            CompletableFuture<Calendar> calendarFuture = fetchAndParseCalendar(url);
            calendarFutures.add(calendarFuture);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                calendarFutures.toArray(new CompletableFuture[0])
        );

        allOf.thenApply((Void) -> {
            List<Calendar> calendars = calendarFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            future.complete(calendars);
            return null;
        });

        return future;
    }

    private CompletableFuture<Calendar> fetchAndParseCalendar(String url) {
        CompletableFuture<Calendar> future = new CompletableFuture<>();
        IcsFetcher.startFetching(url, content -> {
            try {
                Calendar calendar = calendarParser.parseICalFile(content);
                // Save events to the MongoDB database
                for (CalendarEvent event : calendar.getEvents()) {
                    calendarEventRepository.save(event);
                }
                future.complete(calendar);
            } catch (ParseException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}

