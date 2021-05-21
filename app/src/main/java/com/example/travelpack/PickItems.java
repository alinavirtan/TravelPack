package com.example.travelpack;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PickItems extends AppCompatActivity {
    ArrayList<String> selected_items;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void AddCheckbox(LinearLayout layout, String item) {
        CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setText(item);
        checkBox.setTextSize(16);
        checkBox.setTranslationY(-300);
        checkBox.setElegantTextHeight(true);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selected_items.contains(item)) {
                        selected_items.add(item);
                    }
                } else {
                    if (selected_items.contains(item)) {
                        selected_items.remove(item);
                    }
                }
            }
        });
        layout.addView(checkBox);
    }

    void ReadAsset(String activity, ArrayList<String> luggage, String previousActivity) {
        BufferedReader reader = null;

        String filename = "";
        if (previousActivity.equals("SelectLeisureActivities")) {
            filename = "proposed_leisure_luggage/" + activity + ".txt";
        } else if (previousActivity.equals("SelectBusinessActivities")) {
            filename = "proposed_business_luggage/" + activity + ".txt";
        }

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(filename), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                luggage.add(mLine);
            }
        } catch (IOException e) {
            //log the exception
            Toast.makeText(PickItems.this, "Exception in opening asset", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    Toast.makeText(PickItems.this, "Exception closing BufferReader", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_items);

        LinearLayout layout = (LinearLayout) findViewById(R.id.item_list_layout);
        selected_items = new ArrayList<>();
        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");

        DBHelper_Trips DB = new DBHelper_Trips(this);

        CheckedTextView business = findViewById(R.id.business);
        CheckedTextView leisure = findViewById(R.id.leisure);
        EditText email = findViewById(R.id.inputEmail);
        EditText editText = findViewById(R.id.inputDestination);
        EditText date = findViewById(R.id.inputDate1);
        SeekBar seekBar = findViewById(R.id.seekBar);
        Integer TripNo ;


        for (int i = 0; i < (MainActivity.activities).size(); i++) {
            String activity = (MainActivity.activities).get(i);

            TextView textView = new TextView(getApplicationContext());
            textView.setText(activity);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLUE);
            textView.setTranslationY(-300);
            textView.setElegantTextHeight(true);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            layout.addView(textView);

            ArrayList<String> luggage = new ArrayList<>();
            ReadAsset(activity, luggage, previousActivity);

            for (int j = 0; j < luggage.size(); j++) {
                AddCheckbox(layout, luggage.get(j));
            }

            TextView blankSpace = new TextView(getApplicationContext());
            blankSpace.setText("");
            layout.addView(blankSpace);
        }

        Button button = new Button(getApplicationContext());
        button.setText("Create package list");
        button.setTypeface(button.getTypeface(), Typeface.BOLD);
        button.setTextSize(18);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.BLUE);
        layout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selected_items.size(); i++) {
                    // adaug in baza de date
                    // ...
                }

                Intent intent = new Intent(PickItems.this, SelectAction.class);
                startActivity(intent);
            }
        });
    }
}
