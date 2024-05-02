package com.example.displaysystemspringboot.model;

import com.example.displaysystemspringboot.repository.CalendarRepository;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CalendarParser {
    public static Calendar parseICalFile(String content) throws ParseException {
        Calendar calendar = null;
        List<Calendar> calendars = new ArrayList<>();
        String[] lines = content.split("\\r?\\n");
        String summary = null;
        String uid = null;
        Date startDate = null;
        Date endDate = null;
        String location = null;
        boolean isParsingSummary = false;
        boolean isParsingUid = false;
        boolean isParsingCalendar = false; // Flag to indicate if currently parsing a calendar

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String nextLine = (i + 1 < lines.length) ? lines[i + 1] : null;

            if (line.startsWith("BEGIN:VCALENDAR")) { // Start of a new calendar
                calendar = new Calendar(null); // Creating a new calendar
                isParsingCalendar = true; // Now parsing a calendar
                location = null; // Resetting location
            } else if (line.startsWith("END:VCALENDAR")) { // End of a calendar
                if (calendar != null) { // Add calendar only if it's not null
                    calendars.add(calendar);
                    isParsingCalendar = false; // Resetting parsing calendar flag
                }
            } else if (line.startsWith("BEGIN:VEVENT")) {
                summary = null;
                uid = null;
                startDate = null;
                endDate = null;
                location = null;
                isParsingSummary = false;
                isParsingUid = false;
            } else if (line.startsWith("SUMMARY:") && summary == null) {
                summary = line.substring("SUMMARY:".length()).trim();
                isParsingSummary = true;
            } if (line.startsWith("UID:") && uid == null) {
                uid = line.substring("UID:".length()).trim();
                isParsingUid = true;
            } else if (isParsingUid && line.startsWith(" ")) {
                uid += line.trim();
                isParsingUid = false;
            }  else if (line.startsWith("DTSTART;")) {
                startDate = parseDate(line.substring("DTSTART;".length()), nextLine);
                isParsingSummary = false;
                isParsingUid = false;
            } else if (line.startsWith("DTEND;")) {
                endDate = parseDate(line.substring("DTEND;".length()), nextLine);
                isParsingSummary = false;
                isParsingUid = false;
            } else if (line.startsWith("LOCATION:") && location == null) {
                location = parseLocation(line.substring("LOCATION:".length()).trim());
                isParsingSummary = false;
                isParsingUid = false;
            } else if (isParsingSummary && line.startsWith(" ")) {
                summary += line.trim();
                isParsingSummary = false;
            } else if (!isParsingSummary && !isParsingUid && line.startsWith(" ") && location != null) {
                location += line.trim();
            } else if (line.startsWith("END:VEVENT") && summary != null && startDate != null && endDate != null && isParsingCalendar) {
                if (calendar != null) {
                    summary = summary.replaceAll("\\\\", "");
                    calendar.addEvent(new CalendarEvent(summary.trim(), startDate, endDate, location, uid));
                    calendar.setLocation(location);
                }
            }
        }
        System.out.println(calendar);
        // Return all calendars found
        return calendars.isEmpty() ? null : calendars.get(0); // Return the first calendar found (assuming all events belong to the same location)
    }

    private static String parseLocation(String location) {
        int whitespaceIndex = location.indexOf(" ");
        if (whitespaceIndex != -1) {
            location = location.substring(0, whitespaceIndex);
        }
        if (location.endsWith(",")) {
            location = location.substring(0, location.length() - 1);
        }
        if (location.endsWith("\\")) {
            location = location.substring(0, location.length() - 1);
        }
        return location;
    }

    private static Date parseDate(String dateString, String nextLine) throws ParseException {
        Pattern pattern = Pattern.compile(":([^:]+)$");
        Matcher matcher = pattern.matcher(dateString);
        if (matcher.find()) {
            String dateStr = matcher.group(1);
            if (nextLine != null && nextLine.startsWith(" ")) {
                dateStr += nextLine.trim();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format.parse(dateStr);
        } else {
            throw new ParseException("Unable to parse date string: " + dateString, 0);
        }
    }
}

