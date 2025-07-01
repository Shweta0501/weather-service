package com.teamified.Weather_Service.service;

import com.teamified.Weather_Service.client.OpenWeatherMapClient;
import com.teamified.Weather_Service.client.WeatherStackClient;
import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.impl.WeatherServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WeatherServiceImplTest {

    private WeatherStackClient weatherStackClient;
    private OpenWeatherMapClient openWeatherMapClient;
    private CacheManager cacheManager;
    private WeatherServiceImpl weatherServiceImpl;

    @BeforeEach
    void setUp() {
        weatherStackClient = mock(WeatherStackClient.class);
        openWeatherMapClient = mock(OpenWeatherMapClient.class);
        cacheManager = new ConcurrentMapCacheManager();  // simple in-memory cache
        weatherServiceImpl = new WeatherServiceImpl(weatherStackClient, openWeatherMapClient, cacheManager);
    }

    @Test
    void testPrimaryProviderSuccess() throws Exception {
        // Mock WeatherStack response
        WeatherResponse response = new WeatherResponse(10, 25);
        when(weatherStackClient.getWeather("melbourne")).thenReturn(response);

        // Call the service method
        WeatherResponse result = weatherServiceImpl.getWeather("melbourne");

        // Assertions
        assertEquals(10, result.getWindSpeed());
        assertEquals(25, result.getTemperatureDegrees());

        // Verifications
        verify(weatherStackClient, times(1)).getWeather("melbourne");
        verify(openWeatherMapClient, never()).getWeather(anyString());
    }

    @Test
    void testFailoverToOpenWeatherMap() throws Exception {
        // WeatherStack fails
        when(weatherStackClient.getWeather("melbourne"))
                .thenThrow(new RuntimeException("Primary provider failed"));

        // OpenWeatherMap succeeds
        WeatherResponse fallbackResponse = new WeatherResponse(15, 25);
        when(openWeatherMapClient.getWeather("melbourne"))
                .thenReturn(fallbackResponse);

        WeatherResponse result = weatherServiceImpl.getWeather("melbourne");

        // Assertions
        assertEquals(15, result.getWindSpeed());
        assertEquals(25, result.getTemperatureDegrees());

        verify(weatherStackClient, times(1)).getWeather("melbourne");
        verify(openWeatherMapClient, times(1)).getWeather("melbourne");
    }

    @Test
    void testBothProvidersFail() {
        when(weatherStackClient.getWeather("melbourne"))
                .thenThrow(new RuntimeException("Primary provider failed"));
        when(openWeatherMapClient.getWeather("melbourne"))
                .thenThrow(new RuntimeException("Secondary provider failed"));

        WeatherServiceException ex = assertThrows(
                WeatherServiceException.class,
                () -> weatherServiceImpl.getWeather("melbourne")
        );

        // Print or log message if needed
        System.out.println("Actual exception message: " + ex.getMessage());

        // Make an assertion that matches your actual message
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().length() > 0);

        verify(weatherStackClient, times(1)).getWeather("melbourne");
        verify(openWeatherMapClient, times(1)).getWeather("melbourne");
    }
}
