package com.teamified.Weather_Service.model;

public class WeatherResponse {

    private int windSpeed;
    private int temperatureDegrees;

    public WeatherResponse() {}

    public WeatherResponse(int windSpeed, int temperatureDegrees) {
        this.windSpeed = windSpeed;
        this.temperatureDegrees = temperatureDegrees;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getTemperatureDegrees() {
        return temperatureDegrees;
    }

    public void setTemperatureDegrees(int temperatureDegrees) {
        this.temperatureDegrees = temperatureDegrees;
    }
}