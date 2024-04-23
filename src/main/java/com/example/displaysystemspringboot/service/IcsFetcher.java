package com.example.displaysystemspringboot.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component // Ensure that the class is recognized as a Spring bean
public class IcsFetcher {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Logger logger = Logger.getLogger(IcsFetcher.class.getName());

    public static void startFetching(String icsURL, ContentHandler contentHandler) {
        scheduler.scheduleAtFixedRate(() -> {
            String fetchedContent = fetch(icsURL);
            contentHandler.handleContent(fetchedContent);
        }, 0, 10, TimeUnit.SECONDS);
    }

    private static String fetch(String icsURL) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(icsURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            logger.log(Level.INFO, "Response Code: {0}", responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            } else {
                logger.log(Level.WARNING, "Failed to fetch ICS file. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error fetching ICS file", e);
        }
        //logger.log(Level.INFO, "Fetched content: {0}", response.toString());
        return response.toString();
    }

    public interface ContentHandler {
        void handleContent(String content);
    }

    @PostConstruct
    public void init() {
        // Start fetching when the bean is constructed
        logger.info("Initializing IcsFetcher...");
        startFetching("https://webmail.kth.se/owa/calendar/sth_plan7_7319@ug.kth.se/Calendar/calendar.ics", content -> {
            // Handle the fetched content asynchronously
            logger.info(content);
        });
    }
}