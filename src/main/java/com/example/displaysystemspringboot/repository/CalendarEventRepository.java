package com.example.displaysystemspringboot.repository;

import com.example.displaysystemspringboot.model.CalendarEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventRepository extends MongoRepository<CalendarEvent, String> {

}