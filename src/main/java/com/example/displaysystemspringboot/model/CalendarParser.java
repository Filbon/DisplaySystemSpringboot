package com.example.displaysystemspringboot.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CalendarParser {
    public static Calendar parseICalFile(String filePath) throws IOException, ParseException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        Calendar calendar = new Calendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String[] lines = content.split("\\r?\\n");

        String summary = null;
        Date startDate = null;
        Date endDate = null;
        String location = null;

        boolean isParsingSummary = false;

        for (String line : lines) {
            if (line.startsWith("BEGIN:VEVENT")) {
                summary = null;
                startDate = null;
                endDate = null;
                location = null;
                isParsingSummary = true;
            } else if (line.startsWith("SUMMARY:") && summary == null) {
                summary = line.substring("SUMMARY:".length());
            } else if (line.startsWith(" ") && isParsingSummary && summary != null) {
                summary += line.trim();
            } else if (line.startsWith("DTSTART;")) {
                startDate = parseDate(line.substring("DTSTART;".length()));
            } else if (line.startsWith("DTEND;")) {
                endDate = parseDate(line.substring("DTEND;".length()));
            } else if (line.startsWith("LOCATION:") && location == null) {
                isParsingSummary = false;
                location = line.substring("LOCATION:".length());
            } else if (line.startsWith(" ") && !isParsingSummary && location != null) {
                location += line.trim();
            } else if (line.startsWith("END:VEVENT") && summary != null && startDate != null && endDate != null) {
                calendar.addEvent(new CalendarEvent(summary.trim(), startDate, endDate, location));
            }
        }

        return calendar;
    }

    private static Date parseDate(String dateString) throws ParseException {

        String[] parts = dateString.split(":");
        String dateStr = parts[1].split(";")[0];

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.parse(dateStr);
    }
}
