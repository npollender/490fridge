package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button invbtn = (Button)findViewById(R.id.invbtn);
        Button cambtn = (Button)findViewById(R.id.cambtn);
        Button listbtn = (Button)findViewById(R.id.listbtn);
        Button recipebtn = (Button)findViewById(R.id.recipebtn);
        Button stngbtn = (Button)findViewById(R.id.stngbtn);

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
        stngbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
    }
}
