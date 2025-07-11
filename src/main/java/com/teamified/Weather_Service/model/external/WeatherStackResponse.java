package com.teamified.Weather_Service.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherStackResponse {
    private Current current;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public static class Current {
        @JsonProperty("temperature")
        private double temperature;

        @JsonProperty("wind_speed")
        private double windSpeed;

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }
    }
}
