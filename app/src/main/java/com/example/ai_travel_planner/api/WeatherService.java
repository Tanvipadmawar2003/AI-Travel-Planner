package com.example.ai_travel_planner.api;

import com.example.ai_travel_planner.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<WeatherResponse> getWeather(

            @Query("q") String city,

            @Query("appid") String apiKey,

            @Query("units") String units

    );

}