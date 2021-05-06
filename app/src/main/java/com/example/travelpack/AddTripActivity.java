package com.example.travelpack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTripActivity extends AppCompatActivity {
    EditText editText;
    EditText date;
    DatePickerDialog.OnDateSetListener setListener;
    SeekBar seekBar;
    TextView nrOfDays;
    CheckedTextView business;
    CheckedTextView leisure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        editText = findViewById(R.id.inputDestination);
        date = findViewById(R.id.inputDate1);
        seekBar = findViewById(R.id.seekBar);
        nrOfDays = findViewById(R.id.nrDays);
        business = findViewById(R.id.business);
        leisure = findViewById(R.id.leisure);

        business.setCheckMarkDrawable(R.drawable.ic_circle);
        leisure.setCheckMarkDrawable(R.drawable.ic_circle);

        business.setChecked(false);
        leisure.setChecked(false);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String apiKey = getString(R.string.apiKey);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID,
                        Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(AddTripActivity.this);

                startActivityForResult(intent, 100);
            }
        });

        date.setFocusable(false);
        date.setKeyListener(null);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this,
                         new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String date_string = day + "/" + month + "/" + year;
                                date.setText(date_string);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = progress * (seekBar.getWidth() -  2 * seekBar.getThumbOffset()) / seekBar.getMax();
                nrOfDays.setText(progress + "");
                nrOfDays.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (business.isChecked()) {
                    business.setCheckMarkDrawable(R.drawable.ic_circle);
                    business.setChecked(false);
                } else {
                    business.setCheckMarkDrawable(R.drawable.ic_check);
                    business.setChecked(true);
                    leisure.setChecked(false);
                    leisure.setCheckMarkDrawable(R.drawable.ic_circle);
                }
            }
        });

        leisure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leisure.isChecked()) {
                    leisure.setCheckMarkDrawable(R.drawable.ic_circle);
                    leisure.setChecked(false);
                } else {
                    leisure.setCheckMarkDrawable(R.drawable.ic_check);
                    leisure.setChecked(true);
                    business.setChecked(false);
                    business.setCheckMarkDrawable(R.drawable.ic_circle);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            editText.setText(place.getName());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
