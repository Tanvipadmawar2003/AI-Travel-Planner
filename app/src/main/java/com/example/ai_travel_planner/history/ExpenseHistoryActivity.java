package com.example.ai_travel_planner;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.adapter.ExpenseAdapter;
import com.example.ai_travel_planner.database.Expense;
import com.example.ai_travel_planner.database.TripDatabase;

import java.util.List;

public class ExpenseHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerExpense;
    TextView tvTotalExpense;
    TextView tvRemainingBudget;
    TextView tvProgress;
    ProgressBar progressBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        recyclerExpense = findViewById(R.id.recyclerExpense);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget);
        tvProgress = findViewById(R.id.tvProgress);
        progressBudget = findViewById(R.id.progressBudget);

        recyclerExpense.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadExpenseData();
    }

    private void loadExpenseData() {

        List<Expense> expenseList =
                TripDatabase.getInstance(this)
                        .expenseDao()
                        .getAllExpenses();

        ExpenseAdapter adapter =
                new ExpenseAdapter(
                        expenseList,
                        this::updateBudgetSummary
                );

        recyclerExpense.setAdapter(adapter);

        updateBudgetSummary();
    }

    private void updateBudgetSummary() {

        double totalExpense =
                TripDatabase.getInstance(this)
                        .expenseDao()
                        .getTotalExpense();

        if (Double.isNaN(totalExpense)) {
            totalExpense = 0;
        }

        String budgetString =
                getIntent().getStringExtra("budget");

        double totalBudget;

        if (budgetString == null || budgetString.isEmpty()) {
            totalBudget = 15000;
        } else {
            totalBudget = Double.parseDouble(budgetString);
        }

        double remaining =
                totalBudget - totalExpense;

        tvTotalExpense.setText(
                "💰 Total Expense : ₹" + totalExpense
        );

        tvRemainingBudget.setText(
                "💵 Remaining Budget : ₹" + remaining
        );

        int percentage =
                (int) ((totalExpense / totalBudget) * 100);

        if (percentage > 100) {
            percentage = 100;
        }

        progressBudget.setProgress(percentage);

        tvProgress.setText(
                percentage + "% Budget Used"
        );
    }
}