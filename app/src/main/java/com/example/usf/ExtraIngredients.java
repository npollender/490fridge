package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ExtraIngredients extends AppCompatActivity {

    ExtraIngredientsDBHelper EIDB;
    Button add_data;
    ListView ingredientsList;

    ArrayList<String> ingTable;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_ingredients);
        getSupportActionBar().setTitle("List of Extra Ingredients");

        EIDB = new ExtraIngredientsDBHelper(this);
        ingTable = new ArrayList<>();

        add_data = (Button)findViewById(R.id.addeibtn);
        ingredientsList = (ListView)findViewById(R.id.ei_list);

        viewData();

        ingredientsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ingredientsList.getItemAtPosition(position).toString();
                deletePopOut(text);
                return true;
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDataPopOut();
            }
        });
    }

    //displays the data of each row into the listview
    private void viewData() {
        Cursor cursor = EIDB.viewData();

        if (cursor.getCount() == 0) {
            //Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                String toAdd = cursor.getString(1);

                ingTable.add(toAdd);
            }
            Collections.sort(ingTable);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingTable){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView)super.getView(position, convertView, parent);
                    Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf");
                    tv.setTypeface(tf);
                    return tv;
                }
            };;
            ingredientsList.setAdapter(adapter);
        }
    }

    public void enterDataPopOut() {
        final Dialog dialog = new Dialog(ExtraIngredients.this);
        dialog.setContentView(R.layout.add_shopping_list_dialog);
        final EditText name = (EditText)dialog.findViewById(R.id.iname);
        Button ok = (Button)dialog.findViewById(R.id.okbtn);
        Button cancel = (Button)dialog.findViewById(R.id.cancelbtn);
        Button m = (Button)dialog.findViewById(R.id.add_sl_minus);
        Button p = (Button)dialog.findViewById(R.id.add_sl_plus);
        TextView t = (TextView)dialog.findViewById(R.id.new_sl_qty);
        TextView msg = (TextView)dialog.findViewById(R.id.sl_txt);

        msg.setText("Enter an extra ingredient!");

        m.setVisibility(View.GONE);
        p.setVisibility(View.GONE);
        t.setVisibility(View.GONE);

        name.setEnabled(true);
        ok.setEnabled(true);
        cancel.setEnabled(true);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                boolean result = EIDB.insertData(n);
                if (result) {
                    Toast.makeText(ExtraIngredients.this, "Data added!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ExtraIngredients.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void deletePopOut(final String s) {
        final Dialog dialog = new Dialog(ExtraIngredients.this);
        dialog.setContentView(R.layout.delete_shopping_list_dialog);
        TextView prompt = (TextView)dialog.findViewById(R.id.areyousure);
        Button yes = (Button)dialog.findViewById(R.id.yesbtn);
        Button no = (Button)dialog.findViewById(R.id.nobtn);

        prompt.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EIDB.deleteRow(s);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
