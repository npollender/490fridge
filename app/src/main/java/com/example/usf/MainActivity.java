package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome!");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);

        Button invbtn = (Button)findViewById(R.id.invbtn);
        Button cambtn = (Button)findViewById(R.id.cambtn);
        Button listbtn = (Button)findViewById(R.id.listbtn);
        Button recipebtn = (Button)findViewById(R.id.recipebtn);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.settings_fp);

        invbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Inventory.class));
            }
        });
        cambtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InteriorView.class));
            }
        });
        listbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShoppingList.class));
            }
        });
        recipebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Recipes.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
    }
}
