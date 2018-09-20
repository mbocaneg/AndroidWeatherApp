package com.mbocaneg.androidweatherapp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mbocaneg.androidweatherapp.contracts.WeatherMVP;
import com.mbocaneg.androidweatherapp.model.WeatherModel;
import com.mbocaneg.androidweatherapp.network.WeatherService;
import com.mbocaneg.androidweatherapp.network.responses.FiveDayResponse;
import com.mbocaneg.androidweatherapp.network.responses.TodayResponse;
import com.mbocaneg.androidweatherapp.utils.ApiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Presenter portion for the main weather view
public class WeatherPresenterImpl implements WeatherMVP.WeatherPresenter {

    private WeatherMVP.WeatherView view;
    private Context context;
    private WeatherService service;

    private WeatherModel todayWeather;
    private double latitute;
    private double longitude;


    public WeatherPresenterImpl(Context context) {
        this.view = (WeatherMVP.WeatherView) context;
        if (this.service == null) {
            this.service = new WeatherService();
            this.context = context;
        }
    }

    /**
     * function that is called whenever the refresh button within the View
     * is clicked. It tries to find the user's location, and if successful,
     * current weather data as well as a 5 day forecast is fetched. If the
     * user's location is not found, this likely means the user must grant
     * location permissions
     */
    @Override
    public void handleRefreshButton() {
        setLocation();
    }

    /**
     *  Function that tries to pinpoint the user's location. If the app
     *  is granted the appropriate permissions, then a set of latitude
     *  and longitude coordinates is extrapolated from said location,
     *  and the function returns true.
     */
    @Override
    public void setLocation() {

        /**
         * Prompt the user to grant Location permissions(Using Dexter, as stock permission handling mechanisms can get VERY messy)
         * If permission is granted, extract user's latitude and longitude coordinates, and fetch weather data for these coordinates.
         * Else, display a Toast notifying the user location permissions must be granted.
         */
        Dexter.withActivity((Activity) context)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            LocationManager mgr =
                                    (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            Location location = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            latitute = location.getLatitude();
                            longitude = location.getLongitude();

                            fetchCurrentData();
                            fetchFiveDayData();
                        }
                        else {
                            view.displayToast("Please enable Location Permssions");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }


    /**
     * Function that fetches current weather data from the OpenWeathermap API. If
     * successful, it passes that data onto the View, where it is displayed
     */
    public void fetchCurrentData() {
        service
                .getAPI()
                .getCurrentForecastLatLon(latitute, longitude, ApiUtils.TEMP_IMPERIAL, ApiUtils.API_KEY)
                .enqueue(new Callback<TodayResponse>() {
                    @Override
                    public void onResponse(Call<TodayResponse> call, Response<TodayResponse> response) {
                        WeatherModel today = response.body().response;
                        view.displayCurrentWeather(today);
                    }

                    @Override
                    public void onFailure(Call<TodayResponse> call, Throwable t) {
                        view.displayToast("Fetch Error");
                    }
                });
    }

    /**
     * Function that fetches 5 day 3 hour forecast data from the OpenWeathermap API. If
     * successful, it summarizes it into 5 single day forecasts and passes it into
     * the View, where it is displayed
     */
    public void fetchFiveDayData() {
        service
                .getAPI()
                .getFiveDayForecastLatLon(latitute, longitude, ApiUtils.TEMP_IMPERIAL, ApiUtils.API_KEY)
                .enqueue(new Callback<FiveDayResponse>() {
                    @Override
                    public void onResponse(Call<FiveDayResponse> call, Response<FiveDayResponse> response) {
                        List<WeatherModel> wList = response.body().response;
                        List<WeatherModel> synopsis = getFiveDaySummary(wList);
                        view.displayFiveDayWeather(synopsis);
                    }

                    @Override
                    public void onFailure(Call<FiveDayResponse> call, Throwable t) {
                        view.displayToast("Fetch Error");
                    }
                });
    }

    /**
     *  Function that Synopsizes 5 day 3 hour forecast data into 5 single day chunks. It calculates
     *  Average, Min, and Max temperatures, avg humidity, average weather conditiions, and a weather
     *  icon that matches those conditions
     */
    private static List<WeatherModel> getFiveDaySummary(List<WeatherModel> wList){

        /**
         * List that will hold the summarized weather data for each day in the 5 day forecast
         */
        List<WeatherModel> summary = new ArrayList<>();

        /**
         * Accumuator values for Weather data fields that will be synopsized. These include max/min
         * temperatures, average temperature, & average humidity. These will be obtained by simply
         * iterating through the five day 3 hour data and keeping a running min, max, and average for
         * each of the five days.
         */
        int maxTemp = 0;
        int minTemp = 0;
        int averageTemp = 0;
        int averageHumidity = 0;

        /**
         * Non numeric fields that will also be summarized. These will be summarized by taking the
         * string literal that has the most occurances for each day.
         */
        HashMap<String, Integer> descriptionSummary = new HashMap<>();
        HashMap<String, Integer> iconSummary = new HashMap<>();

        String day = "";

        /**
         * iterator value that keep track of three hour chunks
         */
        int hourIndex = 0;

        /**
         * iterator value that keeps track of the elements that are iterated through. Mainly used
         * for checking for the last element of the list
         */
        int index = 0;

        /**
         * boolean that simply checks for the first element of the list
         */
        boolean firstRun = true;

        for(WeatherModel w: wList){

            /**
             * If this is the first element, simply set up each accumulator value
             */
            if(firstRun == true){
                maxTemp = wList.get(0).getTemperature();
                minTemp = wList.get(0).getTemperature();
                averageTemp = wList.get(0).getTemperature();

                averageHumidity = wList.get(0).getHumidity();

                day = wList.get(0).getDay();

                String wDescription = wList.get(0).getDescription();
                descriptionSummary.put(wDescription, 1);

                String wIcon = wList.get(0).getIcon();
                iconSummary.put(wIcon, 1);

                firstRun = false;
                index++;
                hourIndex++;
                continue;
            }

            /**
             * If this 3 hour chunk is still part of the same day as the previous, agregate this data
             * into accumulator values
             */
            if(day.equals(w.getDay())){
                if(w.getTemperature() > maxTemp)
                    maxTemp = w.getTemperature();
                if(w.getTemperature() < minTemp)
                    minTemp = w.getTemperature();

                String wDescription = w.getDescription();
                if(descriptionSummary.containsKey(wDescription)){
                    int num = descriptionSummary.get(wDescription);
                    descriptionSummary.put(wDescription, num+1);
                }
                else {
                    descriptionSummary.put(wDescription, 1);
                }

                String wIcon = w.getIcon();
                if(iconSummary.containsKey(wIcon)){
                    int num = iconSummary.get(wIcon);
                    iconSummary.put(wIcon, num+1);
                }
                else {
                    iconSummary.put(wIcon, 1);
                }

                averageTemp += w.getTemperature();

                averageHumidity += w.getHumidity();

                hourIndex++;
            }

            /**
             * If the current 3 hour chunk is not part of the same day, or
             * we have reached the end of the list, check the runnin avg/min/max
             * values, and construct a weather model object with these fields.
             * Finally, add this model object to the synopsized list.
             */
            if(!day.equals(w.getDay()) || index == wList.size()-1){
                WeatherModel daySummary = new WeatherModel();

                daySummary.setDay(day);

                daySummary.setTemperature(averageTemp/hourIndex);

                daySummary.setHumidity(averageHumidity/hourIndex);

                daySummary.setTempMax(maxTemp);

                daySummary.setTempMin(minTemp);

                int descFreq = 0;
                String descKey = "";
                for(String key: descriptionSummary.keySet()){
                    if(descriptionSummary.get(key) > descFreq)
                        descKey = key;
                }

                daySummary.setDescription(descKey);

                int iconFreq = 0;
                String iconKey = "";
                for(String key: iconSummary.keySet()){
                    if(iconSummary.get(key) > iconFreq)
                        iconKey = key;
                }

                iconKey = iconKey.substring(0, iconKey.length() - 1);
                iconKey += 'd';

                daySummary.setIcon(iconKey);

                summary.add(daySummary);

                maxTemp = w.getTemperature();
                minTemp = w.getTemperature();
                day = w.getDay();

                hourIndex = 0;
                averageTemp = 0;
                averageHumidity = 0;
            }
            index++;
        }
        return summary;
    }


}
