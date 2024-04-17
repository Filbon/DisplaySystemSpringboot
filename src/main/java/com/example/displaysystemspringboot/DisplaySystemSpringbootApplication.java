package com.example.displaysystemspringboot;

import com.example.displaysystemspringboot.model.Calendar;
import com.example.displaysystemspringboot.model.CalendarEvent;
import com.example.displaysystemspringboot.model.CalendarParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@SpringBootApplication
@RestController
public class DisplaySystemSpringbootApplication {

    @GetMapping
    public String displayCalendar(Model model) {
        String filePath = "src/main/resources/calendar.ics";
        try {
            Calendar calendar = CalendarParser.parseICalFile(filePath);
            model.addAttribute("events", calendar.getEvents());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return "calender"; // Assuming you have a "calendar.html" template
    }

    public static void main(String[] args) {
        SpringApplication.run(DisplaySystemSpringbootApplication.class, args);
    }
}
