package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam String city) {
        try {
            WeatherResponse response = weatherService.getWeather(city);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

