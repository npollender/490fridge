package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InteriorView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_view);
        getSupportActionBar().setTitle("Interior View");
    }
}
