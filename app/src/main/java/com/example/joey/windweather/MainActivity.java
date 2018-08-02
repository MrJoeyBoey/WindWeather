package com.example.joey.windweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<String>latestCity=new ArrayList<>(3);
    private ImageView icon1,icon2,icon3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView cityAdd=(ImageView)findViewById(R.id.city_add);
        ImageView aboutMe=(ImageView)findViewById(R.id.about_me);
        ViewPager viewPager=(ViewPager)findViewById(R.id.view_pager);
        icon1=(ImageView)findViewById(R.id.icon1);
        icon2=(ImageView)findViewById(R.id.icon2);
        icon3=(ImageView)findViewById(R.id.icon3);
        icon1.setSelected(true);

        SharedPreferences pref=getSharedPreferences("LatestCity", MODE_PRIVATE);
        String name1=pref.getString("city1","");
        String name2=pref.getString("city2","");
        SharedPreferences pref2=getSharedPreferences("LatestPage", MODE_PRIVATE);
        int latestpage=pref2.getInt("latestpage",0);

        latestCity.add(0,"宁波");
        if(!name1.isEmpty()){
            latestCity.add(1,name1);
        }
        if(!name2.isEmpty()){
            latestCity.add(2,name2);
        }
        Log.d(TAG, "看看存储1"+name1);
        Log.d(TAG, "看看存储2"+name2);
        Log.d(TAG, "看看大小"+latestCity.size());
        if(latestCity.size()==1){
            icon2.setVisibility(View.GONE);
            icon3.setVisibility(View.GONE);
        }else if(latestCity.size()==2){
            icon3.setVisibility(View.GONE);

        }
        if(getIntent().getBooleanExtra("defaut",false)&&latestCity.size()==3){
            latestpage=2;
        }else if(getIntent().getBooleanExtra("defaut",false)&&latestCity.size()==2){
            latestpage=1;
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
        viewPager.setAdapter(new MyPageAdapter(this,latestCity));
        viewPager.setCurrentItem(latestpage);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
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
            }
        });

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
