package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    //TODO: probably just do another listview here that generates X results (maybe 5?)
    //      upon returning results, store them temporarily in a table, then if user wishes to bookmark
    //      the recipe, just transfer row from temporary data to permanent table...
    //      The recipes from the search result will be viewed like the ViewRecipe activity like the bookmarked ones
    TextView no_results;
    Intent intent;
    String passed_name, passed_pt, passed_cal, passed_ing;
    String[] ingredients_array;
    boolean passed_useinv;
    SearchDBHelper SDB;
    //TODO: add a listview like the shopping list to select a serach result!
    ListView searchlist;
    ArrayList<String> searchTable;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().setTitle("Search Results");

        SDB = new SearchDBHelper(this);
        intent = getIntent();
        passed_name = intent.getStringExtra("name");
        passed_pt = intent.getStringExtra("prep_time");
        passed_cal = intent.getStringExtra("calories");
        passed_ing = intent.getStringExtra("ingredients");
        passed_useinv = intent.getBooleanExtra("use_inv", false);
        ingredients_array = passed_ing.split(",");

        searchTable = new ArrayList<>();
        searchlist = (ListView)findViewById(R.id.sr_list);

        viewData();
    }

    private void viewData() {
        Cursor cursor = SDB.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                String toAdd = cursor.getString(1);
                if (!toAdd.equals("NULL")) {
                    searchTable.add(toAdd);
                }
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchTable);
            searchlist.setAdapter(adapter);
        }
        if (searchTable.isEmpty()) {
            Toast.makeText(SearchResult.this, "No recipes found!", Toast.LENGTH_LONG).show();
        }
    }

    public Intent passParameters(String name) {
        Intent intent;
        String desc, ings, qings, source, servings, pt, nv, attach;
        int category;
        boolean tag;

        desc = SDB.getDesc(name);
        ings = SDB.getIngredients(name);
        qings = SDB.getQuantities(name);
        source = SDB.getSource(name);
        servings = SDB.getServings(name);
        pt = SDB.getPrepTime(name);
        nv = SDB.getNutritionalValues(name);
        attach = SDB.getAttachments(name);
        category = SDB.getCategory(name);
        tag = SDB.getTag(name);
        intent = putAllExtras(name, desc, ings, qings, category, source, servings, pt, nv, attach, tag);
        return intent;
    }

    public Intent putAllExtras(String name, String desc, String ings, String qings, int category, String source, String servings, String pt, String nv, String attach, boolean tag) {
        Intent intent = new Intent(SearchResult.this, ViewRecipe.class);
        intent.putExtra("name", name);
        intent.putExtra("desc", desc);
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
}
