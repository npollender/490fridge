package com.example.usf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {

    ShoppingListDBHelper SLDB;
    Button add_data;
    EditText name;
    EditText amount;
    ListView shoppinglist;

    ArrayList<String> listTable;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        getSupportActionBar().hide();

        SLDB = new ShoppingListDBHelper(this);

        listTable = new ArrayList<>();

        add_data = (Button)findViewById(R.id.adddatabtn);
        shoppinglist = (ListView)findViewById(R.id.shopping_list);

        viewData();

        shoppinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = shoppinglist.getItemAtPosition(position).toString();
                String array[] = text.split(" ", 5);
                String rowID = array[1];
                deletePopOut(rowID);
                //Toast.makeText(ShoppingList.this, "Your item will be removed when you exit this page", Toast.LENGTH_SHORT).show();
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**String n = name.getText().toString();
                String a = amount.getText().toString();
                int aint = Integer.parseInt(a);
                boolean result = SLDB.insertData(n, aint, false);
                if (result) {
                    Toast.makeText(ShoppingList.this, "Data added!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ShoppingList.this, "Error!", Toast.LENGTH_SHORT).show();
                }*/

                enterDataPopOut();
            }
        });
    }

    private void viewData() {
        Cursor cursor = SLDB.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                String toAdd = "NAME: " + cursor.getString(1) + " | AMOUNT: " + cursor.getString(2);
                listTable.add(toAdd);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTable);
            shoppinglist.setAdapter(adapter);
        }
    }

    public void enterDataPopOut() {
        final Dialog dialog = new Dialog(ShoppingList.this);
        dialog.setContentView(R.layout.add_shopping_list_dialog);
        final EditText name = (EditText)dialog.findViewById(R.id.iname);
        final EditText qty = (EditText)dialog.findViewById(R.id.iqty);
        Button ok = (Button)dialog.findViewById(R.id.okbtn);
        Button cancel = (Button)dialog.findViewById(R.id.cancelbtn);

        name.setEnabled(true);
        qty.setEnabled(true);
        ok.setEnabled(true);
        cancel.setEnabled(true);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                String q = qty.getText().toString();
                int qint = Integer.parseInt(q);
                boolean result = SLDB.insertData(n, qint, false);
                if (result) {
                    Toast.makeText(ShoppingList.this, "Data added!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ShoppingList.this, "Error!", Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(ShoppingList.this);
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
                SLDB.deleteRow(s);
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
