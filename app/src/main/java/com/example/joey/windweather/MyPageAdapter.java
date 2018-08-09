package com.example.joey.windweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joey.windweather.Now.WeatherNow;
import com.example.joey.windweather.Weather.Forecast;
import com.example.joey.windweather.Weather.Weather;

import java.util.ArrayList;
import java.util.List;

public class MyPageAdapter extends PagerAdapter {
    private static final String TAG = "MyPageAdapter";

    private Context mContext;
    private List<String> mData;
    private List<Weather>mWeathers;
    private List<WeatherNow>mWeatherNows;

     MyPageAdapter(Context context , List<String> list,List<Weather>weathers,List<WeatherNow>weatherNows) {
        mContext = context;
        mData = list;
        mWeathers=weathers;
        mWeatherNows=weatherNows;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.latestcity_item,null);

        TextView cityTitle = (TextView) view.findViewById(R.id.city_title);
        TextView weatherDiscribe=(TextView)view.findViewById(R.id.weather_discribe);
        TextView weatherTmp=(TextView)view.findViewById(R.id.weather_tmp);
        TextView dateToday=(TextView)view.findViewById(R.id.date_today);
        TextView tmp=(TextView)view.findViewById(R.id.tmp);
        TextView weatherInfo=(TextView)view.findViewById(R.id.weather_info);
        RecyclerView weatherForecast=(RecyclerView)view.findViewById(R.id.weather_forecast);

      //  cityTitle.setText(mData.get(position));
        cityTitle.setText(mWeathers.get(position).basic.location);
        weatherDiscribe.setText(mWeatherNows.get(position).now.cond_txt);
        weatherTmp.setText(mWeatherNows.get(position).now.tmpnow);
        dateToday.setText(mWeathers.get(position).forecastList.get(0).date);
        tmp.append(mWeathers.get(position).forecastList.get(0).tmp_max+"      ");
        tmp.append(mWeathers.get(position).forecastList.get(0).tmp_min);

        String weatherText1="今天："+mWeathers.get(position).forecastList.get(0).cond_txt_d+"，最高温"+mWeathers.get(position).forecastList.get(0).tmp_max+"°C。";
        String weatherText2="晚间："+mWeathers.get(position).forecastList.get(0).cond_txt_n+"，最低温"+mWeathers.get(position).forecastList.get(0).tmp_min+"°C。";
        weatherInfo.append(weatherText1);
        weatherInfo.append("\n");
        weatherInfo.append(weatherText2);

        GridLayoutManager layoutManager=new GridLayoutManager(mContext,2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position<=1){
                    return 2;
                }else return 1;
            }
        });
        weatherForecast.setLayoutManager(layoutManager);
        WeatherInfoAdapter mWeatherInfoAdapter=new WeatherInfoAdapter(mContext,mWeathers.get(position).forecastList);
        weatherForecast.setAdapter(mWeatherInfoAdapter);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
