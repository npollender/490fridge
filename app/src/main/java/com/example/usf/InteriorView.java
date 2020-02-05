package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class InteriorView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_view);
        getSupportActionBar().setTitle("Interior View");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
