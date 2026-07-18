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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText etDestination, etStartDate, etEndDate, etTravelers, etBudget;
    RadioGroup rgTravelMode;
    Spinner spHotel;
    Button btnGenerate;

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

            int selectedId = rgTravelMode.getCheckedRadioButtonId();

            if(selectedId == -1){
                Toast.makeText(this,"Select Travel Mode",Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rb = findViewById(selectedId);

            String travelMode = rb.getText().toString();

            Toast.makeText(this,
                    "Generating AI Trip...\nMode : " + travelMode,
                    Toast.LENGTH_LONG).show();

        });

    }

    private void showDatePicker(EditText editText){

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, y, m, d) ->
                        editText.setText(d + "/" + (m+1) + "/" + y),
                year,month,day);

        dialog.show();
    }
}