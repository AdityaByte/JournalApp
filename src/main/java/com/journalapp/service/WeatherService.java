package com.journalapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.journalapp.api.WeatherResponse;
import com.journalapp.cache.AppCache;
import com.journalapp.constants.Placeholders;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;
    WeatherService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeather(String city){
        try{
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, API_KEY);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET , null , WeatherResponse.class);
            WeatherResponse body = response.getBody();
            return body;
        }
        catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

}
