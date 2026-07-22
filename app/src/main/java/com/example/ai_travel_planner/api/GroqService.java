package com.example.ai_travel_planner.api;

import com.example.ai_travel_planner.model.GroqRequest;
import com.example.ai_travel_planner.model.GroqResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Header;

public interface GroqService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    Call<GroqResponse> generateTrip(
            @Header("Authorization") String token,
            @Body GroqRequest request
    );
}