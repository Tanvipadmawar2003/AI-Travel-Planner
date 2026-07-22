package com.example.ai_travel_planner.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDao {

    @Insert
    void insertTrip(Trip trip);

    @Delete
    void deleteTrip(Trip trip);

    @Query("SELECT * FROM trips ORDER BY id DESC")
    List<Trip> getAllTrips();
}