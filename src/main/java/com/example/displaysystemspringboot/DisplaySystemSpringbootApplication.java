package com.example.displaysystemspringboot;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.model.CalendarParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.ParseException;

@SpringBootApplication
public class DisplaySystemSpringbootApplication {

    public static void main(String[] args) {
        String filePath = "src/main/resources/calendar.ics";
        try {
            Calendar calendar = CalendarParser.parseICalFile(filePath);
            for (CalendarEvent event : calendar.getEvents()) {
                System.out.println("Summary: " + event.getSummary());
                System.out.println("Start Date: " + event.getStartDate());
                System.out.println("End Date: " + event.getEndDate());
                System.out.println("Location: " + event.getLocation());
                System.out.println("UID: " + event.getUid());
                System.out.println("----------------------");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        //SpringApplication.run(DisplaySystemSpringbootApplication.class, args);
    }

}
