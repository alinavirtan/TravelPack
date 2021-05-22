package com.example.travelpack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTripActivity extends AppCompatActivity {
    EditText editText;   // oras
    EditText date;  // data plecare
    DatePickerDialog.OnDateSetListener setListener;
    SeekBar seekBar;  // numar de zile
    TextView nrOfDays;
    CheckedTextView business;   // tip calatorie
    CheckedTextView leisure;   // tip calatorie
    LatLng latLng;
    static String destination;
    static String date_trip;
    static String type;
    static int days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        (MainActivity.activities).clear();
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
                (MainActivity.activities).clear();
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
                                String date_string = dayOfMonth + "/" + month + "/" + year;
                                date.setText(date_string);
                                date_trip = date_string;
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
                days = progress;
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

        // pentru WeatherAPI

        Button addTripBtn = findViewById(R.id.btnAddTrip);
        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!business.isChecked() && !leisure.isChecked()) {
                    Toast.makeText(AddTripActivity.this, "Select trip type", Toast.LENGTH_SHORT).show();
                    return;
                }
                    // iau parametrii de care am nevoie
//                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month + 1);
//                String startDate = year + "-" + formattedMonth + "-" + day;
//                //String cityName = editText.getText().toString().trim();
//                String daysNum = nrOfDays.getText().toString().trim();
//
//                WeatherData weatherRequest = new WeatherData(latLng, startDate, daysNum, getApplicationContext());
//                weatherRequest.ComputeRequest();

                if (business.isChecked()) {
                    type = "business";
                    Intent intent = new Intent(AddTripActivity.this, SelectBusinessActivities.class);
                    startActivity(intent);
                }

                if (leisure.isChecked()) {
                    type = "leisure";
                    Intent intent = new Intent(AddTripActivity.this, SelectLeisureActivities.class);
                    startActivity(intent);
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
            destination = editText.getText().toString();

            // pt weatherapi -> vreau sa iau coordonatele
            Log.d("Address: ", editText.getText().toString());
            latLng = getLatLngFromAddress(editText.getText().toString());
            if (latLng != null) {
                Log.d("Latitude, Longitude: ", "" + latLng.latitude + " " + latLng.longitude);
            } else {
                Log.d("Null coordinates!", "");
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(AddTripActivity.this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null) {
                Address singleAddress = addressList.get(0);
                LatLng latLng = new LatLng(singleAddress.getLatitude(), singleAddress.getLongitude());
                return latLng;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
