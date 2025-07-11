package com.teamified.Weather_Service.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {

    @JsonProperty("main")
    private Main main;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("cod")
    private int code;

    @JsonProperty("message")
    private String message;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private double temp;

        public double getTemp() {
            return temp;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        @JsonProperty("speed")
        private double speed;

        public double getSpeed() {
            return speed;
        }
    }
}
