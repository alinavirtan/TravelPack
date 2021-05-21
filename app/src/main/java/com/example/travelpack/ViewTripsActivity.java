package com.example.travelpack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewTripsActivity extends AppCompatActivity {
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);

        LinearLayout layout = (LinearLayout) findViewById(R.id.trips_list_layout);
        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
//        DB = new DBHelper(this);
//
//        ArrayList<Integer> trips = new ArrayList<>();
//        EditText email = findViewById(R.id.inputEmail);

//        if (DB.getTripsUser(email.getText().toString()) == null) {
//            Button button = new Button(getApplicationContext());
//            button.setText("Prima calatorie");
//            button.setTypeface(button.getTypeface(), Typeface.BOLD);
//            button.setTextSize(18);
//            button.setTextColor(Color.WHITE);
//            button.setBackgroundColor(Color.BLUE);
//            layout.addView(button);
//        }

    }
}
