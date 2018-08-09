package com.example.joey.windweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.joey.windweather.Weather.Forecast;
import java.util.List;

public class WeatherInfoAdapter extends RecyclerView.Adapter{
    private static final String TAG = "WeatherInfoAdapter";
    private final int TYPE_FIRST=0;
    private final int TYPE_OTHERS=1;

    private Context mContext;
    private List<Forecast> forecasts;
    private String[] forecastTitleList={"日出时间","日落时间","风向","风速","相对湿度","降水量","降水概率","大气压强","紫外线强度指数","能见度"};

     public class ViewHolder1 extends RecyclerView.ViewHolder {
         ImageView forecastIcon;
         View line;
         TextView forecastDate,forecastTmp;
         ViewHolder1(View itemView) {
            super(itemView);
             forecastDate=(TextView)itemView.findViewById(R.id.forecast_date);
             forecastTmp=(TextView)itemView.findViewById(R.id.forecast_tmp);
             forecastIcon=(ImageView)itemView.findViewById(R.id.forecast_icon);
             line=(View) itemView.findViewById(R.id.line);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView forecastTitle,forecastContent;
         ViewHolder2(View itemView) {
            super(itemView);
            forecastTitle=(TextView)itemView.findViewById(R.id.forecast_title);
            forecastContent=(TextView)itemView.findViewById(R.id.forecast_content);
        }
    }

    WeatherInfoAdapter(Context context,List<Forecast>forecastList){
        forecasts=forecastList;
        mContext=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       if(viewType==TYPE_FIRST){
           View view= LayoutInflater.from(mContext).inflate(R.layout.weather_forecast_item,parent,false);
           return new ViewHolder1(view);
        }else{
           View view= LayoutInflater.from(mContext).inflate(R.layout.weatherinfo_item,parent,false);
           return new ViewHolder2(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

         if(position<=1){
             ViewHolder1 viewHolder1=(ViewHolder1)holder;
             viewHolder1.forecastDate.setText(forecasts.get(position+1).date);
             switch (forecasts.get(position+1).cond_txt_d){
                 case "晴":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.sunnyicon);
                     break;
                 case "多云":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.cloudy);
                     break;
                 case "少云":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.fewclouds);
                     break;
                 case "晴间多云":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.partlycloudy);
                     break;
                 case "阴":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.overcast);
                     break;
                 case "阵雨":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.showerrain);
                     break;
                 case "雷阵雨":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.thundershower);
                     break;
                 case "小雨":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.lightrain);
                     break;
                 case "中雨":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.moderaterain);
                     break;
                 case "大雨":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.heavyrain);
                     break;
                 case "暴雨":
                     viewHolder1.forecastIcon.setImageResource(R.drawable.storm);
                     break;
             }


             viewHolder1.forecastTmp.setText(forecasts.get(position+1).tmp_max+"      "+forecasts.get(position+1).tmp_min);
             if(position==0){
                 viewHolder1.line.setVisibility(View.GONE);
             }else {
                 viewHolder1.line.setVisibility(View.VISIBLE);
             }
         }else {
             ViewHolder2 viewHolder2=(ViewHolder2)holder;
             viewHolder2.forecastTitle.setText(forecastTitleList[position-2]);
             switch (position){
                 case 2:
                     viewHolder2.forecastContent.setText(forecasts.get(0).sr);
                     break;
                 case 3:
                     viewHolder2.forecastContent.setText(forecasts.get(0).ss);
                     break;
                 case 4:
                     viewHolder2.forecastContent.setText(forecasts.get(0).wind_dir);
                     break;
                 case 5:
                     viewHolder2.forecastContent.setText(forecasts.get(0).wind_spd);
                     break;
                 case 6:
                     viewHolder2.forecastContent.setText(forecasts.get(0).hum);
                     break;
                 case 7:
                     viewHolder2.forecastContent.setText(forecasts.get(0).pcpn);
                     break;
                 case 8:
                     viewHolder2.forecastContent.setText(forecasts.get(0).pop);
                     break;
                 case 9:
                     viewHolder2.forecastContent.setText(forecasts.get(0).pres);
                     break;
                 case 10:
                     viewHolder2.forecastContent.setText(forecasts.get(0).uv_index);
                     break;
                 case 11:
                     viewHolder2.forecastContent.setText(forecasts.get(0).vis);
                     break;
             }
        }
    }

    @Override
    public int getItemCount() {
        return forecastTitleList.length+2;
    }

    @Override
    public int getItemViewType(int position) {
  //       return TYPE_FIRST;
        if(position<=1){
            return TYPE_FIRST;
        }
        else return TYPE_OTHERS;
    }

}
