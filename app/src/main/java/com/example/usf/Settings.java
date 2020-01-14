package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Button tmp;
    ShoppingListDBHelper SLDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tmp = (Button)findViewById(R.id.tmpbtn);
        SLDB = new ShoppingListDBHelper(this);

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLDB.deleteData();
                Toast.makeText(Settings.this, "Attempting to erase table...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
