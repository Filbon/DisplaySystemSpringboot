package com.example.displaysystemspringboot.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecurringEventGenerator {

    public static List<CalendarEvent> generateRecurringEvents(String summary, Date startDate, Date endDate, String location, String uid, String rrule) throws ParseException {
        List<CalendarEvent> recurringEvents = new ArrayList<>();
        String[] rruleParts = rrule.split(";");
        int interval = 1; // Default interval is 1
        Date untilDate = null;
        String freq = null;

        for (String part : rruleParts) {
            String[] keyValue = part.split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            switch (key) {
                case "INTERVAL" -> interval = Integer.parseInt(value);
                case "UNTIL" -> untilDate = parseDate(value);
                case "FREQ" -> freq = value;
                case "BYDAY" -> {
                    List<Date> occurrences = generateOccurrences(startDate, untilDate, value, interval, freq);
                    for (Date occurrence : occurrences) {
                        recurringEvents.add(new CalendarEvent(summary, occurrence, endDate, location, uid));
                    }
                }
                default -> {
                }

            }
        }
        return recurringEvents;
    }

    private static List<Date> generateOccurrences(Date startDate, Date untilDate, String byDay, int interval, String freq) {
        List<Date> occurrences = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        String[] days = byDay.split(",");
        int[] daysOfWeek = new int[days.length];
        for (int i = 0; i < days.length; i++) {
            daysOfWeek[i] = getDayOfWeekNumber(days[i]);
        }

        // Find the next occurrence based on the frequency
        switch (freq) {
            case "DAILY" -> {
                while (cal.getTime().before(untilDate) || cal.getTime().equals(untilDate)) {
                    occurrences.add(cal.getTime());
                    cal.add(Calendar.DATE, interval);
                }
            }
            case "WEEKLY" -> {
                for (int dayOfWeek : daysOfWeek) {
                    cal.setTime(startDate);
                    while (cal.getTime().before(untilDate) || cal.getTime().equals(untilDate)) {
                        if (cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                            occurrences.add(cal.getTime());
                        }
                        cal.add(Calendar.DATE, 7 * interval);
                    }
                }
            }
            case "MONTHLY" -> {
                while (cal.getTime().before(untilDate) || cal.getTime().equals(untilDate)) {
                    occurrences.add(cal.getTime());
                    cal.add(Calendar.MONTH, interval);
                }
            }
            case "YEARLY" -> {
                while (cal.getTime().before(untilDate) || cal.getTime().equals(untilDate)) {
                    occurrences.add(cal.getTime());
                    cal.add(Calendar.YEAR, interval);
                }
            }
            default -> {
            }
            // Handle other frequencies if needed
        }

        return occurrences;
    }



    private static int getDayOfWeekNumber(String day) {
        return switch (day.toUpperCase()) {
            case "SU" -> 1; // Sunday
            case "MO" -> 2; // Monday
            case "TU" -> 3; // Tuesday
            case "WE" -> 4; // Wednesday
            case "TH" -> 5; // Thursday
            case "FR" -> 6; // Friday
            case "SA" -> 7; // Saturday
            default -> -1;
        };
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(dateString);
    }
}

