package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WeatherApiControllerCacheTest {

    private WeatherService weatherService;
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        weatherController = new WeatherController(weatherService);
    }

    @Test
    void testCachingBehavior() throws Exception {
        // Mocking the weather response
        WeatherResponse weatherResponse = new WeatherResponse(29, 20);

        // Mock the service to return the same response for the first call
        when(weatherService.getWeather("melbourne")).thenReturn(weatherResponse);

        // First request - should call the service and return the response
        ResponseEntity<WeatherResponse> firstResponse = weatherController.getWeather("melbourne");

        // Assert that the response is as expected
        assertEquals(200, firstResponse.getStatusCodeValue());
        assertEquals(29, firstResponse.getBody().getTemperatureDegrees());
        assertEquals(20, firstResponse.getBody().getWindSpeed());

        // Verify that the service was called once
        verify(weatherService, times(1)).getWeather("melbourne");

        // Simulate a second request within 3 seconds (should use cached result)
        ResponseEntity<WeatherResponse> secondResponse = weatherController.getWeather("melbourne");

        // Assert that the second response is the same as the first response
        assertEquals(200, secondResponse.getStatusCodeValue());
        assertEquals(29, secondResponse.getBody().getTemperatureDegrees());
        assertEquals(20, secondResponse.getBody().getWindSpeed());

        // Verify that the service was NOT called again (because it's cached)
        verify(weatherService, times(1)).getWeather("melbourne");
    }

    @Test
    void testCachingExpiresAfterThreeSeconds() throws Exception {
        // Mocking the weather response
        WeatherResponse weatherResponse = new WeatherResponse(29, 20);

        // Mock the service to return the same response for the first call
        when(weatherService.getWeather("melbourne")).thenReturn(weatherResponse);

        // First request - should call the service and return the response
        ResponseEntity<WeatherResponse> firstResponse = weatherController.getWeather("melbourne");

        // Assert the first response
        assertEquals(200, firstResponse.getStatusCodeValue());
        assertEquals(29, firstResponse.getBody().getTemperatureDegrees());
        assertEquals(20, firstResponse.getBody().getWindSpeed());

        // Verify the service was called once
        verify(weatherService, times(1)).getWeather("melbourne");

        // Simulate a delay of 4 seconds (caching expires after 3 seconds)
        Thread.sleep(4000); // Wait for 4 seconds

        // Second request after cache expiry - should call the service again
        ResponseEntity<WeatherResponse> secondResponse = weatherController.getWeather("melbourne");

        // Assert that the second response is the same as the first response
        assertEquals(200, secondResponse.getStatusCodeValue());
        assertEquals(29, secondResponse.getBody().getTemperatureDegrees());
        assertEquals(20, secondResponse.getBody().getWindSpeed());

        // Verify that the service was called again (because the cache expired)
        verify(weatherService, times(2)).getWeather("melbourne");
    }
}
