package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam String city) {
        logger.info("Received weather request for city: {}", city);
        try {
            WeatherResponse response = weatherService.getWeather(city);
            logger.info("Successfully retrieved weather data for city: {}", city);
            return ResponseEntity.ok(response);
        } catch (WeatherServiceException ex) {
            logger.error("Weather service error for city: {} - {}", city, ex.getMessage());
            return ResponseEntity.status(502).body(null);  // Bad Gateway - upstream provider issue
        } catch (Exception ex) {
            logger.error("Unexpected error retrieving weather data for city: {}", city, ex);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
