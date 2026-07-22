package com.example.ai_travel_planner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class}, version = 1, exportSchema = false)
public abstract class TripDatabase extends RoomDatabase {

    private static TripDatabase instance;

    public abstract TripDao tripDao();

    public static synchronized TripDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TripDatabase.class,
                            "trip_database"
                    )
                    .allowMainThreadQueries()
                    .build();

        }

        return instance;
    }
}