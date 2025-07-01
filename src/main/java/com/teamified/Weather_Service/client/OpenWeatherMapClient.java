package com.teamified.Weather_Service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.model.external.OpenWeatherMapResponse;
import com.teamified.Weather_Service.service.impl.WeatherServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Component
public class OpenWeatherMapClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public OpenWeatherMapClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

        // Disable automatic error throwing
        this.restTemplate.setErrorHandler(new NoOpResponseErrorHandler());
    }

    public WeatherResponse getWeather(String city) {
        String url = String.format("%s?q=%s,AU&appid=%s&units=metric", apiUrl, city, apiKey);
        try {
            String response = restTemplate.getForObject(url, String.class);
            OpenWeatherMapResponse weatherMapResponse = objectMapper.readValue(response, OpenWeatherMapResponse.class);

            if (weatherMapResponse.getCode() != "200") {
                throw new WeatherServiceException("OpenWeatherMap API returned error: " + weatherMapResponse.getMessage());
            }

            return new WeatherResponse(
                weatherMapResponse.getWind().getSpeed(),
                weatherMapResponse.getMain().getTemp()
            );

        } catch (Exception e) {
            // You control logging — no stacktrace unless you want it
            logger.error("Error fetching weather data from OpenWeatherMap for city: {}. Reason: {}", city, e.getMessage());
            throw new WeatherServiceException("OpenWeatherMap error: " + e.getMessage(), e);
        }
    }

    private static class NoOpResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false; // Treat all responses as non-error
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // Do nothing
        }
    }
}