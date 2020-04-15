package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ViewRecipe extends AppCompatActivity {

    //get extras
    String name, desc, instr, ings, qings, source, servings, pt, nv, attach;
    String category;
    boolean tag, fromSearch;
    TextView tname, tdesc, tings, tinstr, tsource, tpt, tnv, tcategory, tservings;
    ImageView iv;
    Intent intent;
    FloatingActionButton missing, addToBM;
    RecipesDBHelper RDB;
    ShoppingListDBHelper SLDB;
    InventoryDBHelper IDB;
    ExtraIngredientsDBHelper EDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setTitle("Viewing Recipe");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SLDB = new ShoppingListDBHelper(this);
        IDB = new InventoryDBHelper(this);
        EDB = new ExtraIngredientsDBHelper(this);
        RDB = new RecipesDBHelper(this);

        intent = getIntent();
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        instr = intent.getStringExtra("instr");
        ings = intent.getStringExtra("ings");
        qings = intent.getStringExtra("qings");
        category = intent.getStringExtra("cat");
        source = intent.getStringExtra("source");
        servings = intent.getStringExtra("servings");
        pt = intent.getStringExtra("prep_time");
        nv = intent.getStringExtra("nut_vals");
        attach = intent.getStringExtra("attach");
        tag = intent.getBooleanExtra("tag", false);
        fromSearch = intent.getBooleanExtra("fromSearch", false);

        tname = (TextView)findViewById(R.id.VR_name);
        tdesc = (TextView)findViewById(R.id.vr_desc);
        tinstr = (TextView)findViewById(R.id.vr_instr);
        tings = (TextView)findViewById(R.id.vr_ing);
        tpt = (TextView)findViewById(R.id.vr_pt);
        tnv = (TextView)findViewById(R.id.vr_nv);
        tcategory = (TextView)findViewById(R.id.vr_cat);
        tservings = (TextView)findViewById(R.id.vr_servings);
        missing = (FloatingActionButton)findViewById(R.id.add_missing_btn);
        iv = (ImageView)findViewById(R.id.dish_pic);
        addToBM = (FloatingActionButton)findViewById(R.id.add_to_bookmarks);

        tcategory.setText("Category:\n" + category);
        if (!fromSearch) {
            addToBM.hide();
        }

        addToBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RDB.insertData(name, desc, instr, ings, qings, category, source, servings, pt, nv, attach, tag);
                Toast.makeText(ViewRecipe.this, "Recipe added to bookmarks!", Toast.LENGTH_SHORT).show();
                addToBM.hide();
            }
        });

        final String split[] = ings.split(" \\| ");
        String qsplit[] = qings.split(" \\| ");
        tings.setText("Ingredients:\n");
        try {

        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1) {
                tings.append("\u2022 "+qsplit[i] + " " + split[i] + ".");
            }
            else {
                tings.append("\u2022 "+qsplit[i] + " " + split[i] + "\n");
            }
        }}catch(Exception ignored) {}

        String[] instr_split = instr.split(" \\| ");

        tdesc.setText("Description:\n" + desc);
        tinstr.setText("Instructions:\n");
        for (int i = 0; i < instr_split.length; i++) {
            if (i == instr_split.length - 1) {
                tinstr.append(i+1 +". "+instr_split[i]);
            }
            else {
                tinstr.append(i+1 +". "+instr_split[i] + "\n");
            }
        }

        tname.setText(name);
        tpt.setText("Prep time:\n" + pt);
        tnv.setText("Nutrional Values:\n"+nv);
        tservings.setText("Servings:\n" + servings);

        missing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findDupes(split)) {
                    Toast.makeText(ViewRecipe.this, "Added missing ingredients to shopping list!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewRecipe.this, ShoppingList.class));
                }
                else Toast.makeText(ViewRecipe.this, "You have all the ingredients!", Toast.LENGTH_SHORT).show();
            }
        });
        if (name.equals("Pizza")) {
            iv.setImageResource(R.drawable.pizza);
        }

        //TODO: automatically open pdf
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(source);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public boolean findDupes(String[] ings) {
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
        int result = 0;
        for (int i = 0; i < ings.length; i++) {
            for (int j = 0; j < both.length; j++) {
                if (ings[i].equals(both[j])) break;
                if (j == both.length - 1) {
                    SLDB.insertData(ings[i], 0, false);
                    result++;
                }
            }
        }
        return result > 0;
    }
}
