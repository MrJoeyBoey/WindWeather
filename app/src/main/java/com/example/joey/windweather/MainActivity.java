package com.example.joey.windweather;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.joey.windweather.Now.WeatherNow;
import com.example.joey.windweather.Util.HttpUtil;
import com.example.joey.windweather.Util.Utility;
import com.example.joey.windweather.Weather.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<String>latestCity=new ArrayList<>();
    private ImageView icon1,icon2,icon3;
    private Weather weather;
    private List<Weather> weathers=new ArrayList<>();
    private List<WeatherNow>weatherNows=new ArrayList<>();
    private String weather1,weather2,weather3;
    private String weathernow1,weathernow2,weathernow3;
    private long mLastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);

        final LinearLayout mainLayout=(LinearLayout)findViewById(R.id.main_layout);
        ImageView cityAdd=(ImageView)findViewById(R.id.city_add);
        ImageView aboutMe=(ImageView)findViewById(R.id.about_me);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        icon1=(ImageView)findViewById(R.id.icon1);
        icon2=(ImageView)findViewById(R.id.icon2);
        icon3=(ImageView)findViewById(R.id.icon3);
        icon1.setSelected(true);



        SharedPreferences pref=getSharedPreferences("LatestCity", MODE_PRIVATE);
        String name1=pref.getString("city1","");
        String name2=pref.getString("city2","");
        SharedPreferences pref2=getSharedPreferences("LatestPage", MODE_PRIVATE);
        int latestpage = pref2.getInt("latestpage", 0);

        SharedPreferences pref3=getSharedPreferences("Weather",MODE_PRIVATE);
        weather1=pref3.getString("responseWeather1","");
        weather2=pref3.getString("responseWeather2","");
        weather3=pref3.getString("responseWeather3","");

        SharedPreferences pref4=getSharedPreferences("WeatherNow",MODE_PRIVATE);
        weathernow1=pref4.getString("responseNow1","");
        weathernow2=pref4.getString("responseNow2","");
        weathernow3=pref4.getString("responseNow3","");

        if(weather1.isEmpty()){
            queryWeatherNow("auto_ip");
            queryWeather("auto_ip");
        }else {
            weathers.add(Utility.handleWeatherResponse(weather1));
            weatherNows.add(Utility.handleWeatherNowResponse(weathernow1));
        }

        latestCity.add(weatherNows.get(0).basic.location);

        if(!name1.isEmpty()){
            latestCity.add(name1);
        }
        if(!name2.isEmpty()){
            latestCity.add(name2);
        }
        if(latestCity.size()==1){
            icon2.setVisibility(View.GONE);
            icon3.setVisibility(View.GONE);
        }else if(latestCity.size()==2){
            icon3.setVisibility(View.GONE);
        }
        if(getIntent().getBooleanExtra("defaut",false)&&latestCity.size()==3){
            latestpage =2;
        }else if(getIntent().getBooleanExtra("defaut",false)&&latestCity.size()==2){
            latestpage =1;
        }

        switch (latestpage){
            case 0:
                icon1open();
                break;
            case 1:
                icon2open();
                break;
            case 2:
                icon3open();
                break;
        }

        switch (latestCity.size()){
            case 2:
                if(weather2.isEmpty()){
                    queryWeather(latestCity.get(1));
                    queryWeatherNow(latestCity.get(1));
                }else {
                    weathers.add(Utility.handleWeatherResponse(weather2));
                    weatherNows.add(Utility.handleWeatherNowResponse(weathernow2));
                }
                break;
            case 3:
                if(getIntent().getBooleanExtra("defaut",false)){
                    if(weather3.isEmpty()){
                        weathers.add(Utility.handleWeatherResponse(weather2));
                        weatherNows.add(Utility.handleWeatherNowResponse(weathernow2));
                        queryWeather(latestCity.get(2));
                        queryWeatherNow(latestCity.get(2));
                    }else {
                        weathers.add(Utility.handleWeatherResponse(weather3));
                        weatherNows.add(Utility.handleWeatherNowResponse(weathernow3));
                        queryWeather(latestCity.get(2));
                        queryWeatherNow(latestCity.get(2));
                    }
                }else {
                    weathers.add(Utility.handleWeatherResponse(weather2));
                    weathers.add(Utility.handleWeatherResponse(weather3));
                    weatherNows.add(Utility.handleWeatherNowResponse(weathernow2));
                    weatherNows.add(Utility.handleWeatherNowResponse(weathernow3));
                }
                break;
        }

        if(weatherNows.get(latestpage).now.cond_txt.contains("晴")||weatherNows.get(latestpage).now.cond_txt.contains("云")){
            mainLayout.setBackgroundResource(R.drawable.sunnyday);
        }else if(weatherNows.get(latestpage).now.cond_txt.contains("雨")){
            mainLayout.setBackgroundResource(R.drawable.rainyday);
        }else if(weatherNows.get(latestpage).now.cond_txt.contains("阴")){
            mainLayout.setBackgroundResource(R.drawable.cloudyday);
        }
        
        viewPager.setAdapter(new MyPageAdapter(MainActivity.this,latestCity,weathers,weatherNows));
        viewPager.setCurrentItem(latestpage);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(weatherNows.get(position).now.cond_txt.contains("晴")||weatherNows.get(position).now.cond_txt.contains("云")){
                    mainLayout.setBackgroundResource(R.drawable.sunnyday);
                }else if(weatherNows.get(position).now.cond_txt.contains("雨")){
                    mainLayout.setBackgroundResource(R.drawable.rainyday);
                }else if(weatherNows.get(position).now.cond_txt.contains("阴")){
                    mainLayout.setBackgroundResource(R.drawable.cloudyday);
                }
                switch (position){
                    case 0:
                        icon1open();
                        break;
                    case 1:
                        icon2open();
                        break;
                    case 2:
                        icon3open();
                        break;
                }
                SharedPreferences.Editor editor=getSharedPreferences("LatestPage", MODE_PRIVATE).edit();
                editor.putInt("latestpage",position);
                editor.apply();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        cityAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            }
        });

        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AboutMeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
            }
        });

    }

    public void queryWeather(String cityId){
        final Object obj = new Object();
        final String weatherUrl="https://free-api.heweather.com/s6/weather/forecast?location="+cityId+"&key=e9b90a4dff4e4ae5b974aa29b3466cfe";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String responseWeather=response.body().string();
                weather= Utility.handleWeatherResponse(responseWeather);

                if(weather1.isEmpty()){
                    weathers.add(weather);

                    SharedPreferences.Editor editor=getSharedPreferences("Weather", MODE_PRIVATE).edit();
                    editor.putString("responseWeather1",responseWeather);
                    editor.apply();
                }else if(weather2.isEmpty()){
                    weathers.add(weather);

                    SharedPreferences.Editor editor=getSharedPreferences("Weather", MODE_PRIVATE).edit();
                    editor.putString("responseWeather2",responseWeather);
                    editor.apply();
                }else if(weather3.isEmpty()){
                    weathers.add(weather);

                    SharedPreferences.Editor editor=getSharedPreferences("Weather", MODE_PRIVATE).edit();
                    editor.putString("responseWeather3",responseWeather);
                    editor.apply();
                }else {
                    weathers.add(weather);
                    SharedPreferences.Editor editor=getSharedPreferences("Weather", MODE_PRIVATE).edit();
                    editor.putString("responseWeather3",responseWeather);
                    editor.putString("responseWeather2",weather3);
                    editor.apply();
                }
                synchronized (obj){
                    obj.notify();
                }
/*                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });*/
            }
            @Override
            public void onFailure(Call call, IOException e) {
            }


        });
        synchronized (obj){
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void queryWeatherNow(String cityId){
        final Object obj = new Object();
        final String url="https://free-api.heweather.com/s6/weather/now?location="+cityId+"&key=e9b90a4dff4e4ae5b974aa29b3466cfe";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseNow=response.body().string();
                WeatherNow weatherNow=Utility.handleWeatherNowResponse(responseNow);

                if(weathernow1.isEmpty()){
                    weatherNows.add(weatherNow);

                    SharedPreferences.Editor editor=getSharedPreferences("WeatherNow", MODE_PRIVATE).edit();
                    editor.putString("responseNow1",responseNow);
                    editor.apply();
                }else if(weathernow2.isEmpty()){
                    weatherNows.add(weatherNow);

                    SharedPreferences.Editor editor=getSharedPreferences("WeatherNow", MODE_PRIVATE).edit();
                    editor.putString("responseNow2",responseNow);
                    editor.apply();
                }else if(weathernow3.isEmpty()){
                    weatherNows.add(weatherNow);

                    SharedPreferences.Editor editor=getSharedPreferences("WeatherNow", MODE_PRIVATE).edit();
                    editor.putString("responseNow3",responseNow);
                    editor.apply();
                }else {
                    weatherNows.add(weatherNow);
                    SharedPreferences.Editor editor=getSharedPreferences("WeatherNow", MODE_PRIVATE).edit();
                    editor.putString("responseNow",responseNow);
                    editor.putString("responseNow",weather3);
                    editor.apply();
                }

                synchronized (obj){
                    obj.notify();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
        synchronized (obj){
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void icon1open(){
        icon1.setSelected(true);
        icon2.setSelected(false);
        icon3.setSelected(false);
    }
    private void icon2open(){
        icon2.setSelected(true);
        icon1.setSelected(false);
        icon3.setSelected(false);
    }
    private void icon3open(){
        icon3.setSelected(true);
        icon2.setSelected(false);
        icon1.setSelected(false);
    }
}
