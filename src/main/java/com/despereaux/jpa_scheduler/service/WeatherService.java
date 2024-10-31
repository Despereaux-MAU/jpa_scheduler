package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchWeatherDescription() {
        String url = "https://f-api.github.io/f-api/weather.json";
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

        if (weatherResponse == null || weatherResponse.getToday() == null) {
            return "날씨 정보를 가져올 수 없습니다";
        }

        return weatherResponse.getToday().getDescription() + " " + weatherResponse.getToday().getTemperature() + "°C";
    }
}
