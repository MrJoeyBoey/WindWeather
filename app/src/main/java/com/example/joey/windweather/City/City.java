package com.example.joey.windweather.City;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {
    @SerializedName("status")
    public String status;

    @SerializedName("basic")
    public List<Basic> basicList;
}
