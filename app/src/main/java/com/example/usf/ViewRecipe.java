package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ViewRecipe extends AppCompatActivity {

    //get extras
    String name, desc, ings, qings, source, servings, pt, nv, attach;
    int category;
    boolean tag;
    TextView tname, tdesc, tings, tsource, tpt, tnv, tcategory, tservings;
    Intent intent;
    FloatingActionButton missing;
    ShoppingListDBHelper SLDB;
    InventoryDBHelper IDB;
    ExtraIngredientsDBHelper EDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setTitle("Viewing Recipe");
        SLDB = new ShoppingListDBHelper(this);
        IDB = new InventoryDBHelper(this);
        EDB = new ExtraIngredientsDBHelper(this);

        intent = getIntent();
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        ings = intent.getStringExtra("ings");
        qings = intent.getStringExtra("qings");
        category = intent.getIntExtra("cat", 0);
        source = intent.getStringExtra("source");
        servings = intent.getStringExtra("servings");
        pt = intent.getStringExtra("prep_time");
        nv = intent.getStringExtra("nut_vals");
        attach = intent.getStringExtra("attach");
        tag = intent.getBooleanExtra("tag", false);

        tname = (TextView)findViewById(R.id.VR_name);
        tdesc = (TextView)findViewById(R.id.vr_desc);
        tings = (TextView)findViewById(R.id.vr_ing);
        tpt = (TextView)findViewById(R.id.vr_pt);
        tnv = (TextView)findViewById(R.id.vr_nv);
        tcategory = (TextView)findViewById(R.id.vr_cat);
        tservings = (TextView)findViewById(R.id.vr_servings);
        missing = (FloatingActionButton)findViewById(R.id.add_missing_btn);

        final String split[] = parseIngs(ings);
        String qsplit[] = parseIngs(qings);
        tings.setText("");
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1) {
                tings.append(qsplit[i] + " " + split[i] + ".");
            }
            else {
                tings.append(qsplit[i] + " " + split[i] + ", ");
            }
        }

        tname.setText("\n" + name);
        tdesc.setText(desc);
        tpt.setText(pt);
        tnv.setText(nv);
        tservings.setText(servings);

        missing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDupes(split);
            }
        });
    }

    public String[] parseIngs(String ings) {
        String parsedIngs[] = ings.split(",");
        return parsedIngs;
    }

    public void findDupes(String[] ings) {
        String[] inv_names = new String[6];
        for (int i = 0; i < inv_names.length; i++) {
            inv_names[i] = IDB.getName(i + 1);
        }
        String[] e_inv_names = EDB.getNames();
        String[] sl_inv_names = SLDB.getNames();
        String[] both = new String[inv_names.length + e_inv_names.length + sl_inv_names.length];
        System.arraycopy(inv_names, 0, both, 0, inv_names.length);
        System.arraycopy(e_inv_names, 0, both, inv_names.length, e_inv_names.length);
        System.arraycopy(sl_inv_names, 0, both, e_inv_names.length, sl_inv_names.length);
        for (int i = 0; i < ings.length; i++) {
            for (int j = 0; j < both.length; j++) {
                if (ings[i].equals(both[j])) break;
                if (j == both.length - 1) SLDB.insertData(ings[i], 0, false);
            }
        }
    }
}
