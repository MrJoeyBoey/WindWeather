package com.example.joey.windweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joey.windweather.City.Basic;
import com.example.joey.windweather.City.City;
import com.example.joey.windweather.Util.HttpUtil;
import com.example.joey.windweather.Util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private List<String> hotCities=new ArrayList<String>(){
        {
            add("上海");
            add("北京");
            add("深圳");
            add("广州");
            add("南京");
            add("杭州");
            add("重庆");
            add("武汉");
        }
    };

    private RecyclerView searchList;
    private LinearLayout hotCityTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        TextView searchCancel=(TextView)findViewById(R.id.cancel_search);
        final EditText search=(EditText)findViewById(R.id.search);
        searchList=(RecyclerView)findViewById(R.id.recyclerview_search);
        hotCityTip=(LinearLayout)findViewById(R.id.hotcity_tip);


        GridLayoutManager layoutManager=new GridLayoutManager(SearchActivity.this,4);
        searchList.setLayoutManager(layoutManager);
        SearchAdapter mSearchAdapter = new SearchAdapter(hotCities);
        searchList.setAdapter(mSearchAdapter);


        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "search键");

                    searchCities(textView.getText().toString());

                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });


    }

    public void searchCities(String cityName){
        String cityUrl="https://search.heweather.com/find?location="+cityName+"&key=e9b90a4dff4e4ae5b974aa29b3466cfe";
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseCity=response.body().string();
                City city= Utility.handleCityResponse(responseCity);

                hotCities.clear();
                if (city != null&&city.basicList!=null) {
                    for(int i=0;i<city.basicList.size();i++){
                        Basic basic=city.basicList.get(i);
                        hotCities.add(basic.location);

                        Log.d(TAG, "城市ID"+basic.cid);
                        Log.d(TAG, "城市名称"+basic.location);
                        Log.d(TAG, "所在省份"+basic.admin_area);
                        Log.d(TAG, "大小"+city.basicList.size());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        hotCityTip.setVisibility(View.GONE);
                        if(hotCities!=null){
                            GridLayoutManager layoutManager=new GridLayoutManager(SearchActivity.this,1);
                            searchList.setLayoutManager(layoutManager);
                            SearchListAdapter mSearchAdapter = new SearchListAdapter(hotCities);
                            searchList.setAdapter(mSearchAdapter);
                            searchList.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
                        }

                    }
                });

            }
            @Override
            public void onFailure(Call call, IOException e) {

            }

        });
    }

}
