package com.mbocaneg.androidweatherapp.network.responses;

import com.mbocaneg.androidweatherapp.model.WeatherModel;

import java.util.List;

/**
 * Helpful class that is used to hold a WeatherModel list produced by the Deserializer
 * for 5 day 3 hour forecast
 */
public class FiveDayResponse {
    public List<WeatherModel> response;

    public FiveDayResponse(List<WeatherModel> response) {
        this.response = response;
    }
}
