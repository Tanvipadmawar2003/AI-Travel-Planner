package com.example.ai_travel_planner.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesClient {

    private static final String BASE_URL =
            "https://places.googleapis.com/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }

        return retrofit;
    }
}