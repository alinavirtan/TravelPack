package com.example.travelpack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

public class ViewTripsActivity extends AppCompatActivity {
    DBHelper_Trips DB;
    static int tripNo;
    SwipeMenuListView swipeListView;
    LinearLayout layout;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);

        layout = (LinearLayout) findViewById(R.id.trips_list_layout);
        swipeListView = (SwipeMenuListView) findViewById(R.id.swipeListView);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        swipeListView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setIcon(R.drawable.ic_edit);
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        swipeListView.setMenuCreator(creator);

        // set swipe direction
        swipeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        DB = new DBHelper_Trips(this);
        Cursor cursor = DB.getTripsUser(LoginActivity.email_trip);

        if (cursor.getCount() > 0 ) {
            if (cursor.moveToFirst()) {
                do {
                    String text = cursor.getString(cursor.getColumnIndex("destination")) +
                            " - " + cursor.getString(cursor.getColumnIndex("date"));

                    // add trip to the list
                    arrayList.add(text);
                    adapter.notifyDataSetChanged();
                } while (cursor.moveToNext());
            }
        }

        swipeListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                swipeListView.setSelector(R.color.purple_500);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        //swipeListView.setCloseInterpolator(new BounceInterpolator());

        // perform listView item click event
        swipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                swipeListView.setSelector(R.color.purple_500);

                switch (index) {
                    case 0:
                        // open
                        String trip_to_open = arrayList.get(position);
                        String[] open_tokens = trip_to_open.split(" - ");
                        String open_name = open_tokens[0];
                        String open_date = open_tokens[1];

                        tripNo = DB.getTripDestDate(LoginActivity.email_trip, open_name, open_date);

                        Intent intent = new Intent(ViewTripsActivity.this, ViewItemsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // delete

                        /* get trip information - location and date */
                        String trip_to_delete = arrayList.get(position);

                        /* unselect trip from swipelist view */
                        swipeListView.setSelector(R.color.white);

                        /* show message after pressing the delete button */
                        Toast.makeText(getApplicationContext(), "You deleted " + trip_to_delete,Toast.LENGTH_LONG).show();

                        /* delete trip from database */
                        String[] tokens = trip_to_delete.split(" - ");
                        String name = tokens[0];
                        String date = tokens[1];
                        tripNo = DB.getTripDestDate(LoginActivity.email_trip, name, date);
                        DB.deleteTrip(tripNo);

                        /* dynamically remove trip from the list */
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }
}