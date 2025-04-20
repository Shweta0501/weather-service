package com.teamified.Weather_Service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.model.external.OpenWeatherMapResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenWeatherMapClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public OpenWeatherMapClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherResponse getWeather(String city) throws Exception {
        String url = String.format("%s?q=%s,AU&appid=%s&units=metric", apiUrl, city, apiKey);
        String response = restTemplate.getForObject(url, String.class);
        OpenWeatherMapResponse weatherMapResponse = objectMapper.readValue(response, OpenWeatherMapResponse.class);
        return new WeatherResponse(weatherMapResponse.getWind().getSpeed(),
                                   weatherMapResponse.getMain().getTemp());
    }
}