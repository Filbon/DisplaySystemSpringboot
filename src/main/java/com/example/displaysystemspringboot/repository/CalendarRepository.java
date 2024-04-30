package com.example.displaysystemspringboot.repository;

import com.example.displaysystemspringboot.model.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends MongoRepository<Calendar, String> {

}