package com.teamified.Weather_Service.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {

    @JsonProperty("code")
    private String code;  // safer as String, because API sends "404" sometimes

    @JsonProperty("message")
    private String message;

    private Main main;

    private Wind wind;

    // getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    // nested classes
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private double temp;

        public double getTemp() {
            return temp;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private double speed;

        public double getSpeed() {
            return speed;
        }
    }
}
