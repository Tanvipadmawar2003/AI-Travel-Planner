package com.example.ai_travel_planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_travel_planner.database.Expense;
import com.example.ai_travel_planner.database.TripDatabase;

public class ExpenseActivity extends AppCompatActivity {

    EditText etExpenseName, etAmount;
    Spinner spCategory;
    Button btnSaveExpense, btnViewExpenses;

    TripDatabase database;

    String[] categories = {
            "Food",
            "Hotel",
            "Transport",
            "Shopping",
            "Ticket",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        database = TripDatabase.getInstance(this);

        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);

        spCategory = findViewById(R.id.spCategory);

        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        btnViewExpenses = findViewById(R.id.btnViewExpenses);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );

        spCategory.setAdapter(adapter);

        // Save Expense
        btnSaveExpense.setOnClickListener(v -> {

            String name = etExpenseName.getText().toString().trim();

            if (name.isEmpty()) {
                etExpenseName.setError("Enter Expense Name");
                return;
            }

            String amountText = etAmount.getText().toString().trim();

            if (amountText.isEmpty()) {
                etAmount.setError("Enter Amount");
                return;
            }

            double amount = Double.parseDouble(amountText);

            String category = spCategory.getSelectedItem().toString();

            Expense expense = new Expense(
                    name,
                    category,
                    amount,
                    "Current Trip"
            );

            database.expenseDao().insertExpense(expense);

            Toast.makeText(
                    ExpenseActivity.this,
                    "Expense Saved Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            etExpenseName.setText("");
            etAmount.setText("");

        });

        // View Expense History
        btnViewExpenses.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ExpenseActivity.this,
                    com.example.ai_travel_planner.ExpenseHistoryActivity.class
            );

            intent.putExtra(
                    "budget",
                    getIntent().getStringExtra("budget")
            );

            startActivity(intent);

        });

    }
}