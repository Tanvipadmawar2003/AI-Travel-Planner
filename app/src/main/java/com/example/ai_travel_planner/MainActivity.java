package com.example.ai_travel_planner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_travel_planner.api.ApiClient;
import com.example.ai_travel_planner.api.GroqService;
import com.example.ai_travel_planner.api.WeatherClient;
import com.example.ai_travel_planner.api.WeatherService;
import com.example.ai_travel_planner.database.Trip;
import com.example.ai_travel_planner.database.TripDatabase;
import com.example.ai_travel_planner.model.GroqRequest;
import com.example.ai_travel_planner.model.GroqResponse;
import com.example.ai_travel_planner.model.WeatherResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.GROQ_API_KEY;
    private static final String WEATHER_API_KEY =BuildConfig.WEATHER_API_KEY;

    private EditText etDestination;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etTravelers;
    private EditText etBudget;

    private RadioGroup rgTravelMode;
    private Spinner spHotel;

    private Button btnGenerate;
    private Button btnHistory;

    private TripDatabase database;

    private final String[] hotelRating = {"3 Star", "4 Star", "5 Star"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = TripDatabase.getInstance(this);

        etDestination = findViewById(R.id.etDestination);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etTravelers = findViewById(R.id.etTravelers);
        etBudget = findViewById(R.id.etBudget);

        rgTravelMode = findViewById(R.id.rgTravelMode);
        spHotel = findViewById(R.id.spHotel);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnHistory = findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        com.example.ai_travel_planner.HistoryActivity.class
                );

                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                hotelRating
        );
        spHotel.setAdapter(adapter);

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnGenerate.setOnClickListener(v -> generateTrip());
    }

    private void generateTrip() {
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

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
        );
        sdf.setLenient(false);

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (start == null || end == null) {
                Toast.makeText(
                        this,
                        "Invalid date",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (end.before(start)) {
                etEndDate.setError("End Date must be after Start Date");
                Toast.makeText(
                        this,
                        "End Date must be after Start Date",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(
                    this,
                    "Please select valid dates",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (travelers.isEmpty()) {
            etTravelers.setError("Enter Number of Travelers");
            return;
        }

        if (budget.isEmpty()) {
            etBudget.setError("Enter your travel budget");
            return;
        }

        int travelerCount;
        int budgetAmount;

        try {
            travelerCount = Integer.parseInt(travelers);
            budgetAmount = Integer.parseInt(budget);
        } catch (NumberFormatException e) {
            etTravelers.setError("Enter a valid number");
            etBudget.setError("Enter a valid budget");
            return;
        }

        if (travelerCount <= 0) {
            etTravelers.setError("Travelers must be at least 1");
            return;
        }

        int minimumBudget = travelerCount * 1000;

        if (budgetAmount < minimumBudget) {
            etBudget.setError("Budget is too low");
            Toast.makeText(
                    this,
                    "Minimum budget for " + travelerCount
                            + " traveler(s) is ₹" + minimumBudget,
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        int selectedId = rgTravelMode.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(
                    this,
                    "Select Travel Mode",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        RadioButton rb = findViewById(selectedId);
        String travelMode = rb.getText().toString();

        String hotel = spHotel.getSelectedItem().toString();

        Toast.makeText(
                this,
                "Getting weather and generating AI trip...",
                Toast.LENGTH_SHORT
        ).show();

        String prompt =
                "Create a travel itinerary for " + destination
                        + " from " + startDate
                        + " to " + endDate
                        + ". Number of travelers: " + travelers
                        + ". Budget: ₹" + budget
                        + ". Travel mode: " + travelMode
                        + ". Hotel Rating: " + hotel
                        + ". Give a detailed day-wise travel plan.";

        List<GroqRequest.Message> messages = new ArrayList<>();
        messages.add(new GroqRequest.Message("user", prompt));

        GroqRequest request = new GroqRequest(
                "llama-3.3-70b-versatile",
                messages
        );

        fetchWeatherThenGenerateTrip(
                destination,
                startDate,
                endDate,
                travelers,
                budget,
                travelMode,
                hotel,
                request
        );
    }

    private void fetchWeatherThenGenerateTrip(
            String destination,
            String startDate,
            String endDate,
            String travelers,
            String budget,
            String travelMode,
            String hotel,
            GroqRequest request
    ) {
        WeatherService weatherService =
                WeatherClient.getClient().create(WeatherService.class);

        weatherService.getWeather(
                destination,
                WEATHER_API_KEY,
                "metric"
        ).enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(
                    Call<WeatherResponse> call,
                    Response<WeatherResponse> weatherResponse
            ) {
                String temperature = "Unavailable";
                String weather = "Unavailable";
                String humidity = "Unavailable";
                String wind = "Unavailable";

                if (weatherResponse.isSuccessful()
                        && weatherResponse.body() != null) {

                    WeatherResponse body = weatherResponse.body();

                    if (body.main != null) {
                        temperature = body.main.temp + "°C";
                        humidity = body.main.humidity + "%";
                    }

                    if (body.weather != null
                            && !body.weather.isEmpty()
                            && body.weather.get(0) != null) {
                        weather = body.weather.get(0).description;
                    }

                    if (body.wind != null) {
                        wind = body.wind.speed + " m/s";
                    }
                }

                generateGroqTrip(
                        destination,
                        startDate,
                        endDate,
                        travelers,
                        budget,
                        travelMode,
                        hotel,
                        request,
                        temperature,
                        weather,
                        humidity,
                        wind
                );
            }

            @Override
            public void onFailure(
                    Call<WeatherResponse> call,
                    Throwable t
            ) {
                // Weather should not prevent trip generation.
                generateGroqTrip(
                        destination,
                        startDate,
                        endDate,
                        travelers,
                        budget,
                        travelMode,
                        hotel,
                        request,
                        "Unavailable",
                        "Unavailable",
                        "Unavailable",
                        "Unavailable"
                );
            }
        });
    }

    private void generateGroqTrip(
            String destination,
            String startDate,
            String endDate,
            String travelers,
            String budget,
            String travelMode,
            String hotel,
            GroqRequest request,
            String temperature,
            String weather,
            String humidity,
            String wind
    ) {
        GroqService service =
                ApiClient.getClient().create(GroqService.class);

        service.generateTrip(
                "Bearer " + API_KEY,
                request
        ).enqueue(new Callback<GroqResponse>() {

            @Override
            public void onResponse(
                    Call<GroqResponse> call,
                    Response<GroqResponse> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().choices != null
                        && !response.body().choices.isEmpty()
                        && response.body().choices.get(0) != null
                        && response.body().choices.get(0).message != null) {

                    String aiTrip =
                            response.body()
                                    .choices
                                    .get(0)
                                    .message
                                    .content;

                    Trip trip = new Trip(
                            destination,
                            startDate,
                            endDate,
                            travelers,
                            budget,
                            travelMode,
                            hotel,
                            aiTrip
                    );

                    database.tripDao().insertTrip(trip);

                    Intent intent = new Intent(
                            MainActivity.this,
                            TripDetailsActivity.class
                    );

                    intent.putExtra("destination", destination);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    intent.putExtra("travelers", travelers);
                    intent.putExtra("budget", budget);
                    intent.putExtra("travelMode", travelMode);
                    intent.putExtra("hotel", hotel);
                    intent.putExtra("aiTrip", aiTrip);

                    intent.putExtra("temperature", temperature);
                    intent.putExtra("weather", weather);
                    intent.putExtra("humidity", humidity);
                    intent.putExtra("wind", wind);

                    startActivity(intent);

                } else {
                    String error = "Groq API error: " + response.code();

                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception ignored) {
                    }

                    android.util.Log.e("GROQ_ERROR", error);

                    Toast.makeText(
                            MainActivity.this,
                            error,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<GroqResponse> call,
                    Throwable t
            ) {
                Toast.makeText(
                        MainActivity.this,
                        "Groq connection error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                MainActivity.this,
                (view, y, m, d) ->
                        editText.setText(
                                d + "/" + (m + 1) + "/" + y
                        ),
                year,
                month,
                day
        );

        dialog.show();
    }
}
