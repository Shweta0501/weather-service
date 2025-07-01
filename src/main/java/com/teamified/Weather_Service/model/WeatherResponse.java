package com.teamified.Weather_Service.model;

public class WeatherResponse {

    private double windSpeed;
    private double temperatureDegrees;

    public WeatherResponse() {}

    public WeatherResponse(double windSpeed, double temperatureDegrees) {
        this.windSpeed = windSpeed;
        this.temperatureDegrees = temperatureDegrees;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getTemperatureDegrees() {
        return temperatureDegrees;
    }

    public void setTemperatureDegrees(double temperatureDegrees) {
        this.temperatureDegrees = temperatureDegrees;
    }
}