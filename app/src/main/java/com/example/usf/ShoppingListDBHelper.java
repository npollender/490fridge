package com.example.usf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ShoppingListDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "shoppinglist.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "SHOPPING_LIST";
    public static final String _ID = "ROW_ID";
    public static final String COL_1 = "ITEM_NAME";
    public static final String COL_2 = "AMOUNT";
    public static final String COL_3 = "SUGGESTED";

    public ShoppingListDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SL_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 + " TEXT NOT NULL, " +
                COL_2 + " INTEGER NOT NULL, " +
                COL_3 + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_SL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //inserts a new row to the table, each row is one item on the shopping list.
    //the boolean is for the suggested flag. if its true, print a * next to the item
    public boolean insertData(String name, int amount, boolean suggested) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String sugString = "";
        if (suggested) {
            sugString = "*";
        }
        cv.put(COL_1, name);
        cv.put(COL_2, amount);
        cv.put(COL_3, sugString);

        long result = db.insert(TABLE_NAME, null, cv);

        return result != -1;
    }

    //for displaying the table data in the listview
    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public String[] getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        int n = (int)count;
        Cursor cursor = viewData();
        String[] names = new String[n];

        int i = 0;
        while (cursor.moveToNext()) {
            names[i] = cursor.getString(1);
            i++;
        }
        return names;
    }

    //deletes the entire table
    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    //deletes a specific row depending on the name.
    public void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + "=?", new String[] {id});
    }

    //edit an item on the list
    public void changeData(String name, String new_name, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, new_name);
        cv.put(COL_2, amount);
        db.update(TABLE_NAME, cv, COL_1 + " =?", new String[] {name});
    }
}
