package com.example.ai_travel_planner.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trips")
public class Trip {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String destination;
    private String startDate;
    private String endDate;
    private String travelers;
    private String budget;
    private String travelMode;
    private String hotel;
    private String aiTrip;

    public Trip(String destination,
                String startDate,
                String endDate,
                String travelers,
                String budget,
                String travelMode,
                String hotel,
                String aiTrip) {

        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelers = travelers;
        this.budget = budget;
        this.travelMode = travelMode;
        this.hotel = hotel;
        this.aiTrip = aiTrip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTravelers() {
        return travelers;
    }

    public String getBudget() {
        return budget;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public String getHotel() {
        return hotel;
    }

    public String getAiTrip() {
        return aiTrip;
    }
}