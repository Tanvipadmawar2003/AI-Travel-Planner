package com.example.ai_travel_planner;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TripDetailsActivity extends AppCompatActivity {

    TextView tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvDetails = findViewById(R.id.tvDetails);

        String destination = getIntent().getStringExtra("destination");
        String budget = getIntent().getStringExtra("budget");
        String travelers = getIntent().getStringExtra("travelers");

        tvDetails.setText(
                "Destination : " + destination +
                        "\n\nBudget : " + budget +
                        "\n\nTravelers : " + travelers
        );
    }
}