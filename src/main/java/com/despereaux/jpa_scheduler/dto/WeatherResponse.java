package com.despereaux.jpa_scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class WeatherResponse {
    @JsonProperty("today")
    private WeatherData today;

    // Getters and Setters
    public WeatherData getToday() {
        return today;
    }

    public void setToday(WeatherData today) {
        this.today = today;
    }

    public static class WeatherData {
        private String description;
        private double temperature;

        // Getters and Setters
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
    }

    @Service
    public static class WeatherService {

        private final RestTemplate restTemplate;

        @Autowired
        public WeatherService(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public WeatherResponse getWeather() {
            String url = "https://f-api.github.io/f-api/weather.json"; // API URL
            return restTemplate.getForObject(url, WeatherResponse.class);
        }
    }
}
