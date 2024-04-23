package com.example.displaysystemspringboot.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarParser {
    public static Calendar parseICalFile(String filePath) throws IOException, ParseException {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        Calendar calendar = new Calendar();

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
                location = line.substring("LOCATION:".length()).trim();
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
                calendar.addEvent(new CalendarEvent(summary.trim(), startDate, endDate, location, uid));
            }
        }

        return calendar;
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

