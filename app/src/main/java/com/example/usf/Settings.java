package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Button tmp, tmp2, a_ing;
    ShoppingListDBHelper SLDB;
    SearchDBHelper SDB;
    RecipesDBHelper RDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tmp = (Button)findViewById(R.id.tmpbtn);
        tmp2 = (Button)findViewById(R.id.tmp2btn);
        a_ing = (Button)findViewById(R.id.aingbtn);
        SLDB = new ShoppingListDBHelper(this);
        SDB = new SearchDBHelper(this);
        RDB = new RecipesDBHelper(this);

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLDB.deleteData();
                Toast.makeText(Settings.this, "Attempting to erase table...", Toast.LENGTH_SHORT).show();
            }
        });

        tmp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDB.deleteData();
                RDB.deleteData();
            }
        });

        a_ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, ExtraIngredients.class));
            }
        });
    }
}
