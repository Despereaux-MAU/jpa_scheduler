package com.despereaux.jpa_scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {

    @JsonProperty("today")
    private WeatherData today;

    @Getter
    @Setter
    public static class WeatherData {
        private String description;
        private double temperature;
    }
}
