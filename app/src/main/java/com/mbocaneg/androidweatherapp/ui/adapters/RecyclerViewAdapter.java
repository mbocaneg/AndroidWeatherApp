package com.mbocaneg.androidweatherapp.ui.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mbocaneg.androidweatherapp.R;
import com.mbocaneg.androidweatherapp.model.WeatherModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom RecyclerView adapter that populates a RecyclerView with 5 day forecast data.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private List<WeatherModel> weatherList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, List<WeatherModel> weatherList) {
        this.weatherList = weatherList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_five_day, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder called");
        WeatherModel w = weatherList.get(position);

        Picasso.get().load("http://openweathermap.org/img/w/"+w.getIcon()+".png").into(holder.weatherImage);

        holder.dayText.setText(w.getDay());
        holder.tempText.setText(Integer.toString(w.getTemperature())+(char) 0x00B0);
        holder.tempMinText.setText(Integer.toString(w.getTempMin())+(char) 0x00B0);
        holder.tempMaxText.setText(Integer.toString(w.getTempMax())+(char) 0x00B0);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView weatherImage;
        TextView dayText;
        TextView tempText;
        TextView tempMinText;
        TextView tempMaxText;

        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherImage = itemView.findViewById(R.id.image_weather);
            dayText = itemView.findViewById(R.id.tv_day);
            tempText = itemView.findViewById(R.id.tv_temp);
            tempMinText = itemView.findViewById(R.id.tv_temp_min);
            tempMaxText = itemView.findViewById(R.id.tv_temp_max);

            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }


}
