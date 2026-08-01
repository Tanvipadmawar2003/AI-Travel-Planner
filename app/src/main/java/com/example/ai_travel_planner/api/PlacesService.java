package com.example.ai_travel_planner.api;

import com.example.ai_travel_planner.model.PlacesRequest;
import com.example.ai_travel_planner.model.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PlacesService {

    @Headers("Content-Type: application/json")
    @POST("v1/places:searchText")
    Call<PlacesResponse> searchPlaces(
            @Header("X-Goog-Api-Key") String apiKey,
            @Header("X-Goog-FieldMask") String fieldMask,
            @Body PlacesRequest request
    );
}