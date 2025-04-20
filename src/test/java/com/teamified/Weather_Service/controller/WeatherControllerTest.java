package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class WeatherApiControllerTest {

    private WeatherService weatherService;
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        weatherController = new WeatherController(weatherService);
    }

    @Test
    void testGetWeatherSuccess() throws Exception {
        // Mock the weather service response
        WeatherResponse weatherResponse = new WeatherResponse(29, 20);
        when(weatherService.getWeather("melbourne")).thenReturn(weatherResponse);

        // Call the controller method
        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeather("melbourne");

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(29, responseEntity.getBody().getTemperatureDegrees());
        assertEquals(20, responseEntity.getBody().getWindSpeed());

        // Verify the service method was called
        verify(weatherService, times(1)).getWeather("melbourne");
    }

    @Test
    void testFailoverToOpenWeatherMap() throws Exception {
        // Mock WeatherStack failure
        when(weatherService.getWeather("melbourne")).thenThrow(new RuntimeException("Primary provider failed"));

        // Mock the weather service response for failover
        WeatherResponse fallbackResponse = new WeatherResponse(25, 15);
        when(weatherService.getWeather("melbourne")).thenReturn(fallbackResponse);

        // Call the controller method
        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeather("melbourne");

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(25, responseEntity.getBody().getTemperatureDegrees());
        assertEquals(15, responseEntity.getBody().getWindSpeed());

        // Verify the service method was called
        verify(weatherService, times(2)).getWeather("melbourne");  // Called once for primary, once for failover
    }

    @Test
    void testBothProvidersFail() throws Exception {
        // Mock both WeatherStack and OpenWeatherMap failures
        when(weatherService.getWeather("melbourne")).thenThrow(new RuntimeException("Both providers failed"));

        // Call the controller method and expect an exception
        assertThrows(RuntimeException.class, () -> weatherController.getWeather("melbourne"));

        // Verify the service method was called
        verify(weatherService, times(2)).getWeather("melbourne");  // Called once for each provider
    }
}
