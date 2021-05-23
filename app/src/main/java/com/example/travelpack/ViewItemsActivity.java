package com.example.travelpack;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

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
public class ViewItemsActivity extends AppCompatActivity {
    ArrayList<Item> picked_items;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void AddCheckbox(LinearLayout layout, Item item, int is_picked) {
        CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setText(item.name);
        checkBox.setTextSize(16);
        checkBox.setTranslationY(-300);
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.items_trips_list_layout);
        picked_items = new ArrayList<>();
        Intent mIntent = getIntent();

        DBHelper_Items DB_Items = new DBHelper_Items(this);

        Integer tripNo = ViewTripsActivity.tripNo;

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
        button.setBackgroundColor(Color.BLUE);
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
}
