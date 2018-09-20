package com.mbocaneg.androidweatherapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mbocaneg.androidweatherapp.network.deserialize.FiveDayDeserializer;
import com.mbocaneg.androidweatherapp.network.deserialize.TodayDeserializer;
import com.mbocaneg.androidweatherapp.network.responses.FiveDayResponse;
import com.mbocaneg.androidweatherapp.network.responses.TodayResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service class that sets up a retrofit instance with the base openweathermap api
 * URL, as well as the custom Deserializer needed to parse REST JSON responses
 */
public class WeatherService {
    private Retrofit retrofit;
    private static final String BASE_URL_OWM = "http://api.openweathermap.org/";

    public WeatherAPI getAPI(){
        if(retrofit == null){
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL_OWM)
                    .addConverterFactory(buildGsonConverter())
                    .build();
        }

        return retrofit.create(WeatherAPI.class);
    }

    private static GsonConverterFactory buildGsonConverter(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .registerTypeAdapter(TodayResponse.class, new TodayDeserializer())
                .registerTypeAdapter(FiveDayResponse.class, new FiveDayDeserializer());

        Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }



}
