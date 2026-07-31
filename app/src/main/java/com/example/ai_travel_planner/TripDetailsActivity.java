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
    Button btnMap;
    TextView tvTemperature;
    TextView tvWeather;
    TextView tvHumidity;
    TextView tvWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvSummary = findViewById(R.id.tvSummary);
        tvAITrip = findViewById(R.id.tvAITrip);
        btnMap = findViewById(R.id.btnMap);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeather = findViewById(R.id.tvWeather);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);

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
        tvTemperature.setText("🌡 Temperature : " + temperature);
        tvWeather.setText("☁ Weather : " + weather);
        tvHumidity.setText("💧 Humidity : " + humidity);
        tvWind.setText("🌬 Wind : " + wind);

        // Show AI Itinerary
        if (aiTrip != null && !aiTrip.isEmpty()) {
            tvAITrip.setText(aiTrip);
        } else {
            tvAITrip.setText(
                    "Day 1\nVisit local attractions\n\n" +
                            "Day 2\nExplore museums\n\n" +
                            "Day 3\nShopping and return."
            );
        }

        // Google Maps Button
        btnMap.setOnClickListener(v -> {

            Toast.makeText(
                    TripDetailsActivity.this,
                    "Opening Google Maps...",
                    Toast.LENGTH_SHORT
            ).show();

            Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(destination));

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {

                Uri webUri = Uri.parse(
                        "https://www.google.com/maps/search/?api=1&query="
                                + Uri.encode(destination));

                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, webUri);

                startActivity(browserIntent);
            }
        });
    }
}