package com.example.displaysystemspringboot.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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
            try {
                contentHandler.handleContent(fetchedContent);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private static String fetch(String icsURL) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            URL url = new URL(icsURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            logger.log(Level.INFO, "Response Code: {0}", responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream in = conn.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                logger.log(Level.WARNING, "Failed to fetch ICS file. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error fetching ICS file", e);
        }

        // Convert the byte array to a string using UTF-8 encoding
        return outputStream.toString(StandardCharsets.UTF_8);
    }


    public interface ContentHandler {
        void handleContent(String content) throws ParseException;
    }


}