package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        getSupportActionBar().setTitle("Shopping List");

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
                String array[] = text.split("x\t\t");
                String rowID = array[1];
                deletePopOut(rowID);
            }
        });

        shoppinglist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = shoppinglist.getItemAtPosition(position).toString();
                //this is fucking garbage, find a better way to do this
                String array[] = text.split("x\t\t");
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
            //Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                String checkFlag = cursor.getString(3);
                String toAdd = cursor.getString(2) + "x\t\t" + cursor.getString(1);
                if (checkFlag.equals("*")) {
                    toAdd = "\u2726 " + cursor.getString(2) + "x\t\t" + cursor.getString(1);
                }
                
                listTable.add(toAdd);
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listTable){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView)super.getView(position, convertView, parent);
                    Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf");
                    tv.setTextSize(20);
                    tv.setTypeface(tf);
                    return tv;
                }
            };
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
        Button ok = (Button)dialog.findViewById(R.id.okbtn);
        Button minus = (Button)dialog.findViewById(R.id.add_sl_minus);
        Button plus = (Button)dialog.findViewById(R.id.add_sl_plus);
        final TextView qty = (TextView)dialog.findViewById(R.id.new_sl_qty);
        Button cancel = (Button)dialog.findViewById(R.id.cancelbtn);

        name.setEnabled(true);
        ok.setEnabled(true);
        cancel.setEnabled(true);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = qty.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                qty.setText(""+itmp);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = qty.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                qty.setText(""+itmp);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                int q = Integer.parseInt(qty.getText().toString());
                if (n.isEmpty()) {
                    Toast.makeText(ShoppingList.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean result = SLDB.insertData(n, q, false);
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
        Button ma = (Button)dialog.findViewById(R.id.pa_minus);
        Button mb = (Button)dialog.findViewById(R.id.pb_minus);
        Button mc = (Button)dialog.findViewById(R.id.pc_minus);
        Button md = (Button)dialog.findViewById(R.id.pd_minus);
        Button me = (Button)dialog.findViewById(R.id.pe_minus);
        Button mf = (Button)dialog.findViewById(R.id.pf_minus);
        Button pa = (Button)dialog.findViewById(R.id.pa_plus);
        Button pb = (Button)dialog.findViewById(R.id.pb_plus);
        Button pc = (Button)dialog.findViewById(R.id.pc_plus);
        Button pd = (Button)dialog.findViewById(R.id.pd_plus);
        Button pe = (Button)dialog.findViewById(R.id.pe_plus);
        Button pf = (Button)dialog.findViewById(R.id.pf_plus);
        final TextView ta = (TextView)dialog.findViewById(R.id.pa_num);
        final TextView tb = (TextView)dialog.findViewById(R.id.pb_num);
        final TextView tc = (TextView)dialog.findViewById(R.id.pc_num);
        final TextView td = (TextView)dialog.findViewById(R.id.pd_num);
        final TextView te = (TextView)dialog.findViewById(R.id.pe_num);
        final TextView tf = (TextView)dialog.findViewById(R.id.pf_num);
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

        int leave = 0;
        for (int i = 0; i < 6; i++) {
            if (!IDB.checkIfLow(i + 1) || findDupe(pName[i])) leave++;
        }
        if (leave > 5) {
            Toast.makeText(ShoppingList.this, "No items to suggest!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (IDB.checkIfLow(1)) {
            if (!findDupe(pName[0])) {
                cbA.setEnabled(true);
            }
            else {
                cbA.setVisibility(View.GONE);
                ma.setVisibility(View.GONE);
                pa.setVisibility(View.GONE);
                ta.setVisibility(View.GONE);
            }
        }
        else {
            cbA.setVisibility(View.GONE);
            ma.setVisibility(View.GONE);
            pa.setVisibility(View.GONE);
            ta.setVisibility(View.GONE);
        }
        if (IDB.checkIfLow(2)) {
            if (!findDupe(pName[1])) {
                cbB.setEnabled(true);
            }
            else {
                cbB.setVisibility(View.GONE);
                mb.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                tb.setVisibility(View.GONE);
            }
        }
        else {
            cbB.setVisibility(View.GONE);
            mb.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            tb.setVisibility(View.GONE);
        }
        if (IDB.checkIfLow(3)) {
            if (!findDupe(pName[2])) {
                cbC.setEnabled(true);
            }
            else {
                cbC.setVisibility(View.GONE);
                mc.setVisibility(View.GONE);
                pc.setVisibility(View.GONE);
                tc.setVisibility(View.GONE);
            }
        }
        else {
            cbC.setVisibility(View.GONE);
            mc.setVisibility(View.GONE);
            pc.setVisibility(View.GONE);
            tc.setVisibility(View.GONE);
        }
        if (IDB.checkIfLow(4)) {
            if (!findDupe(pName[3])) {
                cbD.setEnabled(true);
            }
            else {
                cbD.setVisibility(View.GONE);
                md.setVisibility(View.GONE);
                pd.setVisibility(View.GONE);
                td.setVisibility(View.GONE);
            }
        }
        else {
            cbD.setVisibility(View.GONE);
            md.setVisibility(View.GONE);
            pd.setVisibility(View.GONE);
            td.setVisibility(View.GONE);
        }
        if (IDB.checkIfLow(5)) {
            if (!findDupe(pName[4])) {
                cbE.setEnabled(true);
            }
            else {
                cbE.setVisibility(View.GONE);
                me.setVisibility(View.GONE);
                pe.setVisibility(View.GONE);
                te.setVisibility(View.GONE);
            }
        }
        else {
            cbE.setVisibility(View.GONE);
            me.setVisibility(View.GONE);
            pe.setVisibility(View.GONE);
            te.setVisibility(View.GONE);
        }
        if (IDB.checkIfLow(6)) {
            if (!findDupe(pName[5])) {
                cbF.setEnabled(true);
            }
            else {
                cbF.setVisibility(View.GONE);
                mf.setVisibility(View.GONE);
                pf.setVisibility(View.GONE);
                tf.setVisibility(View.GONE);
            }
        }
        else {
            cbF.setVisibility(View.GONE);
            mf.setVisibility(View.GONE);
            pf.setVisibility(View.GONE);
            tf.setVisibility(View.GONE);
        }

        ma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = ta.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                ta.setText(""+itmp);
            }
        });
        mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = tb.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                tb.setText(""+itmp);
            }
        });
        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = tc.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                tc.setText(""+itmp);
            }
        });
        md.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = td.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                td.setText(""+itmp);
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = te.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                te.setText(""+itmp);
            }
        });
        mf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = tf.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                tf.setText(""+itmp);
            }
        });
        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = ta.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                ta.setText(""+itmp);
            }
        });
        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = tb.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                tb.setText(""+itmp);
            }
        });
        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = tc.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                tc.setText(""+itmp);
            }
        });
        pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = td.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                td.setText(""+itmp);
            }
        });
        pe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = te.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                te.setText(""+itmp);
            }
        });
        pf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = tf.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                tf.setText(""+itmp);
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbA.isChecked()) {
                    SLDB.insertData(pName[0], Integer.parseInt(ta.getText().toString()), true);
                }
                if (cbB.isChecked()) {
                    SLDB.insertData(pName[1], Integer.parseInt(tb.getText().toString()), true);
                }
                if (cbC.isChecked()) {
                    SLDB.insertData(pName[2], Integer.parseInt(tc.getText().toString()), true);
                }
                if (cbD.isChecked()) {
                    SLDB.insertData(pName[3], Integer.parseInt(td.getText().toString()), true);
                }
                if (cbE.isChecked()) {
                    SLDB.insertData(pName[4], Integer.parseInt(te.getText().toString()), true);
                }
                if (cbF.isChecked()) {
                    SLDB.insertData(pName[5], Integer.parseInt(tf.getText().toString()), true);
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void changeData(final String name) {
        final Dialog dialog = new Dialog(ShoppingList.this);
        dialog.setContentView(R.layout.change_shopping_list_entry);
        TextView prompt = (TextView)dialog.findViewById(R.id.editSL);
        final EditText newname = (EditText)dialog.findViewById(R.id.newnameSL);
        Button yes = (Button)dialog.findViewById(R.id.okSLEbtn);
        Button no = (Button)dialog.findViewById(R.id.cancelSLEbtn);
        Button minus = (Button)dialog.findViewById(R.id.edit_sl_minus);
        Button plus = (Button)dialog.findViewById(R.id.edit_sl_plus);
        final TextView qty = (TextView)dialog.findViewById(R.id.edit_sl_txt);

        newname.setVisibility(View.GONE);
        prompt.setEnabled(true);
        newname.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = qty.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp > 0) itmp--;
                qty.setText(""+itmp);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stmp = qty.getText().toString();
                int itmp = Integer.parseInt(stmp);
                if (itmp < 99) itmp++;
                qty.setText(""+itmp);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int new_amt = Integer.parseInt(qty.getText().toString());
                SLDB.changeData(name, name, new_amt);
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
