package com.example.ai_travel_planner.model;

public class Place {

    public DisplayName displayName;
    public String formattedAddress;
    public Double rating;
    public String googleMapsUri;

    public static class DisplayName {
        public String text;
        public String languageCode;
    }
}