package com.example.ai_travel_planner.model;

import java.util.List;

public class GroqRequest {

    public String model;
    public List<Message> messages;

    public GroqRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public static class Message {

        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}