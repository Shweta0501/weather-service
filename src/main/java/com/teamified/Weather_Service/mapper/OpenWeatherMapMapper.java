package com.teamified.Weather_Service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.model.external.OpenWeatherMapResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenWeatherMapMapper {

    private final ObjectMapper objectMapper;

    public OpenWeatherMapMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public WeatherResponse map(String json) throws Exception {
        OpenWeatherMapResponse weatherMapResponse = objectMapper.readValue(json, OpenWeatherMapResponse.class);
        return new WeatherResponse(
                weatherMapResponse.getWind().getSpeed(),
                weatherMapResponse.getMain().getTemp()
        );
    }
}
