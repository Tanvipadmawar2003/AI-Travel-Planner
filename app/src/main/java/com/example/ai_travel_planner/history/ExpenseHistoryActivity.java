package com.example.ai_travel_planner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.adapter.ExpenseAdapter;
import com.example.ai_travel_planner.database.Expense;
import com.example.ai_travel_planner.database.TripDatabase;

import java.util.List;
import android.text.TextWatcher;

public class ExpenseHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerExpense;
    TextView tvTotalExpense;
    TextView tvRemainingBudget;
    TextView tvProgress;
    ProgressBar progressBudget;
    TextView tvBudgetStatus;
    TextView tvBudget;
    TextView tvFood;
    TextView tvHotel;
    TextView tvTransport;
    TextView tvShopping;
    TextView tvTicket;
    TextView tvOther;
    Button btnAnalytics;
    EditText etSearchExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        recyclerExpense = findViewById(R.id.recyclerExpense);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget);
        tvProgress = findViewById(R.id.tvProgress);
        progressBudget = findViewById(R.id.progressBudget);
        tvBudget = findViewById(R.id.tvBudget);
        tvBudgetStatus = findViewById(R.id.tvBudgetStatus);
        tvFood = findViewById(R.id.tvFood);
        tvHotel = findViewById(R.id.tvHotel);
        tvTransport = findViewById(R.id.tvTransport);
        tvShopping = findViewById(R.id.tvShopping);
        tvTicket = findViewById(R.id.tvTicket);
        tvOther = findViewById(R.id.tvOther);
        tvOther = findViewById(R.id.tvOther);
        btnAnalytics =
                findViewById(R.id.btnAnalytics);
        etSearchExpense = findViewById(R.id.etSearchExpense);

        recyclerExpense.setLayoutManager(
                new LinearLayoutManager(this)
        );


        loadExpenseData();
        etSearchExpense.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                List<Expense> list =
                        TripDatabase.getInstance(
                                        ExpenseHistoryActivity.this)
                                .expenseDao()
                                .searchExpense(
                                        s.toString()
                                );

                ExpenseAdapter adapter =
                        new ExpenseAdapter(
                                list,
                                ExpenseHistoryActivity.this::updateBudgetSummary
                        );

                recyclerExpense.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(
                    Editable s) {

            }

        });
        btnAnalytics.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ExpenseHistoryActivity.this,
                    ExpenseAnalyticsActivity.class
            );

            startActivity(intent);

        });
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
        updateCategorySummary();

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
        if (remaining > totalBudget * 0.50) {

            tvBudgetStatus.setText("🟢 Excellent Budget");

        }
        else if (remaining > totalBudget * 0.20) {

            tvBudgetStatus.setText("🟡 Good Budget");

        }
        else if (remaining > 0) {

            tvBudgetStatus.setText("🟠 Low Budget");

        }
        else {

            tvBudgetStatus.setText("🔴 Over Budget");

        }

        tvProgress.setText(
                percentage + "% Budget Used"
        );
    }
    private void updateCategorySummary() {

        TripDatabase db = TripDatabase.getInstance(this);

        tvFood.setText(
                "🍔 Food : ₹" +
                        db.expenseDao().getFoodExpense());

        tvHotel.setText(
                "🏨 Hotel : ₹" +
                        db.expenseDao().getHotelExpense());

        tvTransport.setText(
                "🚕 Transport : ₹" +
                        db.expenseDao().getTransportExpense());

        tvShopping.setText(
                "🛍 Shopping : ₹" +
                        db.expenseDao().getShoppingExpense());

        tvTicket.setText(
                "🎫 Ticket : ₹" +
                        db.expenseDao().getTicketExpense());

        tvOther.setText(
                "📦 Other : ₹" +
                        db.expenseDao().getOtherExpense());

    }
}