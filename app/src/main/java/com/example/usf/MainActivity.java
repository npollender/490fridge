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
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String CHECKED = "checked";

    ExtraIngredientsDBHelper EDB;
    InventoryDBHelper IDB;
    RecipesDBHelper RDB;
    SearchDBHelper SDB;
    ShoppingListDBHelper SLDB;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("GYN Smart Refrigerator");
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
        this.sp.getBoolean(CHECKED, false);

        String totv = sp.getString(NAME, "");
        TextView usf = (TextView)findViewById(R.id.usftext);
        if (!usf.equals("")) {
            usf.setText("Welcome, " + totv + "!");
        }
        else {
            usf.setText("Welcome to GYN!");
        }

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

        if (checkWeights()) {
            if (!sp.getBoolean(CHECKED, false)) {
                editor = sp.edit();
                editor.putBoolean(CHECKED, true);
                editor.apply();
                toSL();
            }
        }
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
        dialog.setCancelable(false);
        dialog.show();
    }

    public void toSL() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.delete_shopping_list_dialog);
        TextView prompt = (TextView)dialog.findViewById(R.id.areyousure);
        Button yes = (Button)dialog.findViewById(R.id.yesbtn);
        Button no = (Button)dialog.findViewById(R.id.nobtn);

        prompt.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        prompt.setText("Low inventory on item(s), go to shopping list?");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingList.class);
                intent.putExtra("open_r", true);
                dialog.dismiss();
                startActivity(intent);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public Boolean checkWeights() {
        Boolean result = false;
        int i = 0;
        editor = sp.edit();
        if (IDB.checkIfLow(1)) {
            result = true;
        }
        else {
            i++;
        }
        if (IDB.checkIfLow(2)) {
            result = true;
        }
        else {
            i++;
        }
        if (IDB.checkIfLow(3)) {
            result = true;
        }
        else {
            i++;
        }
        if (IDB.checkIfLow(4)) {
            result = true;
        }
        else {
            i++;
        }
        if (IDB.checkIfLow(5)) {
            result = true;
        }
        else {
            i++;
        }
        if (IDB.checkIfLow(6)) {
            result = true;
        }
        else {
            i++;
        }
        if (i == 6) {
            editor.putBoolean(CHECKED, false);
            editor.apply();
        }
        return result;
    }
}
