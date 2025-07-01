package com.teamified.Weather_Service.exception;

/**
 * Custom exception to represent errors when interacting with external weather services.
 */
public class WeatherServiceException extends RuntimeException {

    public WeatherServiceException(String message) {
        super(message);
    }

    public WeatherServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
