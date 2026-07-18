package com.example.ai_travel_planner;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TripDetailsActivity extends AppCompatActivity {

    TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvSummary = findViewById(R.id.tvSummary);

        String destination = getIntent().getStringExtra("destination");
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
                        hotel +

                        "\n\n🎉 Have a Safe Journey!";

        tvSummary.setText(summary);

    }
}