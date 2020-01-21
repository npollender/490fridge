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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {

    ShoppingListDBHelper SLDB;
    InventoryDBHelper IDB;
    Button add_data, check_low;
    ListView shoppinglist;

    ArrayList<String> listTable;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        getSupportActionBar().hide();

        SLDB = new ShoppingListDBHelper(this);
        IDB = new InventoryDBHelper(this);

        listTable = new ArrayList<>();

        add_data = (Button)findViewById(R.id.adddatabtn);
        check_low = (Button)findViewById(R.id.checklowbtn);
        shoppinglist = (ListView)findViewById(R.id.shopping_list);

        viewData();

        shoppinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = shoppinglist.getItemAtPosition(position).toString();
                //this is fucking garbage, find a better way to do this
                String array[] = text.split("x ");
                String rowID = array[1];
                deletePopOut(rowID);
            }
        });

        shoppinglist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = shoppinglist.getItemAtPosition(position).toString();
                //this is fucking garbage, find a better way to do this
                String array[] = text.split("x ");
                String rowID = array[1];
                changeData(rowID);
                return true;
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDataPopOut();
            }
        });

        check_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TEST VALUES
                addFromInv();
            }
        });
    }

    //displays the data of each row into the listview
    private void viewData() {
        Cursor cursor = SLDB.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                String checkFlag = cursor.getString(3);
                String toAdd = cursor.getString(2) + "x " + cursor.getString(1);
                if (checkFlag.equals("*")) {
                    toAdd = "\u2726 " + cursor.getString(2) + "x " + cursor.getString(1);
                }
                
                listTable.add(toAdd);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTable);
            shoppinglist.setAdapter(adapter);
        }
    }

    //searches the db to see if an item already exists (for the recommended items only atm)
    public boolean findDupe(String name) {
        Cursor cursor = SLDB.viewData();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String checkName = cursor.getString(1);
                if (checkName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    //dialog box for entering data, inserts data into the table upon confirmation
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

    //deletes data from specific row selected, data is removed upon confirmation
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

    //this checks if the values of the inventory (from fridge) are too low, if they are, then the low items will be available to select to add to the table
    public void addFromInv() {
        final Dialog dialog = new Dialog(ShoppingList.this);
        dialog.setContentView(R.layout.add_from_inventory);
        TextView prompt = (TextView)dialog.findViewById(R.id.lowinvtxt);
        Button yes = (Button)dialog.findViewById(R.id.addinvbtn);
        Button no = (Button)dialog.findViewById(R.id.naddinvbtn);
        final CheckBox cbA = (CheckBox)dialog.findViewById(R.id.cbA);
        final CheckBox cbB = (CheckBox)dialog.findViewById(R.id.cbB);
        final CheckBox cbC = (CheckBox)dialog.findViewById(R.id.cbC);
        final CheckBox cbD = (CheckBox)dialog.findViewById(R.id.cbD);
        final CheckBox cbE = (CheckBox)dialog.findViewById(R.id.cbE);
        final CheckBox cbF = (CheckBox)dialog.findViewById(R.id.cbF);

        prompt.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        final String pName[] = new String[6];
        for (int i = 0; i < 6; i++) {
            pName[i] = IDB.getName(i + 1);
        }
        cbA.setText(pName[0]);
        cbB.setText(pName[1]);
        cbC.setText(pName[2]);
        cbD.setText(pName[3]);
        cbE.setText(pName[4]);
        cbF.setText(pName[5]);

        if (IDB.checkIfLow(1)) {
            if (!findDupe(pName[0])) {
                cbA.setEnabled(true);
            }
            else cbA.setVisibility(View.GONE);
        }
        else cbA.setVisibility(View.GONE);
        if (IDB.checkIfLow(2)) {
            if (!findDupe(pName[1])) {
                cbB.setEnabled(true);
            }
            else cbB.setVisibility(View.GONE);
        }
        else cbB.setVisibility(View.GONE);
        if (IDB.checkIfLow(3)) {
            if (!findDupe(pName[2])) {
                cbC.setEnabled(true);
            }
            else cbC.setVisibility(View.GONE);
        }
        else cbC.setVisibility(View.GONE);
        if (IDB.checkIfLow(4)) {
            if (!findDupe(pName[3])) {
                cbD.setEnabled(true);
            }
            else cbD.setVisibility(View.GONE);
        }
        else cbD.setVisibility(View.GONE);
        if (IDB.checkIfLow(5)) {
            if (!findDupe(pName[4])) {
                cbE.setEnabled(true);
            }
            else cbE.setVisibility(View.GONE);
        }
        else cbE.setVisibility(View.GONE);
        if (IDB.checkIfLow(6)) {
            if (!findDupe(pName[5])) {
                cbF.setEnabled(true);
            }
            else cbF.setVisibility(View.GONE);
        }
        else cbF.setVisibility(View.GONE);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbA.isChecked()) {
                    SLDB.insertData(pName[0], 0, true);
                }
                if (cbB.isChecked()) {
                    SLDB.insertData(pName[1], 0, true);
                }
                if (cbC.isChecked()) {
                    SLDB.insertData(pName[2], 0, true);
                }
                if (cbD.isChecked()) {
                    SLDB.insertData(pName[3], 0, true);
                }
                if (cbE.isChecked()) {
                    SLDB.insertData(pName[4], 0, true);
                }
                if (cbF.isChecked()) {
                    SLDB.insertData(pName[5], 0, true);
                }
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

        dialog.show();
    }

    public void changeData(final String name) {
        final Dialog dialog = new Dialog(ShoppingList.this);
        dialog.setContentView(R.layout.change_shopping_list_entry);
        TextView prompt = (TextView)dialog.findViewById(R.id.editSL);
        final EditText newname = (EditText)dialog.findViewById(R.id.newnameSL);
        final EditText newamt = (EditText)dialog.findViewById(R.id.newamountSL);
        Button yes = (Button)dialog.findViewById(R.id.okSLEbtn);
        Button no = (Button)dialog.findViewById(R.id.cancelSLEbtn);

        prompt.setEnabled(true);
        newname.setEnabled(true);
        newamt.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = newname.getText().toString();
                int new_amt = Integer.parseInt(newamt.getText().toString());
                SLDB.changeData(name, new_name, new_amt);
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

        dialog.show();
    }
}
