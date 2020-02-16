package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Recipes extends AppCompatActivity {

    SearchDBHelper SDB;
    Button search, bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getSupportActionBar().setTitle("Recipes");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SDB = new SearchDBHelper(this);
        SDB.initData();

        search = (Button)findViewById(R.id.search_btn);
        bm = (Button)findViewById(R.id.BMR_btn);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopOut();
            }
        });

        bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Recipes.this, BookmarkedRecipes.class));
            }
        });


    }

    //TODO: new intent here, pass the arguments to ViewRecipe.class and proceed with the search on the server from there! use .putExtra()
    public void searchPopOut() {
        Toast.makeText(Recipes.this, "Separate each ingredient with a comma please!", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(Recipes.this);
        dialog.setContentView(R.layout.recipe_search_dialog);
        TextView msg = (TextView)dialog.findViewById(R.id.searchmsg);
        final EditText name = (EditText)dialog.findViewById(R.id.searchname);
        final EditText preptime = (EditText)dialog.findViewById(R.id.searchpreptime);
        final EditText calories = (EditText)dialog.findViewById(R.id.searchcalories);
        final EditText ingredients = (EditText)dialog.findViewById(R.id.searching);
        final CheckBox uselocalinv = (CheckBox)dialog.findViewById(R.id.useinv);
        Button search = (Button)dialog.findViewById(R.id.searchbtn);
        Button cancel = (Button)dialog.findViewById(R.id.searchcancelbtn);

        msg.setEnabled(true);
        name.setEnabled(true);
        preptime.setEnabled(true);
        calories.setEnabled(true);
        ingredients.setEnabled(true);
        uselocalinv.setEnabled(true);
        search.setEnabled(true);
        cancel.setEnabled(true);

        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Recipes.this, "Separate each ingredient with a comma please!", Toast.LENGTH_LONG).show();
            }
        });

        uselocalinv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uselocalinv.isChecked()) {
                    ingredients.setEnabled(false);
                    ingredients.setText("");
                    ingredients.setHint("Using fridge inventory");
                }
                else {
                    ingredients.setEnabled(true);
                    ingredients.setHint("Ingredients");
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass_name = name.getText().toString();
                String pass_pt = preptime.getText().toString();
                String pass_cal = calories.getText().toString();
                String pass_ing = ingredients.getText().toString();
                //pass parameters to new activity
                //new intent here
                Intent intent = new Intent(Recipes.this, SearchResult.class);
                //putExtra...
                if (uselocalinv.isChecked()) {
                    intent.putExtra("use_inv", true);
                    intent.putExtra("ingredients", pass_ing);
                }
                else {
                    intent.putExtra("use_inv", false);
                    intent.putExtra("ingredients", pass_ing);
                }
                if (!pass_name.equals("")) {
                    intent.putExtra("name", pass_name);
                }
                if (!pass_pt.equals("")) {
                    intent.putExtra("prep_time", pass_pt);
                }
                if (!pass_cal.equals("")) {
                    intent.putExtra("calories", pass_cal);
                }
                dialog.dismiss();
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
