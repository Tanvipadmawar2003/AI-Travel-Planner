package com.example.ai_travel_planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.adapter.PlaceAdapter;
import com.example.ai_travel_planner.api.PlacesClient;
import com.example.ai_travel_planner.api.PlacesService;
import com.example.ai_travel_planner.model.Place;
import com.example.ai_travel_planner.model.PlacesRequest;
import com.example.ai_travel_planner.model.PlacesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyPlacesActivity extends AppCompatActivity {

    private RecyclerView recyclerPlaces;
    private TextView tvPlacesTitle;

    private PlaceAdapter adapter;
    private final List<Place> placeList = new ArrayList<>();

    private static final String PLACES_API_KEY =
            BuildConfig.PLACES_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearby_places);

        // Find views
        recyclerPlaces = findViewById(R.id.recyclerPlaces);
        tvPlacesTitle = findViewById(R.id.tvPlacesTitle);

        // RecyclerView setup
        recyclerPlaces.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new PlaceAdapter(placeList);
        recyclerPlaces.setAdapter(adapter);

        // Get destination
        String destination =
                getIntent().getStringExtra("destination");

        android.util.Log.d(
                "PLACES_KEY",
                "Key starts with: " +
                        (PLACES_API_KEY != null
                                ? PLACES_API_KEY.substring(
                                0,
                                Math.min(8, PLACES_API_KEY.length()))
                                : "NULL")
        );

        // Set title
        tvPlacesTitle.setText(
                "📍 Places near " + destination
        );

        // Search attractions
        searchPlaces(
                destination + " tourist attractions"
        );
    }

    private void searchPlaces(String query) {

        PlacesRequest request =
                new PlacesRequest(
                        query,
                        10
                );

        PlacesService service =
                PlacesClient.getClient()
                        .create(PlacesService.class);

        String fieldMask =
                "places.displayName,"
                        + "places.formattedAddress,"
                        + "places.rating,"
                        + "places.googleMapsUri";

        service.searchPlaces(
                PLACES_API_KEY,
                fieldMask,
                request
        ).enqueue(new Callback<PlacesResponse>() {

            @Override
            public void onResponse(
                    Call<PlacesResponse> call,
                    Response<PlacesResponse> response
            ) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().places != null) {

                    placeList.clear();

                    placeList.addAll(
                            response.body().places
                    );

                    adapter.notifyDataSetChanged();

                    if (placeList.isEmpty()) {

                        Toast.makeText(
                                NearbyPlacesActivity.this,
                                "No places found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {

                    String errorMessage =
                            "Places API Error: "
                                    + response.code();

                    try {

                        if (response.errorBody() != null) {

                            errorMessage =
                                    response.errorBody().string();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    android.util.Log.e(
                            "PLACES_ERROR",
                            errorMessage
                    );

                    Toast.makeText(
                            NearbyPlacesActivity.this,
                            errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<PlacesResponse> call,
                    Throwable t
            ) {

                android.util.Log.e(
                        "PLACES_CONNECTION",
                        "Connection error",
                        t
                );

                Toast.makeText(
                        NearbyPlacesActivity.this,
                        "Connection error: "
                                + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}