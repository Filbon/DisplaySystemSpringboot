package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarParser;
import com.example.displaysystemspringboot.repository.CalendarRepository;
import jakarta.annotation.PostConstruct;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EventFilteringService eventFilteringService;

    @Value("#{'${calendar.locations}'.split(',')}")
    private List<String> locations;

    public CompletableFuture<List<Calendar>> getCalendars() {
        CompletableFuture<List<Calendar>> future = new CompletableFuture<>();
        List<String> urls = generateUrls(locations);
        CompletableFuture<Void> fetchAll = fetchAndStoreCalendars(urls);
        fetchAll.thenCompose((Void) -> retrieveCalendarsFromDatabase())
                .thenAccept(future::complete)
                .exceptionally(ex -> {
                    // If fetch failed, retrieve from database
                    retrieveCalendarsFromDatabase()
                            .thenAccept(future::complete)
                            .exceptionally(innerEx -> {
                                future.completeExceptionally(new RuntimeException("Failed to fetch and retrieve calendars", ex));
                                return null;
                            });
                    return null;
                });

        return future;
    }
    private List<String> generateUrls(List<String> locations) {
        return locations.stream()
                .map(location -> "https://webmail.kth.se/owa/calendar/" + location + "@ug.kth.se/Calendar/calendar.ics")
                .collect(Collectors.toList());
    }

    private CompletableFuture<Void> fetchAndStoreCalendars(List<String> urls) {
        List<CompletableFuture<Void>> fetchFutures = urls.stream()
                .map(this::fetchAndStoreCalendar)
                .toList();
        return CompletableFuture.allOf(fetchFutures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<Void> fetchAndStoreCalendar(String url) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        fetchCalendar(url)
                .thenAccept(calendar -> {
                    if(!calendar.getEvents().isEmpty()){
                        calendarRepository.save(calendar);
                    }
                    future.complete(null);
                })
                .exceptionally(ex -> {
                    future.completeExceptionally(new RuntimeException("Failed to fetch and store calendar from URL: " + url, ex));
                    return null;
                });
        return future;
    }

    private CompletableFuture<Calendar> fetchCalendar(String url) {
        CompletableFuture<Calendar> future = new CompletableFuture<>();
        IcsFetcher.startFetching(url, content -> {
            try {
                Calendar calendar = CalendarParser.parseICalFile(content);
                assert calendar != null;
                future.complete(calendar);
            } catch (ParseException e) {
                future.completeExceptionally(new RuntimeException("Error parsing calendar content from URL: " + url, e));
            } catch (InvalidRecurrenceRuleException e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    private CompletableFuture<List<Calendar>> retrieveCalendarsFromDatabase() {
        CompletableFuture<List<Calendar>> future = new CompletableFuture<>();
        List<Calendar> calendars = calendarRepository.findAll();
        future.complete(calendars);
        return future;
    }
}
