package com.example.displaysystemspringboot.service;

import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    public void getCalendar(ContentHandler contentHandler) {
        IcsFetcher.startFetching("https://webmail.kth.se/owa/calendar/sth_plan7_7319@ug.kth.se/Calendar/calendar.ics", content -> {
            // Handle the fetched content asynchronously
            contentHandler.handleContent(content);
        });
    }

    public interface ContentHandler {
        void handleContent(String content);
    }
}
