package com.example.ai_travel_planner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_travel_planner.api.ApiClient;
import com.example.ai_travel_planner.api.GroqService;
import com.example.ai_travel_planner.model.GroqRequest;
import com.example.ai_travel_planner.model.GroqResponse;

import java.util.Calendar;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = BuildConfig.GROQ_API_KEY;
    EditText etDestination, etStartDate, etEndDate, etTravelers, etBudget;
    RadioGroup rgTravelMode;
    Spinner spHotel;
    Button btnGenerate;

    String aiTrip = "AI itinerary will be shown here.";
    private Calendar startCalendar = Calendar.getInstance();

    String[] hotelRating = {"3 Star", "4 Star", "5 Star"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDestination = findViewById(R.id.etDestination);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etTravelers = findViewById(R.id.etTravelers);
        etBudget = findViewById(R.id.etBudget);

        rgTravelMode = findViewById(R.id.rgTravelMode);
        spHotel = findViewById(R.id.spHotel);
        btnGenerate = findViewById(R.id.btnGenerate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                hotelRating
        );

        spHotel.setAdapter(adapter);

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));

        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnGenerate.setOnClickListener(v -> {

            String destination = etDestination.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String travelers = etTravelers.getText().toString().trim();
            String budget = etBudget.getText().toString().trim();


            if (destination.isEmpty()) {
                etDestination.setError("Enter Destination");
                return;
            }
            if (startDate.isEmpty()) {
                etStartDate.setError("Enter start date");
                return;
            }
            if (endDate.isEmpty()) {
                etEndDate.setError("Enter End Date");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            try {

                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);

                if (end.before(start)) {

                    etEndDate.setError("End Date must be after Start Date");
                    Toast.makeText(MainActivity.this,
                            "End Date must be after Start Date",
                            Toast.LENGTH_LONG).show();

                    return;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (travelers.isEmpty()) {
                etTravelers.setError("Enter Number of Travelers");
                return;
            }

            if (budget.isEmpty()) {
                etBudget.setError("Enter Budget");
                return;
            }

            int selectedId = rgTravelMode.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this,
                        "Select Travel Mode",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rb = findViewById(selectedId);

            String travelMode = rb.getText().toString();

            String hotel = spHotel.getSelectedItem().toString();

            Toast.makeText(this,
                    "Generating AI Trip...",
                    Toast.LENGTH_SHORT).show();
            // Create AI Prompt
            // Create AI Prompt
            String prompt =
                    "Create a travel itinerary for " + destination +
                            " from " + startDate +
                            " to " + endDate +
                            ". Number of travelers: " + travelers +
                            ". Budget: ₹" + budget +
                            ". Travel mode: " + travelMode +
                            ". Hotel Rating: " + hotel +
                            ". Give a detailed day-wise travel plan.";

            List<GroqRequest.Message> messages = new ArrayList<>();

            messages.add(new GroqRequest.Message(
                    "user",
                    prompt
            ));

            GroqRequest request = new GroqRequest(
                    "llama-3.3-70b-versatile",
                    messages
            );

            GroqService service =
                    ApiClient.getClient().create(GroqService.class);

            service.generateTrip(
                    "Bearer " + API_KEY,
                    request
            ).enqueue(new Callback<GroqResponse>() {

                @Override
                public void onResponse(Call<GroqResponse> call,
                                       Response<GroqResponse> response) {

                    if (response.isSuccessful()
                            && response.body() != null
                            && response.body().choices != null
                            && !response.body().choices.isEmpty()) {

                        String aiTrip =
                                response.body()
                                        .choices
                                        .get(0)
                                        .message
                                        .content;

                        Intent intent = new Intent(MainActivity.this,
                                TripDetailsActivity.class);

                        intent.putExtra("destination", destination);
                        intent.putExtra("startDate", startDate);
                        intent.putExtra("endDate", endDate);
                        intent.putExtra("travelers", travelers);
                        intent.putExtra("budget", budget);
                        intent.putExtra("travelMode", travelMode);
                        intent.putExtra("hotel", hotel);
                        intent.putExtra("aiTrip", aiTrip);

                        startActivity(intent);

                    } else {

                        try {
                            String error = response.errorBody().string();

                            android.util.Log.e("GROQ_ERROR", error);

                            Toast.makeText(
                                    MainActivity.this,
                                    error,
                                    Toast.LENGTH_LONG
                            ).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GroqResponse> call,
                                      Throwable t) {

                    Toast.makeText(
                            MainActivity.this,
                            t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }); // enqueue()

        }); // btnGenerate.setOnClickListener()

    } // onCreate()

    private void showDatePicker(EditText editText) {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, y, m, d) ->
                        editText.setText(d + "/" + (m + 1) + "/" + y),
                year,
                month,
                day
        );

        dialog.show();
    }

} // MainActivity



