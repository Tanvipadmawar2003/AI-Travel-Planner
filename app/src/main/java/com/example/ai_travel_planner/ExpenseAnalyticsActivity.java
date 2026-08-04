package com.example.ai_travel_planner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_travel_planner.database.ExpenseDao;
import com.example.ai_travel_planner.database.TripDatabase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ExpenseAnalyticsActivity
        extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expense_analytics);

        pieChart = findViewById(R.id.pieChart);

        ExpenseDao dao =
                TripDatabase.getInstance(this).expenseDao();

        ArrayList<PieEntry> entries =
                new ArrayList<>();

        entries.add(new PieEntry(
                (float) dao.getFoodExpense(),
                "Food"));

        entries.add(new PieEntry(
                (float) dao.getHotelExpense(),
                "Hotel"));

        entries.add(new PieEntry(
                (float) dao.getTransportExpense(),
                "Transport"));

        entries.add(new PieEntry(
                (float) dao.getShoppingExpense(),
                "Shopping"));

        entries.add(new PieEntry(
                (float) dao.getTicketExpense(),
                "Ticket"));

        entries.add(new PieEntry(
                (float) dao.getOtherExpense(),
                "Other"));

        PieDataSet dataSet =
                new PieDataSet(entries, "Expenses");

        PieData data =
                new PieData(dataSet);

        pieChart.setData(data);

        pieChart.setCenterText("Expense Report");

        pieChart.animateY(1500);

        pieChart.invalidate();

    }
}