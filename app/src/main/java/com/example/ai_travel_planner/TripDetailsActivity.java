package com.example.ai_travel_planner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TripDetailsActivity extends AppCompatActivity {

    TextView tvSummary, tvAITrip;
    TextView tvTemperature, tvWeather, tvHumidity, tvWind;

    Button btnMap;
    Button btnAttractions, btnRestaurants, btnHotels, btnShopping;
    Button btnExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        // Find views
        tvSummary = findViewById(R.id.tvSummary);
        tvAITrip = findViewById(R.id.tvAITrip);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeather = findViewById(R.id.tvWeather);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);

        btnMap = findViewById(R.id.btnMap);

        btnAttractions = findViewById(R.id.btnAttractions);
        btnRestaurants = findViewById(R.id.btnRestaurants);
        btnHotels = findViewById(R.id.btnHotels);
        btnShopping = findViewById(R.id.btnShopping);
        btnExpense = findViewById(R.id.btnExpense);

        // Receive data from MainActivity
        String destination = getIntent().getStringExtra("destination");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        String travelers = getIntent().getStringExtra("travelers");
        String budget = getIntent().getStringExtra("budget");
        String travelMode = getIntent().getStringExtra("travelMode");
        String hotel = getIntent().getStringExtra("hotel");
        String aiTrip = getIntent().getStringExtra("aiTrip");

        String temperature = getIntent().getStringExtra("temperature");
        String weather = getIntent().getStringExtra("weather");
        String humidity = getIntent().getStringExtra("humidity");
        String wind = getIntent().getStringExtra("wind");

        // Trip Summary
        String summary =
                "📍 Destination : " + destination +
                        "\n\n📅 Start Date : " + startDate +
                        "\n\n📅 End Date : " + endDate +
                        "\n\n👥 Travelers : " + travelers +
                        "\n\n💰 Budget : ₹" + budget +
                        "\n\n✈ Travel Mode : " + travelMode +
                        "\n\n⭐ Hotel Rating : " + hotel;

        tvSummary.setText(summary);

        // Weather information
        tvTemperature.setText(
                "🌡 Temperature : " + getSafeValue(temperature)
        );

        tvWeather.setText(
                "☁ Weather : " + getSafeValue(weather)
        );

        tvHumidity.setText(
                "💧 Humidity : " + getSafeValue(humidity)
        );

        tvWind.setText(
                "🌬 Wind : " + getSafeValue(wind)
        );

        // AI Itinerary
        if (aiTrip != null && !aiTrip.trim().isEmpty()) {
            tvAITrip.setText(aiTrip);
        } else {
            tvAITrip.setText(
                    "Day 1\nVisit local attractions\n\n" +
                            "Day 2\nExplore museums\n\n" +
                            "Day 3\nShopping and return."
            );
        }

        // Main Google Maps button
        btnMap.setOnClickListener(v -> {

            Toast.makeText(
                    TripDetailsActivity.this,
                    "Opening Google Maps...",
                    Toast.LENGTH_SHORT
            ).show();

            Uri uri = Uri.parse(
                    "geo:0,0?q=" + Uri.encode(destination)
            );

            Intent mapIntent = new Intent(
                    Intent.ACTION_VIEW,
                    uri
            );

            mapIntent.setPackage(
                    "com.google.android.apps.maps"
            );

            if (mapIntent.resolveActivity(getPackageManager()) != null) {

                startActivity(mapIntent);

            } else {

                Uri webUri = Uri.parse(
                        "https://www.google.com/maps/search/?api=1&query="
                                + Uri.encode(destination)
                );

                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        webUri
                );

                startActivity(browserIntent);
            }
        });

        // Tourist attractions
        btnAttractions.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TripDetailsActivity.this,
                    NearbyPlacesActivity.class
            );

            intent.putExtra("destination", destination);

            startActivity(intent);
        });

        // Restaurants
        btnRestaurants.setOnClickListener(v -> {
            openGoogleMapsSearch(
                    destination + " restaurants"
            );
        });

        // Hotels
        btnHotels.setOnClickListener(v -> {
            openGoogleMapsSearch(
                    destination + " hotels"
            );
        });

        // Shopping
        btnShopping.setOnClickListener(v -> {
            openGoogleMapsSearch(
                    destination + " shopping"
            );
        });
        btnExpense.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TripDetailsActivity.this,
                    ExpenseActivity.class
            );

            // Pass trip information
            intent.putExtra("budget", budget);
            intent.putExtra("destination", destination);

            startActivity(intent);

        });
        /**
         * Opens a Google Maps search for the given query.
         */
    }

    private void openGoogleMapsSearch(String query) {

        Uri webUri = Uri.parse(
                "https://www.google.com/maps/search/?api=1&query="
                        + Uri.encode(query)
        );

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                webUri
        );

        try {

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    TripDetailsActivity.this,
                    "Unable to open Google Maps",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * Prevents "null" from appearing on the screen.
     */
    private String getSafeValue(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "Unavailable";
        }

        return value;
    }
}
