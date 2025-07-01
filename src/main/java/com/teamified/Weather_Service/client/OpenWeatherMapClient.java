package com.teamified.Weather_Service.client;

import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.mapper.OpenWeatherMapMapper;
import com.teamified.Weather_Service.model.WeatherResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenWeatherMapClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherMapClient.class);

    private final RestTemplate restTemplate;
    private final OpenWeatherMapMapper mapper;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public OpenWeatherMapClient(RestTemplate restTemplate, OpenWeatherMapMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public WeatherResponse getWeather(String city) {
        try {
            String url = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("q", city + ",AU")
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            logger.info("Calling OpenWeatherMap API: {}", url);

            String response = restTemplate.getForObject(url, String.class);

            logger.debug("Received response from OpenWeatherMap: {}", response);

            return mapper.map(response);

        } catch (Exception e) {
            logger.error("Error fetching weather data from OpenWeatherMap for city: {}", city, e);
            throw new WeatherServiceException("Failed to get weather from OpenWeatherMap", e);
        }
    }
}
