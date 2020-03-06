package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final String PREF = "my_prefs";
    public static final String STATUS = "status";

    ExtraIngredientsDBHelper EDB;
    InventoryDBHelper IDB;
    RecipesDBHelper RDB;
    SearchDBHelper SDB;
    ShoppingListDBHelper SLDB;
    UserProfile UP = new UserProfile();

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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

        DBINIT();

        this.sp = getSharedPreferences(PREF, 0);
        this.sp.getBoolean(STATUS, false);

        if (!this.sp.getBoolean(STATUS, false)) {
            loginPopOut();
        }


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

    //initialize each database, this will prevent the app from crashing anywhere else in the code if the tables are not yet present.
    public void DBINIT() {
        EDB = new ExtraIngredientsDBHelper(this);
        IDB = new InventoryDBHelper(this);
        RDB = new RecipesDBHelper(this);
        SDB = new SearchDBHelper(this);
        SLDB = new ShoppingListDBHelper(this);
    }

    public void loginPopOut() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.login);
        final EditText username = (EditText)dialog.findViewById(R.id.usernametxt);
        final EditText password = (EditText)dialog.findViewById(R.id.passwordtxt);
        Button login = (Button)dialog.findViewById(R.id.loginbtn);

        username.setEnabled(true);
        password.setEnabled(true);
        login.setEnabled(true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("GYN") && password.getText().toString().equals("123")) {
                    editor = sp.edit();
                    editor.putBoolean(STATUS, true);
                    editor.apply();
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
