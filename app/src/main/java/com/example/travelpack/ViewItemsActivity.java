package com.example.travelpack;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;

class Item {
    Integer id_item;
    String name;
    Integer is_picked;

    Item(Integer id_item, String name, Integer is_picked) {
        this.id_item = id_item;
        this.name = name;
        this.is_picked = is_picked;
    }

    @Override
    public int hashCode() {
        return this.id_item;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof Item)
        {
            isEqual = (this.id_item == ((Item) object).id_item) && (this.name.equals(((Item) object).name));
        }

        return isEqual;
    }

    public String toString() {
        return id_item + " - " + name + "(" + is_picked + ")";
    }
}
public class ViewItemsActivity extends AppCompatActivity implements OnDataLoaded {
    ArrayList<Item> picked_items;
    String destination;
    String date;
    String description;
    int temperature;
    int nrDays;
    LatLng latLng;
    LinearLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void AddCheckbox(LinearLayout layout, Item item, int is_picked) {
        CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setText(item.name);
        checkBox.setTextSize(16);
        checkBox.setTranslationY(-350);
        checkBox.setElegantTextHeight(true);

        /* the checkbox is shown initially as checked if the item has already been picked */
        if (is_picked == 1) {
            checkBox.setChecked(true);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!picked_items.contains(item)) {
                        picked_items.add(item);
                    }
                } else {
                    if (picked_items.contains(item)) {
                        picked_items.remove(item);
                    }
                }
            }
        });
        layout.addView(checkBox);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        layout = (LinearLayout) findViewById(R.id.items_trips_list_layout);
        picked_items = new ArrayList<>();
        Intent mIntent = getIntent();

        Integer tripNo = ViewTripsActivity.tripNo;

        DBHelper_Trips DB_trip = new DBHelper_Trips(this);
        Cursor trip = DB_trip.getTrip(tripNo);

        if (trip.getCount() > 0) {
            trip.moveToFirst();

            description = "NULL";
            temperature = -100;

            destination = trip.getString(trip.getColumnIndex("destination"));
            date = trip.getString(trip.getColumnIndex("date"));
            nrDays = trip.getInt(trip.getColumnIndex("no_days"));

            latLng = getLatLngFromAddress(destination);
            if (latLng != null) {
                Log.d("Latitude, Longitude: ", "" + latLng.latitude + " " + latLng.longitude);
            } else {
                Log.d("Null coordinates!", "");
            }

            @SuppressLint("DefaultLocale")
            String startDate = date;
            String daysNum = Integer.toString(nrDays);

            WeatherData weatherRequest = new WeatherData(latLng, startDate, daysNum, getApplicationContext());
            weatherRequest.ComputeRequest( ViewItemsActivity.this::onDataLoaded);

        }

        Log.d("temparaturaaaaaa: ", Double.toString(temperature));

        DBHelper_Items DB_Items = new DBHelper_Items(this);

        Cursor cursor = DB_Items.getItemsTrip(tripNo);

        ArrayList<Item> allItems = new ArrayList<>();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    Integer id_item = cursor.getInt(cursor.getColumnIndex("id_item"));
                    int is_picked = cursor.getInt(cursor.getColumnIndex("is_picked"));

                    if (is_picked == 1 && !picked_items.contains(new Item(id_item, name, is_picked))) {
                        picked_items.add(new Item(id_item, name, is_picked));
                    }
                    AddCheckbox(layout, new Item(id_item, name, 1), is_picked);
                    //luggage.put(id_item, name);
                    allItems.add(new Item(id_item, name, is_picked));
                } while (cursor.moveToNext());
            }
        }

        Button button = new Button(getApplicationContext());
        button.setText("Save package list");
        button.setTypeface(button.getTypeface(), Typeface.BOLD);
        button.setTextSize(18);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(0xFF6200EE);
       // button.setTranslationY(500);
        layout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < allItems.size(); i++) {
                    Item current_item = allItems.get(i);
                    Integer id_item = current_item.id_item;
                    String name = current_item.name;
                    Integer is_picked = current_item.is_picked;

                    if (is_picked == 0 && picked_items.contains(current_item)) {
                        /* the item has been selected but in the original db it isn't */
                        DB_Items.updateById(id_item, name, 1);
                    } else if (is_picked == 1 && !picked_items.contains(current_item)) {
                        /* the item has been deselected but the db hasn't been updated */
                        DB_Items.updateById(id_item, name, 0);
                    }
                }

                Intent intent = new Intent(ViewItemsActivity.this, ViewTripsActivity.class);
                startActivity(intent);
            }
        });
    }

    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(ViewItemsActivity.this);
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

    @Override
    public void onDataLoaded(ArrayList<ForecastInfo> result) {
        if (!result.isEmpty()) {
            description = result.get(0).description;
            Log.d("Descrierea vremii: ", description);
            temperature = (int)result.get(0).temp;
            Log.d("Temperatura: ", Double.toString(temperature));

            ImageView weather = new ImageView(getApplicationContext());

            if (description.equals("clear sky")) {
                weather.setImageResource(R.drawable.ic_clear);
            } else if (description.equals("few clouds") || description.equals("scattered clouds")) {
                weather.setImageResource(R.drawable.ic_few);
            } else if (description.equals("broken clouds") || description.equals("overcast clouds")) {
                weather.setImageResource(R.drawable.ic_overcast);
            } else if (description.contains("thunder")) {
                weather.setImageResource(R.drawable.ic_thunderstorm);
            } else if (description.contains("drizzle")) {
                weather.setImageResource(R.drawable.ic_drizzle);
            } else if (description.contains("rain")) {
                weather.setImageResource(R.drawable.ic_rain);
            } else if (description.contains("snow")) {
                weather.setImageResource(R.drawable.ic_snow);
            } else {
                weather.setImageResource(R.drawable.ic_mist);
            }

            weather.setLayoutParams(new FrameLayout.LayoutParams(500, 270));
            weather.setTranslationY(-1650);
            layout.addView(weather);

            ImageView temperature_img = new ImageView(getApplicationContext());
            temperature_img.setImageResource(R.drawable.ic_temp);
            temperature_img.setLayoutParams(new FrameLayout.LayoutParams(1700, 270));
            temperature_img.setTranslationY(-1920);
            layout.addView(temperature_img);

            TextView text = new TextView(getApplicationContext());
            text.setText("            " + description + "                      " + temperature + " C");
            //text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 170));
            text.setTextColor(0xFF0099FF);
            text.setTextSize(16);

            text.setTranslationY(-1920);
            layout.addView(text);

        }
    }
}
