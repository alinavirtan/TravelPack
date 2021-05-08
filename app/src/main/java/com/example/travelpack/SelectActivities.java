package com.example.travelpack;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SelectActivities extends AppCompatActivity {
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_activities);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        // set Event
        setSingleEvent(mainGrid);
        setToogleEvent(mainGrid);
    }

    private void setToogleEvent(GridLayout mainGrid) {
        // Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        // change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                    } else {
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        // Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(SelectActivities.this, "Clicked at index "+ finalI, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
