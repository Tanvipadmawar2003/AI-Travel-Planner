package com.example.ai_travel_planner.model;

public class PlacesRequest {

    public String textQuery;
    public int maxResultCount;

    public PlacesRequest(String textQuery, int maxResultCount) {
        this.textQuery = textQuery;
        this.maxResultCount = maxResultCount;
    }
}