package com.example.displaysystemspringboot.model;


import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecurringEventGenerator {

    public static List<CalendarEvent> generateRecurringEvents(String summary, Date startDate, Date endDate, String location, String uid, String rrule) throws ParseException, InvalidRecurrenceRuleException {


        RecurrenceRule rule = new RecurrenceRule(rrule);

        List<CalendarEvent> recurringEvents = new ArrayList<>();


        DateTime startDateTime = new DateTime(startDate.getTime());

        RecurrenceRuleIterator it = rule.iterator(startDateTime);



            while (it.hasNext())
            {
                DateTime nextInstance = it.nextDateTime();

                Date date = new Date(nextInstance.getTimestamp());

                Date hourlyEndDate = new Date(nextInstance.getTimestamp());
                hourlyEndDate.setTime(endDate.getTime());

                recurringEvents.add(new CalendarEvent(summary, date, hourlyEndDate, location, uid));

            }

        return recurringEvents;
    }


}
