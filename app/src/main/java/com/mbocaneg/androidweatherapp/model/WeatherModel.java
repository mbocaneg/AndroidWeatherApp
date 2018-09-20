package com.mbocaneg.androidweatherapp.model;

/**
 * Class that represents the weather conditions for a given day
 */
public class WeatherModel {
    private int temperature;
    private int tempMin;
    private int tempMax;
    private String description;
    private int humidity;
    private String day;
    private String icon;

    private String city;

    public WeatherModel() {
        this.temperature = 0;
        this.tempMin = 0;
        this.tempMax = 0;

        this.description = "";
        this.humidity = 0;
        this.day = "";
        this.icon = "";
    }

    public WeatherModel(int temperature, int tempMin, int tempMax, String description, int humidity, String day, String icon) {
        this.temperature = temperature;
        this.tempMin = tempMin;
        this.tempMax = tempMax;

        this.description = description;
        this.humidity = humidity;
        this.day = day;
        this.icon = icon;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTempMin() { return tempMin; }

    public void setTempMin(int tempMin) { this.tempMin = tempMin; }

    public int getTempMax() { return tempMax; }

    public void setTempMax(int tempMax) { this.tempMax = tempMax; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
