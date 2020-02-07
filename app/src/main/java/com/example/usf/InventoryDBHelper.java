package com.example.usf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class InventoryDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "inventory.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "INVENTORY";
    public static final String _ID = "ROW_ID";
    public static final String COL_1 = "ITEM_NAME";
    public static final String COL_2 = "WEIGHT";
    public static final String COL_3 = "FLAG";

    public InventoryDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SL_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 + " TEXT NOT NULL, " +
                COL_2 + " REAL NOT NULL, " +
                COL_3 + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_SL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //if below a certain value, the row will be declared as low inventory
    public boolean checkIfLow(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String sweight = cursor.getString(2);
        cursor.close();
        double weight = Double.parseDouble(sweight);
        return weight < 108;
    }

    //returns name of a row depending on id
    public String getName(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String name = cursor.getString(1);
        return name;
    }

    //updates the name of a row in the table
    public void changeName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        db.update(TABLE_NAME, cv, _ID + " = " + id, null);
    }

    public void updateWeight(int id, double weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, weight);
        db.update(TABLE_NAME, cv, _ID + " = " + id, null);
    }

    //Initializes the table with the 6 rows, 1 row for each partition ID'd from 0 to 5.
    public void initData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, "DEFAULT");
        cv.put(COL_2, 100);
        cv.put(COL_3, "");

        String count = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(count, null);

        if (cursor.getCount() == 0) {
            db.insert(TABLE_NAME, null, cv);
            db.insert(TABLE_NAME, null, cv);
            db.insert(TABLE_NAME, null, cv);
            db.insert(TABLE_NAME, null, cv);
            db.insert(TABLE_NAME, null, cv);
            db.insert(TABLE_NAME, null, cv);
        }
        cursor.close();
    }

    //display each row of the table
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
}
