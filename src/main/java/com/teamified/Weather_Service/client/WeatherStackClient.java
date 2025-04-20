package com.teamified.Weather_Service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.model.external.WeatherStackResponse;

@Component
public class WeatherStackClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${weatherstack.api.url}")
    private String apiUrl;

    @Value("${weatherstack.api.key}")
    private String apiKey;

    public WeatherStackClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherResponse getWeather(String city) throws Exception {
        String url = String.format("%s?access_key=%s&query=%s", apiUrl, apiKey, city);
        String response = restTemplate.getForObject(url, String.class);
        WeatherStackResponse weatherStackResponse = objectMapper.readValue(response, WeatherStackResponse.class);
        return new WeatherResponse(weatherStackResponse.getCurrent().getWindSpeed(),
                                   weatherStackResponse.getCurrent().getTemperature());
    }
}