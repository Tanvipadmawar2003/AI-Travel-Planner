package com.example.ai_travel_planner;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

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
    Spinner spFilterCategory;
    Button btnExportPdf;

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
        spFilterCategory =
                findViewById(R.id.spFilterCategory);
        btnExportPdf = findViewById(R.id.btnExportPdf);



        recyclerExpense.setLayoutManager(
                new LinearLayoutManager(this)
        );
        String[] categories = {
                "All",
                "Food",
                "Hotel",
                "Transport",
                "Shopping",
                "Ticket",
                "Other"
        };
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories
                );

        spFilterCategory.setAdapter(adapter);
        loadExpenseData();
        spFilterCategory.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        List<Expense> expenseList;

                        if(categories[position].equals("All")){

                            expenseList =
                                    TripDatabase.getInstance(
                                                    ExpenseHistoryActivity.this)
                                            .expenseDao()
                                            .getAllExpenses();

                        }else{

                            expenseList =
                                    TripDatabase.getInstance(
                                                    ExpenseHistoryActivity.this)
                                            .expenseDao()
                                            .getExpenseByCategory(
                                                    categories[position]);

                        }

                        ExpenseAdapter adapter =
                                new ExpenseAdapter(
                                        expenseList,
                                        ExpenseHistoryActivity.this::updateBudgetSummary
                                );

                        recyclerExpense.setAdapter(adapter);

                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {

                    }

                });

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
        btnExportPdf.setOnClickListener(v -> {
            exportPdf();
        });

        btnAnalytics.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ExpenseHistoryActivity.this,
                    ExpenseAnalyticsActivity.class
            );

            startActivity(intent);

        });
    }
    private void exportPdf(){
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(
                        595,
                        842,
                        1
                ).create();

        PdfDocument.Page page =
                pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        paint.setTextSize(24);

        canvas.drawText(
                "AI Travel Planner",
                180,
                60,
                paint
        );

        paint.setTextSize(18);

        canvas.drawText(
                "Expense Report",
                220,
                100,
                paint
        );
        paint.setTextSize(16);

        canvas.drawText(
                tvTotalExpense.getText().toString(),
                40,
                150,
                paint
        );

        canvas.drawText(
                tvRemainingBudget.getText().toString(),
                40,
                180,
                paint
        );

        canvas.drawText(
                tvProgress.getText().toString(),
                40,
                210,
                paint
        );
        List<Expense> list =
                TripDatabase.getInstance(this)
                        .expenseDao()
                        .getAllExpenses();

        int y = 270;

        for(Expense e : list){

            canvas.drawText(
                    e.expenseName,
                    40,
                    y,
                    paint
            );

            canvas.drawText(
                    e.category,
                    250,
                    y,
                    paint
            );

            canvas.drawText(
                    "₹"+e.amount,
                    430,
                    y,
                    paint
            );

            y += 30;

        }
        pdfDocument.finishPage(page);
        File file = new File(
                getExternalFilesDir(null),
                "ExpenseReport.pdf"
        );

        try{

            FileOutputStream out =
                    new FileOutputStream(file);

            pdfDocument.writeTo(out);

            out.close();

            pdfDocument.close();

            Toast.makeText(
                    this,
                    "PDF Saved:\n"+file.getAbsolutePath(),
                    Toast.LENGTH_LONG
            ).show();

        }catch(Exception e){

            e.printStackTrace();

        }

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