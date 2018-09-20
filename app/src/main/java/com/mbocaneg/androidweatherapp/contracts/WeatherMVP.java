package com.mbocaneg.androidweatherapp.contracts;

import com.mbocaneg.androidweatherapp.model.WeatherModel;

import java.util.List;

/**
 * Main MVP contract for the app, sets up the functions needed for the Presenter and
 * View
 */
public interface WeatherMVP {
    interface WeatherPresenter {
        void setLocation();
        void handleRefreshButton();
    }

    interface WeatherView{
        void displayCurrentWeather(WeatherModel model);
        void displayFiveDayWeather(List<WeatherModel> weatherList);

        void displayToast(String message);
    }
}
