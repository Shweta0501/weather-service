package com.teamified.Weather_Service.service.impl;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.teamified.Weather_Service.client.OpenWeatherMapClient;
import com.teamified.Weather_Service.client.WeatherStackClient;
import com.teamified.Weather_Service.exception.WeatherServiceException;
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
        logger.info("Cache MISS or expired — fetching fresh data for city: {}", city);

        StringBuilder failureReasons = new StringBuilder();

        try {
            logger.info("Attempting to fetch data from WeatherStack for city: {}", city);
            WeatherResponse response = weatherStackClient.getWeather(city);
            logger.info("Successfully fetched data from WeatherStack for city: {}", city);
            return response;
        } catch (WeatherServiceException ex) {
            logger.warn("WeatherStack failed for city: {}. Reason: {}", city, ex.getMessage());
            failureReasons.append("WeatherStack failed: ").append(ex.getMessage()).append("; ");
        } catch (Exception ex) {
            logger.warn("Unexpected WeatherStack error for city {}: {}", city, ex.getMessage());
            failureReasons.append("WeatherStack unexpected error: ").append(ex.getMessage()).append("; ");
        }

        try {
            logger.info("Falling back to OpenWeatherMap for city: {}", city);
            WeatherResponse response = openWeatherMapClient.getWeather(city);
            logger.info("Successfully fetched data from OpenWeatherMap for city: {}", city);
            return response;
        } catch (WeatherServiceException ex) {
            logger.warn("OpenWeatherMap failed for city: {}. Reason: {}", city, ex.getMessage());
            failureReasons.append("OpenWeatherMap failed: ").append(ex.getMessage()).append("; ");
        } catch (Exception ex) {
            logger.error("Unexpected OpenWeatherMap error for city {}: {}", city, ex.getMessage());
            failureReasons.append("OpenWeatherMap unexpected error: ").append(ex.getMessage()).append("; ");
        }

        logger.error("Both WeatherStack and OpenWeatherMap failed for city: {}. Reasons: {}", city, failureReasons.toString());
        throw new WeatherServiceException("Both weather providers failed. " + failureReasons.toString());
    }

}