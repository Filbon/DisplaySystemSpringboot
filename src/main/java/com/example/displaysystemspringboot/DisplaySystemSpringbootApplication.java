package com.example.displaysystemspringboot;

import com.example.displaysystemspringboot.model.Activity;
import com.example.displaysystemspringboot.model.Room;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class DisplaySystemSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisplaySystemSpringbootApplication.class, args);
    }

}
