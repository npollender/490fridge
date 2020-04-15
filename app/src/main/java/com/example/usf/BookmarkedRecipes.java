package com.example.usf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookmarkedRecipes extends AppCompatActivity {

    RecipesDBHelper RDB;
    ListView recipelist;

    ArrayList<String> recipeTable;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_recipes);
        getSupportActionBar().setTitle("Bookmarked Recipes");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RDB = new RecipesDBHelper(this);
        recipeTable = new ArrayList<>();

        recipelist = (ListView)findViewById(R.id.bm_list);

        viewData();

        //brings you to viewrecipe activity and displays the selected recipe contents
        recipelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = recipelist.getItemAtPosition(position).toString();
                Intent intent = passParameters(text);
                startActivity(intent);
                //passes the name of the selected listview row, then will search that name in the db and display all the data from that row
            }
        });

        //for deleting a recipe
        recipelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //delete dialog
                String text = recipelist.getItemAtPosition(position).toString();
                deletePopOut(text);
                return true;
            }
        });
    }

    //displays the data of each row into the listview
    private void viewData() {
        Cursor cursor = RDB.viewData();

        if (cursor.getCount() == 0) {
            //Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                String toAdd = cursor.getString(1);

                recipeTable.add(toAdd);
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeTable){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView)super.getView(position, convertView, parent);
                    Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf");
                    tv.setTypeface(tf);
                    return tv;
                }
            };;
            recipelist.setAdapter(adapter);
        }
    }

    //popup dialog for deleting a row, if user confirms, row is deleted from the table
    public void deletePopOut(final String s) {
        final Dialog dialog = new Dialog(BookmarkedRecipes.this);
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
                RDB.deleteRow(s);
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

    //takes all the information of a recipe from the table and passes them to the putallextras method
    //the returned intent is used to navigate to the viewrecipe activity with all the recipe info
    public Intent passParameters(String name) {
        Intent intent;
        String desc, ings, instr, qings, source, servings, pt, nv, attach;
        String category;
        boolean tag;

        desc = RDB.getDesc(name);
        ings = RDB.getIngredients(name);
        instr = RDB.getDirects(name);
        qings = RDB.getQuantities(name);
        source = RDB.getSource(name);
        servings = RDB.getServings(name);
        pt = RDB.getPrepTime(name);
        nv = RDB.getNutritionalValues(name);
        attach = RDB.getAttachments(name);
        category = RDB.getCategory(name);
        tag = RDB.getTag(name);
        intent = putAllExtras(name, desc, instr, ings, qings, category, source, servings, pt, nv, attach, tag);
        return intent;
    }

    //puts all the extras into the intent, each column of the selected recipe are individually included in the putextra
    //the passed parameters can be
    public Intent putAllExtras(String name, String desc, String instr, String ings, String qings, String category, String source, String servings, String pt, String nv, String attach, boolean tag) {
        Intent intent = new Intent(BookmarkedRecipes.this, ViewRecipe.class);
        intent.putExtra("name", name);
        intent.putExtra("desc", desc);
        intent.putExtra("instr", instr);
        intent.putExtra("ings", ings);
        intent.putExtra("qings", qings);
        intent.putExtra("cat", category);
        intent.putExtra("source", source);
        intent.putExtra("servings", servings);
        intent.putExtra("prep_time", pt);
        intent.putExtra("nut_vals", nv);
        intent.putExtra("attach", attach);
        intent.putExtra("tag", tag);
        return intent;
    }

    //TODO: perhaps make a global method for setting the action bar...
    public void setActionbar() {

    }
}
