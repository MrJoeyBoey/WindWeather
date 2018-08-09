package com.example.joey.windweather.Now;

import com.google.gson.annotations.SerializedName;

public class WeatherNow {
    @SerializedName("basic")
    public Basic basic;

    @SerializedName("update")
    public Update update;

    @SerializedName("status")
    public String status;

    @SerializedName("now")
    public Now now;
}
