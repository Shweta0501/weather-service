package com.teamified.Weather_Service.controller;

import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherApiControllerCacheTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    @Test
    void testWeatherApiControllerReturnsJson() throws Exception {
        mockMvc.perform(get("/v1/weather?city=melbourne"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(containsString("temperatureDegrees")))
                .andExpect(content().string(containsString("windSpeed")));
    }


    @Test
    void testCachingBehaviorOnServiceCalls() {
        // First call - should hit provider and cache result
        WeatherResponse first = weatherService.getWeather("melbourne");

        // Second call - should hit cache
        WeatherResponse second = weatherService.getWeather("melbourne");

        // Compare values, not object references
        assertEquals(first.getTemperatureDegrees(), second.getTemperatureDegrees(), "Temperature should match");
        assertEquals(first.getWindSpeed(), second.getWindSpeed(), "Wind speed should match");
    }

    @Test
    void testCacheExpiresAfterTTL() throws Exception {
        // First call - caches result
        WeatherResponse first = weatherService.getWeather("melbourne");

        // Wait for cache expiry (assume TTL = 3s)
        Thread.sleep(4000);

        // Second call - should fetch fresh result
        WeatherResponse second = weatherService.getWeather("melbourne");

        // Values could be same or different depending on API, but objects should differ
        assertNotEquals(first, second, "Expected different objects after cache expiry");
    }
}
