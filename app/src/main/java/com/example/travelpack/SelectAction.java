package com.example.travelpack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectAction extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        Button btn_add_trip = (Button)findViewById(R.id.btn_add_trip);
        Button btn_view_trips = (Button)findViewById(R.id.btn_view_trips);

        btn_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAction.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        btn_view_trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAction.this, ViewTripsActivity.class);
                startActivity(intent);
            }
        });
    }
}
