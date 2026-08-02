package com.example.ai_travel_planner.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String expenseName;
    public String category;
    public double amount;
    public String destination;

    public Expense(String expenseName,
                   String category,
                   double amount,
                   String destination) {

        this.expenseName = expenseName;
        this.category = category;
        this.amount = amount;
        this.destination = destination;
    }
}