package com.example.ai_travel_planner;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_travel_planner.database.Expense;
import com.example.ai_travel_planner.database.TripDatabase;

public class EditExpenseActivity extends AppCompatActivity {

    EditText etExpenseName, etAmount;
    Spinner spCategory;
    Button btnUpdate;

    TripDatabase database;

    int expenseId;
    String destination;

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
        setContentView(R.layout.activity_edit_expense);

        database = TripDatabase.getInstance(this);

        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);
        spCategory = findViewById(R.id.spCategory);
        btnUpdate = findViewById(R.id.btnUpdate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );

        spCategory.setAdapter(adapter);

        // Get data from Intent
        expenseId = getIntent().getIntExtra("id", 0);

        String name = getIntent().getStringExtra("name");
        String category = getIntent().getStringExtra("category");
        double amount = getIntent().getDoubleExtra("amount", 0);

        destination = getIntent().getStringExtra("destination");

        if (destination == null) {
            destination = "Current Trip";
        }

        etExpenseName.setText(name);
        etAmount.setText(String.valueOf(amount));

        for (int i = 0; i < categories.length; i++) {

            if (categories[i].equals(category)) {

                spCategory.setSelection(i);
                break;

            }

        }

        btnUpdate.setOnClickListener(v -> {

            String updatedName = etExpenseName.getText().toString().trim();

            String updatedCategory = spCategory.getSelectedItem().toString();

            String amountText = etAmount.getText().toString().trim();

            if (updatedName.isEmpty()) {
                etExpenseName.setError("Enter Expense Name");
                return;
            }

            if (amountText.isEmpty()) {
                etAmount.setError("Enter Amount");
                return;
            }

            double updatedAmount = Double.parseDouble(amountText);

            Expense expense = new Expense(
                    updatedName,
                    updatedCategory,
                    updatedAmount,
                    destination
            );

            expense.id = expenseId;

            database.expenseDao().updateExpense(expense);

            Toast.makeText(
                    EditExpenseActivity.this,
                    "Expense Updated Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        });

    }
}