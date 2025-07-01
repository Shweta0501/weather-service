package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    private WeatherService weatherService;
    private WeatherController weatherController;
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        cacheManager = mock(CacheManager.class);
        weatherController = new WeatherController(weatherService, cacheManager);
    }

    @Test
    void testGetWeatherSuccess() {
        WeatherResponse mockResponse = new WeatherResponse(20, 29);
        when(weatherService.getWeather("melbourne")).thenReturn(mockResponse);

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeather("melbourne");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(20, responseEntity.getBody().getWindSpeed());
        assertEquals(29, responseEntity.getBody().getTemperatureDegrees());
        verify(weatherService, times(1)).getWeather("melbourne");
    }

    @Test
    void testGetWeatherServiceFailure() {
        when(weatherService.getWeather("melbourne")).thenThrow(new RuntimeException("Service failure"));

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeather("melbourne");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(weatherService, times(1)).getWeather("melbourne");
    }
}
