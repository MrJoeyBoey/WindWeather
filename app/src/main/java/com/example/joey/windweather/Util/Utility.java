package com.example.joey.windweather.Util;

import com.example.joey.windweather.City.City;
import com.example.joey.windweather.Now.WeatherNow;
import com.example.joey.windweather.Weather.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static City handleCityResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String cityContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(cityContent,City.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WeatherNow handleWeatherNowResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherNowContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherNowContent,WeatherNow.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
