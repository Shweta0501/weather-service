package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;
    private final CacheManager cacheManager;

    public WeatherController(WeatherService weatherService, CacheManager cacheManager) {
        this.weatherService = weatherService;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam String city) {
        Cache cache = cacheManager.getCache("weather");
        WeatherResponse cached = cache != null ? cache.get(city, WeatherResponse.class) : null;

        if (cached != null) {
            logger.info("CACHE HIT — serving cached weather data for city: {}", city);
            return ResponseEntity.ok(cached);
        }

        logger.info("CACHE MISS — no cached data found for city: {} — invoking service", city);

        try {
            WeatherResponse response = weatherService.getWeather(city);
            return ResponseEntity.ok(response);
        } catch (WeatherServiceException ex) {
            logger.error("WeatherServiceException occurred for city {}: {}", city, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } catch (Exception ex) {
            logger.error("Unexpected error occurred for city {}", city, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
