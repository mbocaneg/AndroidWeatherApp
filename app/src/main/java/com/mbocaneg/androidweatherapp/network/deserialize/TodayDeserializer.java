package com.mbocaneg.androidweatherapp.network.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mbocaneg.androidweatherapp.model.WeatherModel;
import com.mbocaneg.androidweatherapp.network.responses.TodayResponse;
import com.mbocaneg.androidweatherapp.utils.DateTimeUtils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Custom Deserializer that parses a currect weather response into a WeatherModel object.
 */
public class TodayDeserializer implements JsonDeserializer<TodayResponse> {
    @Override
    public TodayResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        /**
         * instantiate empty WeatherModel object, whose properties will be populated
         */
        WeatherModel w = new WeatherModel();
        JsonObject jobject = json.getAsJsonObject();
        JsonObject main = jobject.get("main").getAsJsonObject();

        //extract temp information, and add it to WeatherModel object
        Double temp = main.get("temp").getAsDouble();
        w.setTemperature((int) Math.round(temp));

        //extract humidity information, and add it to WeatherModel object
        int humidity = main.get("humidity").getAsInt();
        w.setHumidity(humidity);

        //extract weather description information, and add it to WeatherModel object
        JsonElement weather = jobject.get("weather").getAsJsonArray().get(0);
        String description = weather.getAsJsonObject().get("description").getAsString();
        w.setDescription(description);

        //extract icon, and add it to WeatherModel object
        String icon = weather.getAsJsonObject().get("icon").getAsString();
        w.setIcon(icon);

        //extract date information, and add it to WeatherModel object
        long dt = jobject.get("dt").getAsLong();
        String day = DateTimeUtils.extractDay(new Date(dt*1000L));
        w.setDay(day);

        //extract location information, and add it to WeatherModel object
        String city = jobject.get("name").getAsString();
        w.setCity(city);

        //return WeatherModel object encapsulated inside TodayResponse object
        return new TodayResponse(w);
    }
}
