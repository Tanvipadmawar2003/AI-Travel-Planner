package com.example.ai_travel_planner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.adapter.TripAdapter;
import com.example.ai_travel_planner.database.Trip;
import com.example.ai_travel_planner.database.TripDatabase;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    TripDatabase database;
    EditText etSearchTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        etSearchTrip = findViewById(R.id.etSearchTrip);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = TripDatabase.getInstance(this);

        loadTrips();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh trip list after editing a trip
        loadTrips();
    }

    private void loadTrips() {
        etSearchTrip.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                List<Trip> trips =
                        database.tripDao().searchTrip(
                                s.toString()
                        );

                TripAdapter adapter =
                        new TripAdapter(
                                trips,
                                new TripAdapter.OnTripClickListener() {

                                    @Override
                                    public void onTripClick(Trip trip) {

                                        Intent intent =
                                                new Intent(
                                                        HistoryActivity.this,
                                                        TripDetailsActivity.class
                                                );

                                        intent.putExtra("destination", trip.getDestination());
                                        intent.putExtra("startDate", trip.getStartDate());
                                        intent.putExtra("endDate", trip.getEndDate());
                                        intent.putExtra("travelers", trip.getTravelers());
                                        intent.putExtra("budget", trip.getBudget());
                                        intent.putExtra("travelMode", trip.getTravelMode());
                                        intent.putExtra("hotel", trip.getHotel());
                                        intent.putExtra("aiTrip", trip.getAiTrip());

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onTripLongClick(Trip trip) {

                                    }
                                });

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        List<Trip> trips =
                database.tripDao().getAllTrips();

        TripAdapter adapter =
                new TripAdapter(
                        trips,
                        new TripAdapter.OnTripClickListener() {

                            @Override
                            public void onTripClick(Trip trip) {

                                Intent intent =
                                        new Intent(
                                                HistoryActivity.this,
                                                TripDetailsActivity.class
                                        );

                                intent.putExtra("destination", trip.getDestination());
                                intent.putExtra("startDate", trip.getStartDate());
                                intent.putExtra("endDate", trip.getEndDate());
                                intent.putExtra("travelers", trip.getTravelers());
                                intent.putExtra("budget", trip.getBudget());
                                intent.putExtra("travelMode", trip.getTravelMode());
                                intent.putExtra("hotel", trip.getHotel());
                                intent.putExtra("aiTrip", trip.getAiTrip());

                                startActivity(intent);
                            }

                            @Override
                            public void onTripLongClick(Trip trip) {

                                database.tripDao().deleteTrip(trip);

                                loadTrips();

                            }
                        });

        recyclerView.setAdapter(adapter);

    }

}