package com.example.joey.windweather.Weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    @SerializedName("basic")
    public Basic basic;

    @SerializedName("update")
    public Undate undate;

    @SerializedName("status")
    public String status;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
