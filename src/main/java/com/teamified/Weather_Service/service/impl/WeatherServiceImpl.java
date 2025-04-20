package com.teamified.Weather_Service.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.teamified.Weather_Service.client.OpenWeatherMapClient;
import com.teamified.Weather_Service.client.WeatherStackClient;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.service.WeatherService;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherStackClient weatherStackClient;
    private final OpenWeatherMapClient openWeatherMapClient;

    public WeatherServiceImpl(WeatherStackClient weatherStackClient, OpenWeatherMapClient openWeatherMapClient) {
        this.weatherStackClient = weatherStackClient;
        this.openWeatherMapClient = openWeatherMapClient;
    }

    @Override
    @Cacheable(value = "weather", key = "#city")
    public WeatherResponse getWeather(String city) {
        try {
            return weatherStackClient.getWeather(city);
        } catch (Exception e) {
            try {
                return openWeatherMapClient.getWeather(city);
            } catch (Exception ex) {
                return new WeatherResponse(0, 0);
            }
        }
    }
}