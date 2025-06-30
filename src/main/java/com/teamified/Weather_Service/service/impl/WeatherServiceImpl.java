package com.teamified.Weather_Service.service.impl;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.teamified.Weather_Service.client.OpenWeatherMapClient;
import com.teamified.Weather_Service.client.WeatherStackClient;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherStackClient weatherStackClient;
    private final OpenWeatherMapClient openWeatherMapClient;
    private final CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    public WeatherServiceImpl(
        WeatherStackClient weatherStackClient,
        OpenWeatherMapClient openWeatherMapClient,
        CacheManager cacheManager
    ) {
        this.weatherStackClient = weatherStackClient;
        this.openWeatherMapClient = openWeatherMapClient;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(value = "weather", key = "#city")
    public WeatherResponse getWeather(String city) {
        try {
            logger.info("Fetching weather data for {}", city);
            return weatherStackClient.getWeather(city);
        } catch (Exception e) {
            try {
                return openWeatherMapClient.getWeather(city);
            } catch (Exception ex) {
                return new WeatherResponse(0, 0);
            }
        }
    }
}