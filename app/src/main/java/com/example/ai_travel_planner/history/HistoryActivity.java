package com.example.ai_travel_planner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.adapter.TripAdapter;
import com.example.ai_travel_planner.database.Trip;
import com.example.ai_travel_planner.database.TripDatabase;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        recyclerView=findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        TripDatabase database=
                TripDatabase.getInstance(this);

        List<Trip> trips=
                database.tripDao().getAllTrips();

        TripAdapter adapter =
                new TripAdapter(trips, new TripAdapter.OnTripClickListener() {

                    @Override
                    public void onTripClick(Trip trip) {

                        Intent intent =
                                new Intent(HistoryActivity.this,
                                        TripDetailsActivity.class);

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

                        recreate();

                    }

                });
        recyclerView.setAdapter(adapter);

    }
}