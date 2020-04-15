package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.MissingFormatArgumentException;

public class Settings extends AppCompatActivity {

    Button tmp, tmp2, tmp3, cn;
    TextView nameofuser;
    ShoppingListDBHelper SLDB;
    SearchDBHelper SDB;
    RecipesDBHelper RDB;
    ExtraIngredientsDBHelper EDB;

    public static final String PREF = "my_prefs";
    public static final String NAME = "name";
    public static final String STATUS = "status";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    String un;

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
        tmp3 = (Button)findViewById(R.id.tmp3btn);
        tmp3.setText("Logout");
        cn = (Button)findViewById(R.id.changenamebtn);
        nameofuser = (TextView)findViewById(R.id.nameofuser);
        SLDB = new ShoppingListDBHelper(this);
        SDB = new SearchDBHelper(this);
        RDB = new RecipesDBHelper(this);
        EDB = new ExtraIngredientsDBHelper(this);

        this.sp = getSharedPreferences(PREF, 0);
        this.sp.getString(NAME, "DEFAULT");

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLDB.deleteData();
            }
        });

        tmp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EDB.deleteData();
            }
        });

        tmp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });

        cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });

        load();
        updateView();
    }

    public void save(String new_name) {
        editor = sp.edit();
        editor.putString(NAME, new_name);
        editor.apply();
    }

    public void load() {
        un = sp.getString(NAME, "");
    }

    public void updateView() {
        nameofuser.setText("Hello, " + un);
    }

    public void changeName() {
        final Dialog dialog = new Dialog(Settings.this);
        dialog.setContentView(R.layout.change_name_partition);
        TextView prompt = (TextView)dialog.findViewById(R.id.changenametxt);
        final EditText newname = (EditText)dialog.findViewById(R.id.newnameedit);
        Button yes = (Button)dialog.findViewById(R.id.yeschangebtn);
        Button no = (Button)dialog.findViewById(R.id.nochangebtn);
        prompt.setText("Change name of user:");
        yes.setText("Confirm");
        no.setText("Cancel");

        prompt.setEnabled(true);
        newname.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(newname.getText().toString());
                dialog.dismiss();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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

    public void logout() {
        editor = sp.edit();
        editor.putBoolean(STATUS, false);
        editor.apply();
        startActivity(new Intent(Settings.this, MainActivity.class));
    }
}
