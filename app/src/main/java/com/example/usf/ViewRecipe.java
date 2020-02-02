package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewRecipe extends AppCompatActivity {

    //get extras
    String name, desc, ings, qings, source, servings, pt, nv, attach;
    int category;
    boolean tag;
    TextView tname, tdesc, tings, tsource, tpt, tnv, tcategory, tservings;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().hide();

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

        String split[] = parseIngs(ings);
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
    }

    public String[] parseIngs(String ings) {
        String parsedIngs[] = ings.split(",");
        return parsedIngs;
    }
}
