package com.example.usf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ExtraIngredientsDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "extraingr.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "EXTRA_INGREDIENTS";
    public static final String _ID = "ROW_ID";
    public static final String COL_1 = "ITEM_NAME";

    public ExtraIngredientsDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SL_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_SL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //inserts a new row to the table, each row is one item of the extra ingredients
    public boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);

        long result = db.insert(TABLE_NAME, null, cv);

        return result != -1;
    }

    //edit an item in the extra ingredients
    public void changeData(String name, String new_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, new_name);
        db.update(TABLE_NAME, cv, COL_1 + " =?", new String[] {name});
    }

    //for displaying the table data in the listview
    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + "=?", new String[] {id});
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
}
