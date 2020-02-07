package com.example.usf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.usf.PostgreSQLHelper.*;

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

    //TODO: add a listview like the shopping list to select a serach result!
    ListView searchlist;
    ArrayList<String> searchTable;
    ArrayAdapter adapter;

    SearchDBHelper SDB;
    InventoryDBHelper IDB;
    ExtraIngredientsDBHelper EDB;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IDB = new InventoryDBHelper(this);
        EDB = new ExtraIngredientsDBHelper(this);

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


        // These two lines are really needed to solve the issue
        // --Something unusual has occurred to cause the driver to fail.--
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // execute the search
        search_in_RDS();



        viewData();

        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name_to_find = searchlist.getItemAtPosition(position).toString();
                Intent intent = passParameters(name_to_find);
                startActivity(intent);
            }
        });
    }


    /**
     * Sample query data from db
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void search_in_RDS() {

        try {
            establishDBConnection(getBaseContext().getAssets().open("db_config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String query_name = "", query_prep_time = "", query_cal = "", query_ing = "";

        // whether or not the input fields from the search were used
        if (passed_name != null) {
            query_name = "SELECT id " +
                    "FROM recipes_tb " +
                    "WHERE name LIKE " + fmtStrDB("%"+passed_name+"%");
        }

        if (passed_pt != null) {
            query_prep_time = "SELECT id " +
                    "FROM recipes_tb " +
                    "WHERE prep_time <= " + fmtStrDB(passed_pt);
        }


//        TODO - for the calorie (once that's setup)
//        if (passed_cal != null) {
//            query_cal = "SELECT * " +
//                    "FROM recipes_tb " +
//                    "WHERE *** <= " + fmtStrDB(passed_cal) + ");";
//        }

        boolean bl_search_ing = (!passed_ing.isEmpty() && ingredients_array.length > 0) ? true : false;
        boolean bl_search_useinv = passed_useinv;

        if (bl_search_ing || bl_search_useinv) {

            //initialize the set of queries to be executed for the search
            ArrayList<String> queries = new ArrayList<String>();

            // Create the temporary table to hold the searched ingredients
            queries.add("CREATE TEMP TABLE temp_ingredients (name VARCHAR(80)) ON COMMIT DELETE ROWS;");
            queries.add("BEGIN TRANSACTION;");

            // insert typed ingredients from the search into the temporary table
            for (String i : ingredients_array) {
                queries.add("INSERT INTO temp_ingredients VALUES (" + fmtStrDB(i) + ");");
            }

            if (bl_search_useinv) {
                // insert the inventory ingredients into the temporary table
                for (String j : IDB.getNames()) {
                    queries.add("INSERT INTO temp_ingredients VALUES (" + fmtStrDB(j) + ");");
                }

                // insert the extra ingredients into the temporary table
                for (String k : EDB.getNames()) {
                    queries.add("INSERT INTO temp_ingredients VALUES (" + fmtStrDB(k) + ");");
                }
            }

            try {
                executeMultipleQ(connect(), queries);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            query_ing = "SELECT id " +
                    "FROM recipes_tb R" +
                    "INNER JOIN temp_ingredients TI" +
                    "ON ( R.ingredient LIKE '%' || TI.name || '%')";
        }

        // TODO - do not forget to include query_cal once it's implemented (INCOMPLETE)
//        String temp_q =(query_name + query_prep_time + query_ing).replace(";S", ";\nUNION\nS");
//        String temp_q = (!query_name.equals("") && !query_prep_time.equals("") && !query_ing.equals("")) ?
//                query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                (!query_name.equals("") && !query_prep_time.equals("")) ?
//                        query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                        (!query_name.equals("") && !query_ing.equals("")) ?
//                                query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                                (!query_prep_time.equals("") && !query_ing.equals("")) ?
//                                        query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                                        (!query_name.equals("")) ?
//                                                query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                                                (!query_prep_time.equals("")) ?
//                                                        query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                                                        (!query_ing.equals("")) ?
//                                                                query_name + "\nUNION\n" + query_prep_time + "\nUNION\n" + query_ing :
//                                                                "";
        if (!query_name.equals("")) {

            String total_queries = "SELECT * " +
                    "FROM recipes_tb " +
                    "WHERE id IN ( " + query_name + ");";

            System.out.println("\n" + total_queries);


            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(total_queries);
                 ResultSet res = pstmt.executeQuery()) {

                SDB.deleteData();

                while (res.next()) {
                    // clear the content of the DB and then insert new one
                    SDB.insertData(res.getString(2), res.getString(3), res.getString(4),
                            res.getString(5), res.getString(6), res.getString(7),
                            res.getString(8), res.getString(9), res.getString(10),
                            res.getString(11), res.getString(12), res.getBoolean(13));
                }

                // after the entire operation, enable the entire deletion of the tables
                if (bl_search_ing || bl_search_useinv)
                    executedDB("COMMIT;");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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
        String desc, inst, ings, qings, source, servings, pt, nv, attach;
        String category;
        boolean tag;

        desc = SDB.getDesc(name);
        inst = SDB.getDirects(name);
        ings = SDB.getIngredients(name);
        qings = SDB.getQuantities(name);
        source = SDB.getSource(name);
        servings = SDB.getServings(name);
        pt = SDB.getPrepTime(name);
        nv = SDB.getNutritionalValues(name);
        attach = SDB.getAttachments(name);
        category = SDB.getCategory(name);
        tag = SDB.getTag(name);
        boolean fromSearch = true;
        intent = putAllExtras(name, desc, inst, ings, qings, category, source, servings, pt, nv, attach, tag, fromSearch);
        return intent;
    }

    public Intent putAllExtras(String name, String desc, String instr, String ings, String qings, String category, String source, String servings, String pt, String nv, String attach, boolean tag, boolean fromSearch) {
        Intent intent = new Intent(SearchResult.this, ViewRecipe.class);
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
        intent.putExtra("fromSearch", fromSearch);
        return intent;
    }

}
