package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventFilteringService {

    public List<Calendar> filterEventsForCurrentDay(List<Calendar> calendars) {
        Date currentDate = new Date();
        calendars.forEach(calendar -> {
            List<CalendarEvent> currentDayEvents = calendar.getEvents().stream()
                    .filter(event -> isEventOnCurrentDay(event, currentDate))
                    .collect(Collectors.toList());
            calendar.setEvents(currentDayEvents);
        });
        return calendars;
    }

    private boolean isEventOnCurrentDay(CalendarEvent event, Date currentDate) {
        LocalDate eventDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDateWithoutTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return eventDate.equals(currentDateWithoutTime);
    }
}