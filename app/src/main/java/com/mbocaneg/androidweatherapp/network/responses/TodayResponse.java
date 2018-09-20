package com.mbocaneg.androidweatherapp.network.responses;


import com.mbocaneg.androidweatherapp.model.WeatherModel;

/**
 * Helpful class that is used to hold a WeatherModel object produced by the Deserializer
 * for current weather data
 */
public class TodayResponse {
    public WeatherModel response;

    public TodayResponse(WeatherModel response) {
        this.response = response;
    }
}
