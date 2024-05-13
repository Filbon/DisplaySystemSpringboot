package com.example.displaysystemspringboot.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TEFetcher {
    public static void main(String[] args) {
        // Replace YOUR_SUBSCRIPTION_KEY with your actual subscription key
        String subscriptionKey = args[0];

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create a HttpRequest with the subscription key in the headers
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://integral-api.sys.kth.se/api/schema/v1/reservations/search?start=2024-01-11T00:00:00&end=2024-05-11T23:59:00"))
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .GET()
                .build();

        // Send the request and handle the response
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Print the response status code
            System.out.println("Response Status Code: " + response.statusCode());

            // Print the response body
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
