package com.mbocaneg.androidweatherapp.network.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mbocaneg.androidweatherapp.model.WeatherModel;
import com.mbocaneg.androidweatherapp.network.responses.FiveDayResponse;
import com.mbocaneg.androidweatherapp.utils.DateTimeUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Custom Deserializer that parses 5 day 3 hour forecast responses into a list
 * of WeatherModel objects, encapsulated in a FiveDayResponse object
 */
public class FiveDayDeserializer implements JsonDeserializer<FiveDayResponse> {
    @Override
    public FiveDayResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        /**
         * List of WeatherModel objects that will hold parsed data
         */
        List<WeatherModel> wList = new ArrayList<>();
        JsonObject jobject = json.getAsJsonObject();

        /**
         * REST response is a JSON array, so it must be parsed element by element
         */
        JsonArray jList = jobject.getAsJsonArray("list");
        for (JsonElement el: jList){
            /**
             * instantiate empty WeatherModel object, whose properties will be populated
             * and added to the List
             */
            WeatherModel w = new WeatherModel();

            JsonObject main = el.getAsJsonObject()
                    .get("main").getAsJsonObject();

            JsonObject weather = el.getAsJsonObject()
                    .get("weather").getAsJsonArray().get(0).getAsJsonObject();

            //extract date information, and add it to WeatherModel object
            long dt = el.getAsJsonObject()
                    .get("dt").getAsLong();
            String day = DateTimeUtils.extractDay(new Date(dt*1000L));
            w.setDay(day);

            //extract temp information, and add it to WeatherModel object
            int temp = Math.round((int)main.get("temp").getAsDouble());
            w.setTemperature(temp);

            //extract humidity information, and add it to WeatherModel object
            int humidity = main.get("humidity").getAsInt();
            w.setHumidity(humidity);

            //extract weather description information, and add it to WeatherModel object
            String description = weather.get("description").getAsString();
            w.setDescription(description);

            //extract icon, and add it to WeatherModel object
            String icon = weather.get("icon").getAsString();
            w.setIcon(icon);

            //add populated WeatherModel to List
            wList.add(w);

        }

        //return List encapsulated inside FiveDayResponse object
        return new FiveDayResponse(wList);
    }
}
