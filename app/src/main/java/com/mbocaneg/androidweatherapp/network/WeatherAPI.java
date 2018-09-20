package com.mbocaneg.androidweatherapp.network;

import com.mbocaneg.androidweatherapp.network.responses.FiveDayResponse;
import com.mbocaneg.androidweatherapp.network.responses.TodayResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface that holds the REST endpoints the app will be calling. One endpoint
 * returns current weather data, the other returns data for a 5 day, 3 hour forecast
 */
public interface WeatherAPI {

    @GET("data/2.5/weather")
    Call<TodayResponse> getCurrentForecastLatLon(@Query("lat") double lat,
                                                 @Query("lon") double lon,
                                                 @Query("units") String units,
                                                 @Query("appid") String appid);

    @GET("data/2.5/forecast")
    Call<FiveDayResponse> getFiveDayForecastLatLon(@Query("lat") double lat,
                                                   @Query("lon") double lon,
                                                   @Query("units") String units,
                                                   @Query("appid") String appid);

}
