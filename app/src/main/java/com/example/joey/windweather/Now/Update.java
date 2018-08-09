package com.example.joey.windweather.Now;

import com.google.gson.annotations.SerializedName;

public class Update {
    @SerializedName("loc")
    public String loc;

    @SerializedName("utc")
    public String utc;
}
