package com.teamified.Weather_Service.service;

import com.teamified.Weather_Service.client.OpenWeatherMapClient;
import com.teamified.Weather_Service.client.WeatherStackClient;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceImplTest {

    private WeatherStackClient weatherStackClient;
    private OpenWeatherMapClient openWeatherMapClient;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherStackClient = mock(WeatherStackClient.class);
        openWeatherMapClient = mock(OpenWeatherMapClient.class);
        weatherService = new WeatherServiceImpl(weatherStackClient, openWeatherMapClient);
    }

    @Test
    void testPrimaryProviderSuccess() throws Exception {
        // Mock WeatherStack response
        WeatherResponse response = new WeatherResponse(25, 10);
        when(weatherStackClient.getWeather("melbourne")).thenReturn(response);

        // Call the service method
        WeatherResponse result = weatherService.getWeather("melbourne");

        // Assertions
        assertEquals(25, result.getTemperatureDegrees());
        assertEquals(10, result.getWindSpeed());

        // Verifications
        verify(weatherStackClient, times(1)).getWeather("melbourne");
        verify(openWeatherMapClient, never()).getWeather(anyString());
    }

    @Test
    void testFailoverToOpenWeatherMap() throws Exception {
        // Mock WeatherStack failure
        when(weatherStackClient.getWeather("melbourne")).thenThrow(new RuntimeException("Primary failed"));

        // Mock OpenWeatherMap response
        WeatherResponse fallbackResponse = new WeatherResponse(20, 5);
        when(openWeatherMapClient.getWeather("melbourne")).thenReturn(fallbackResponse);

        // Call the service method
        WeatherResponse result = weatherService.getWeather("melbourne");

        // Assertions
        assertEquals(20, result.getTemperatureDegrees());
        assertEquals(5, result.getWindSpeed());

        // Verifications
        verify(weatherStackClient, times(1)).getWeather("melbourne");
        verify(openWeatherMapClient, times(1)).getWeather("melbourne");
    }

    @Test
    void testBothProvidersFail() throws Exception {
        // Mock both WeatherStack and OpenWeatherMap failures
        when(weatherStackClient.getWeather("melbourne")).thenThrow(new RuntimeException("Primary failed"));
        when(openWeatherMapClient.getWeather("melbourne")).thenThrow(new RuntimeException("Failover failed"));

        // Assert exception is thrown when both fail
        assertThrows(RuntimeException.class, () -> weatherService.getWeather("melbourne"));

        // Verifications
        verify(weatherStackClient, times(1)).getWeather("melbourne");
        verify(openWeatherMapClient, times(1)).getWeather("melbourne");
    }
}

