package com.teamified.Weather_Service.service;

import com.teamified.Weather_Service.model.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeather(String city);
}

