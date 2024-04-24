package com.example.displaysystemspringboot.model;

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

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String nextLine = (i + 1 < lines.length) ? lines[i + 1] : null;

            if (line.startsWith("BEGIN:VEVENT")) {
                summary = null;
                uid = null;
                startDate = null;
                endDate = null;
                location = null;
                isParsingSummary = false;
                isParsingUid = false;
            } else if (line.startsWith("SUMMARY:") && summary == null) {
                summary = line.substring("SUMMARY:".length()).trim();
                isParsingSummary = true; // Start parsing the summary
            } if (line.startsWith("UID:") && uid == null) {
                uid = line.substring("UID:".length()).trim();
                isParsingUid = true;
            } else if (isParsingUid && line.startsWith(" ")) {
                // Append continuation lines to the UID
                uid += line.trim();
                isParsingUid = false;
            }  else if (line.startsWith("DTSTART;")) {
                startDate = parseDate(line.substring("DTSTART;".length()), nextLine);
                isParsingSummary = false; // Stop parsing the summary
                isParsingUid = false;
            } else if (line.startsWith("DTEND;")) {
                endDate = parseDate(line.substring("DTEND;".length()), nextLine);
                isParsingSummary = false; // Stop parsing the summary
                isParsingUid = false;
            } else if (line.startsWith("LOCATION:") && location == null) {
                // Handle special characters or escape sequences in the location field
                location = parseLocation(line.substring("LOCATION:".length()).trim());
                isParsingSummary = false; // Stop parsing the summary
                isParsingUid = false;
            } else if (isParsingSummary && line.startsWith(" ")) {
                // Append continuation lines to the summary
                summary += line.trim();
                isParsingSummary = false;
            } else if (!isParsingSummary && !isParsingUid && line.startsWith(" ") && location != null) {
                // Append continuation lines to the location
                location += line.trim();
            } else if (line.startsWith("END:VEVENT") && summary != null && startDate != null && endDate != null) {
                // Create a new calendar if it's the first event or the location changes
                if (calendar == null || !Objects.equals(calendar.getLocation(), location)) {
                    calendar = new Calendar(location);
                    calendars.add(calendar);
                }
                calendar.addEvent(new CalendarEvent(summary.trim(), startDate, endDate, location, uid));
            }
        }

        // For simplicity, return the first calendar found (assuming all events belong to the same location)
        return calendars.isEmpty() ? null : calendars.get(0);
    }

    private static String parseLocation(String location) {
        // Extract the portion before the first comma
        int commaIndex = location.indexOf(",");
        if (commaIndex != -1) {
            location = location.substring(0, commaIndex);
        }

        // Remove the last backslash if it exists
        if (location.endsWith("\\")) {
            location = location.substring(0, location.length() - 1);
        }

        // Remove leading and trailing whitespace
        location = location.trim();

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

