package com.example.ai_travel_planner;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_travel_planner.database.Trip;
import com.example.ai_travel_planner.database.TripDatabase;

public class EditTripActivity extends AppCompatActivity {

    EditText etDestination;
    EditText etStartDate;
    EditText etEndDate;
    EditText etTravelers;
    EditText etBudget;

    Spinner spTravelMode;
    Spinner spHotel;

    Button btnUpdateTrip;

    TripDatabase database;

    int tripId;

    String[] travelModes = {
            "Flight",
            "Train",
            "Bus",
            "Car"
    };

    String[] hotelRatings = {
            "3 Star",
            "4 Star",
            "5 Star"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        database = TripDatabase.getInstance(this);

        etDestination = findViewById(R.id.etDestination);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etTravelers = findViewById(R.id.etTravelers);
        etBudget = findViewById(R.id.etBudget);

        spTravelMode = findViewById(R.id.spTravelMode);
        spHotel = findViewById(R.id.spHotel);

        btnUpdateTrip = findViewById(R.id.btnUpdateTrip);

        ArrayAdapter<String> modeAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        travelModes
                );

        spTravelMode.setAdapter(modeAdapter);

        ArrayAdapter<String> hotelAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        hotelRatings
                );

        spHotel.setAdapter(hotelAdapter);

        tripId = getIntent().getIntExtra("id",0);

        String destination =
                getIntent().getStringExtra("destination");

        String startDate =
                getIntent().getStringExtra("startDate");

        String endDate =
                getIntent().getStringExtra("endDate");

        String travelers =
                getIntent().getStringExtra("travelers");

        String budget =
                getIntent().getStringExtra("budget");

        String travelMode =
                getIntent().getStringExtra("travelMode");

        String hotel =
                getIntent().getStringExtra("hotel");

        String aiTrip =
                getIntent().getStringExtra("aiTrip");

        etDestination.setText(destination);
        etStartDate.setText(startDate);
        etEndDate.setText(endDate);
        etTravelers.setText(travelers);
        etBudget.setText(budget);

        for(int i=0;i<travelModes.length;i++){

            if(travelModes[i].equals(travelMode)){

                spTravelMode.setSelection(i);
                break;

            }

        }

        for(int i=0;i<hotelRatings.length;i++){

            if(hotelRatings[i].equals(hotel)){

                spHotel.setSelection(i);
                break;

            }

        }

        btnUpdateTrip.setOnClickListener(v -> {

            String newDestination =
                    etDestination.getText().toString().trim();

            String newStartDate =
                    etStartDate.getText().toString().trim();

            String newEndDate =
                    etEndDate.getText().toString().trim();

            String newTravelers =
                    etTravelers.getText().toString().trim();

            String newBudget =
                    etBudget.getText().toString().trim();

            String newTravelMode =
                    spTravelMode.getSelectedItem().toString();

            String newHotel =
                    spHotel.getSelectedItem().toString();

            if(newDestination.isEmpty()){

                etDestination.setError("Enter Destination");
                return;

            }

            Trip trip = new Trip(
                    newDestination,
                    newStartDate,
                    newEndDate,
                    newTravelers,
                    newBudget,
                    newTravelMode,
                    newHotel,
                    aiTrip
            );

            trip.setId(tripId);

            database.tripDao().updateTrip(trip);

            Toast.makeText(
                    this,
                    "Trip Updated Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        });

    }

}