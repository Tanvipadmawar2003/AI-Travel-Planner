package com.example.ai_travel_planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText etDestination, etBudget, etTravelers;
    Button btnPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDestination = findViewById(R.id.etDestination);
        etBudget = findViewById(R.id.etBudget);
        etTravelers = findViewById(R.id.etTravelers);
        btnPlan = findViewById(R.id.btnPlan);

        btnPlan.setOnClickListener(view -> {

            String destination = etDestination.getText().toString().trim();
            String budget = etBudget.getText().toString().trim();
            String travelers = etTravelers.getText().toString().trim();

            if(destination.isEmpty()){
                etDestination.setError("Enter Destination");
                return;
            }

            if(budget.isEmpty()){
                etBudget.setError("Enter Budget");
                return;
            }

            if(travelers.isEmpty()){
                etTravelers.setError("Enter Number of Travelers");
                return;
            }

            Toast.makeText(MainActivity.this,
                    "Planning Trip to " + destination,
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, TripDetailsActivity.class);

            intent.putExtra("destination", destination);
            intent.putExtra("budget", budget);
            intent.putExtra("travelers", travelers);

            startActivity(intent);
        });

    }
}