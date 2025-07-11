package com.teamified.Weather_Service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.model.external.OpenWeatherMapResponse;
import com.teamified.Weather_Service.service.impl.WeatherServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class OpenWeatherMapClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;

    // Melbourne coordinates
    private static final String MELBOURNE_LAT = "-37.8136";
    private static final String MELBOURNE_LON = "144.9631";

    public OpenWeatherMapClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate.setErrorHandler(new NoOpResponseErrorHandler());
    }

    public WeatherResponse getWeather(String city) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                MELBOURNE_LAT, MELBOURNE_LON, apiKey
        );

        try {
            String response = restTemplate.getForObject(url, String.class);
            OpenWeatherMapResponse weatherMapResponse = objectMapper.readValue(response, OpenWeatherMapResponse.class);

            if (!"200".equals(weatherMapResponse.getCode())) {
                throw new WeatherServiceException("OpenWeatherMap API returned error: " + weatherMapResponse.getMessage());
            }

            return new WeatherResponse(
                    weatherMapResponse.getWind().getSpeed(),
                    weatherMapResponse.getMain().getTemp()
            );

        } catch (Exception e) {
            logger.error("Error fetching weather data from OpenWeatherMap for city: {}. Reason: {}", city, e.getMessage());
            throw new WeatherServiceException("OpenWeatherMap error: " + e.getMessage(), e);
        }
    }

    private static class NoOpResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // No-op
        }
    }
}
