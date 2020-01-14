package com.example.usf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    /** currently only for testing
    public boolean checkIfLow(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + "='" + id;
        Cursor cursor = db.rawQuery(query, null);
        double weight = cursor.getDouble(cursor.getColumnIndex(COL_2));
        return weight < 1;
    } */

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

    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
