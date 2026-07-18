package com.example.ai_travel_planner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TripDetailsActivity extends AppCompatActivity {

    TextView tvSummary;
    Button btnMap;

    String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvSummary = findViewById(R.id.tvSummary);
        btnMap = findViewById(R.id.btnMap);

        destination = getIntent().getStringExtra("destination");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        String travelers = getIntent().getStringExtra("travelers");
        String budget = getIntent().getStringExtra("budget");
        String travelMode = getIntent().getStringExtra("travelMode");
        String hotel = getIntent().getStringExtra("hotel");

        String summary =
                "🌍 AI Travel Planner\n\n" +

                        "📍 Destination\n" +
                        destination +

                        "\n\n📅 Start Date\n" +
                        startDate +

                        "\n\n📅 End Date\n" +
                        endDate +

                        "\n\n👥 Travelers\n" +
                        travelers +

                        "\n\n💰 Budget\n₹" +
                        budget +

                        "\n\n✈ Travel Mode\n" +
                        travelMode +

                        "\n\n⭐ Hotel Rating\n" +
                        hotel;

        tvSummary.setText(summary);

        btnMap.setOnClickListener(v -> {

            Toast.makeText(this,
                    "Opening Google Maps...",
                    Toast.LENGTH_SHORT).show();

            Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(destination));

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) != null) {

                startActivity(intent);

            } else {

                Uri webUri = Uri.parse(
                        "https://www.google.com/maps/search/?api=1&query="
                                + Uri.encode(destination));

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);

                startActivity(browserIntent);
            }

        });

    }
}