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
        private int temperature;

        @JsonProperty("wind_speed")
        private int windSpeed;

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(int windSpeed) {
            this.windSpeed = windSpeed;
        }
    }
}
