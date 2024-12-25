package com.journalapp.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {

    private Main main;

    @Getter
    @Setter
    public static class Main {
        public double temp;
        @JsonProperty("feels_like")
        public double feelsLike;
        @JsonProperty("temp_min")
        public double tempMin;
        @JsonProperty("temp_max")
        public double tempMax;
        public int pressure;
        public int humidity;
    }
}




