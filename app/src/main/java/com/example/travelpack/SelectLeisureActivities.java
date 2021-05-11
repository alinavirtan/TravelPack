package com.example.travelpack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SelectLeisureActivities extends AppCompatActivity {
    GridLayout mainGrid;
    Button btnPacking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_leisure_activities);
        (MainActivity.activities).clear();

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        btnPacking = (Button) findViewById(R.id.btnPacking);

        // set Event
        setToogleEvent(mainGrid);

        // on clicking button
        btnPacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLeisureActivities.this, PickItems.class);
                intent.putExtra("FROM_ACTIVITY", "SelectLeisureActivities");
                startActivity(intent);
            }
        });
    }

    private void setToogleEvent(GridLayout mainGrid) {
        // Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String activity = getResources().getResourceEntryName(cardView.getId());

                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        // change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        (MainActivity.activities).add(activity);
                    } else {
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        if ((MainActivity.activities).contains(activity)) {
                            (MainActivity.activities).remove(activity);
                        }
                    }
                }
            });
        }
    }
}
