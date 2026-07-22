package com.example.ai_travel_planner.model;

import java.util.List;

public class GroqResponse {

    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String content;
    }
}