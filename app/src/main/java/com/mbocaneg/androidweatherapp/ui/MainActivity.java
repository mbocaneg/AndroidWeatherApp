package com.mbocaneg.androidweatherapp.ui;

import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mbocaneg.androidweatherapp.R;
import com.mbocaneg.androidweatherapp.contracts.WeatherMVP;
import com.mbocaneg.androidweatherapp.model.WeatherModel;
import com.mbocaneg.androidweatherapp.presenter.WeatherPresenterImpl;
import com.mbocaneg.androidweatherapp.ui.adapters.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeatherMVP.WeatherView {

    WeatherPresenterImpl presenter;

    TextView tvCity;
    TextView tvToday;
    TextView tvTodayTemperature;
    TextView tvTodayDescription;

    ImageView ivIcon;

    Button fetchData;

    RecyclerView recyclerView;

    List<WeatherModel> weatherList;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        //Initialize Presenter
        presenter = new WeatherPresenterImpl(this);

        //Initialize UI elements
        tvCity = (TextView)findViewById(R.id.tv_city);
        tvTodayTemperature = (TextView)findViewById(R.id.tv_today_temperature);
        tvTodayDescription = (TextView)findViewById(R.id.tv_today_description);
        tvToday = (TextView)findViewById(R.id.tv_today);
        ivIcon = (ImageView)findViewById(R.id.iv_icon);

        fetchData = (Button)findViewById(R.id.btnRefeshData);
        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleRefreshButton();
            }
        });

        //Initialize RecyclerView
        weatherList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_fiveday);
        adapter = new RecyclerViewAdapter(this, weatherList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

    }

    /**
     *  Function that takes a WeatherModel object containing current Weather conditions
     *  and displays it in the header part of the app.
     */
    @Override
    public void displayCurrentWeather(WeatherModel model) {
        tvCity.setText(model.getCity());
        tvToday.setText(model.getDay());
        tvTodayDescription.setText(model.getDescription() + "\nHumidity: "+model.getHumidity()+"%" );
        tvTodayTemperature.setText(Integer.toString(model.getTemperature()) + "\u00B0");
        Picasso.get().load("http://openweathermap.org/img/w/"+model.getIcon()+".png").into(ivIcon);

    }

    /**
     *  Function that takes a List of WeatherModel objects containing forecast data
     *  for the next 5 days, and displays it in a RecyclerView
     */
    @Override
    public void displayFiveDayWeather(List<WeatherModel> wList){
        weatherList.clear();
        weatherList.addAll(wList);
        adapter.notifyDataSetChanged();
    }

    /**
     *  Function for displaying Toast messages to the user
     */
    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
