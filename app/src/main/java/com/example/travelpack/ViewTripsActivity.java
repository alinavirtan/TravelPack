package com.example.travelpack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewTripsActivity extends AppCompatActivity {
    DBHelper_Trips DB;
    static int TripNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);

        LinearLayout layout = (LinearLayout) findViewById(R.id.trips_list_layout);
        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        DB = new DBHelper_Trips(this);

        Cursor cursor = DB.getTripsUser(LoginActivity.email_trip);

        if (cursor.getCount() > 0 ) {
            if (cursor.moveToFirst()) {
                do {
                    Button button = new Button(getApplicationContext());
                    String text = cursor.getString(cursor.getColumnIndex("destination")) +
                            " - " + cursor.getString(cursor.getColumnIndex("date"));
                    button.setText(text);
                    button.setTypeface(button.getTypeface(), Typeface.BOLD);
                    button.setTextSize(18);
                    button.setTranslationY(-750);
                    button.setTextColor(Color.WHITE);
                    button.setBackgroundColor(0xFF6200EE);

                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(ViewTripsActivity.this, ViewItemsActivity.class);
                            startActivity(intent);
                        }
                    });

                    layout.addView(button);
                } while (cursor.moveToNext());
            }
        }
    }
}
