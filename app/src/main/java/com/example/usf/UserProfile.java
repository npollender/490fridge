package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {

    EditText un, pw;
    Button login;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String STATUS = "status";
    public static final String ID = "GYN45X18";
    public static final String PW = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        un = (EditText)findViewById(R.id.username);
        pw = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (un.getText().toString().equals(ID) && pw.getText().toString().equals(PW)) {
                    //login success
                    login();
                    startActivity(new Intent(UserProfile.this, MainActivity.class));
                }
            }
        });

        load();
    }

    public void login() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(STATUS, true);

        editor.apply();

        Toast.makeText(UserProfile.this, "Logging in...", Toast.LENGTH_SHORT).show();
    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.getBoolean(STATUS, false);
    }
}
