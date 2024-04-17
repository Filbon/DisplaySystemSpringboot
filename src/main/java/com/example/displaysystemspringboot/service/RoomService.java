package com.example.displaysystemspringboot.service;

import com.example.displaysystemspringboot.model.Activity;
import com.example.displaysystemspringboot.model.Room;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    public List<Room> getRoom() {
        Room room1 = new Room("T2");

        Activity act1 = new Activity("Scrum meeting", LocalDateTime.of(2024, Month.APRIL, 18, 10, 0),
                LocalDateTime.of(2024, Month.APRIL, 18, 12, 0),
                "Roman");

        Activity act2 = new Activity(LocalDateTime.of(2024, Month.APRIL, 18, 14, 0),
                LocalDateTime.of(2024, Month.APRIL, 18, 14, 30),
                "Adil");

        List<Activity> activities = new ArrayList<>();
        activities.add(act1);
        activities.add(act2);

        room1.setActivities(activities);
        return List.of(room1);
    }
}
