package com.teamified.Weather_Service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.mapper.WeatherStackMapper;
import com.teamified.Weather_Service.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherStackClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherStackClient.class);

    private final RestTemplate restTemplate;
    private final WeatherStackMapper weatherStackMapper;
    private final ObjectMapper objectMapper;

    @Value("${weatherstack.api.url}")
    private String apiUrl;

    @Value("${weatherstack.api.key}")
    private String apiKey;

    public WeatherStackClient(RestTemplate restTemplate, WeatherStackMapper weatherStackMapper, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.weatherStackMapper = weatherStackMapper;
        this.objectMapper = objectMapper;
    }

    public WeatherResponse getWeather(String city) {
        try {
            String url = String.format("%s?access_key=%s&query=%s", apiUrl, apiKey, city);
            logger.info("Calling WeatherStack API for city: {}", city);

            String json = restTemplate.getForObject(url, String.class);

            if (json.contains("\"success\":false")) {
                logger.warn("WeatherStack API returned error for city {}: {}", city, json);
                throw new WeatherServiceException("WeatherStack API error");
            }

            WeatherResponse response = weatherStackMapper.map(json);
            logger.info("Successfully fetched data from WeatherStack for city: {}", city);
            return response;

        } catch (WeatherServiceException ex) {
            throw ex; // Already logged and meaningful
        } catch (Exception ex) {
            logger.warn("WeatherStack client error for city {}: {}", city, ex.getMessage());
            throw new WeatherServiceException("WeatherStack client failure", ex);
        }
    }
}
