package com.teamified.Weather_Service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamified.Weather_Service.exception.WeatherServiceException;
import com.teamified.Weather_Service.model.WeatherResponse;
import com.teamified.Weather_Service.model.external.WeatherStackResponse;
import org.springframework.stereotype.Component;

@Component
public class WeatherStackMapper {

    private final ObjectMapper objectMapper;

    public WeatherStackMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public WeatherResponse map(String json) {
        try {
            WeatherStackResponse response = objectMapper.readValue(json, WeatherStackResponse.class);

            if (response.getCurrent() == null) {
                throw new WeatherServiceException("WeatherStack response missing 'key' data");
            }

            return new WeatherResponse(
                response.getCurrent().getWindSpeed(),
                response.getCurrent().getTemperature()
            );

        } catch (WeatherServiceException ex) {
            throw ex; // Bubble up cleanly
        } catch (Exception ex) {
            throw new WeatherServiceException("Error mapping WeatherStack response", ex);
        }
    }
}
