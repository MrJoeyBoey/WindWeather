package com.example.joey.windweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private static final String TAG = "SearchAdapter";

    private List<String> mCityList;
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cityItem;
        MyViewHolder(View itemView) {
            super(itemView);
            cityItem=(TextView)itemView.findViewById(R.id.city_item);
        }
    }

    SearchAdapter(List<String> cities){
        this.mCityList=cities;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.city_item,parent,false);
        final MyViewHolder viewHolder =new MyViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=viewHolder.getAdapterPosition();
                Intent intent=new Intent(mContext,MainActivity.class);
                intent.putExtra("defaut",true);
                mContext.startActivity(intent);
                Activity activity=(Activity)mContext;
                activity.overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);

                SharedPreferences pref=mContext.getSharedPreferences("LatestCity", MODE_PRIVATE);
                String name1=pref.getString("city1","");
                String name2=pref.getString("city2","");
                if(name1.isEmpty()){
                    SharedPreferences.Editor editor=mContext.getSharedPreferences("LatestCity", MODE_PRIVATE).edit();
                    editor.putString("city1",viewHolder.cityItem.getText().toString());
                    editor.apply();
                }else if(name2.isEmpty()){
                    SharedPreferences.Editor editor=mContext.getSharedPreferences("LatestCity", MODE_PRIVATE).edit();
                    editor.putString("city2",viewHolder.cityItem.getText().toString());
                    editor.apply();
                }else {
                    SharedPreferences.Editor editor=mContext.getSharedPreferences("LatestCity", MODE_PRIVATE).edit();
                    editor.putString("city2",viewHolder.cityItem.getText().toString());
                    editor.putString("city1",name2);
                    editor.apply();
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        String city=mCityList.get(position);
        holder.cityItem.setText(city);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

}
