package com.example.usf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RecipesDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "recipes.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "RECIPES";
    public static final String _ID = "ID";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "DESCRIPTION";
    public static final String COL_3 = "DIRECTIONS";
    public static final String COL_4 = "INGREDIENTS";
    public static final String COL_5 = "QUANTITY";
    public static final String COL_6 = "CATEGORY";
    public static final String COL_7 = "SOURCE";
    public static final String COL_8 = "SERVINGS";
    public static final String COL_9 = "PREP_TIME";
    public static final String COL_10 = "NUT_VALS";
    public static final String COL_11 = "ATTACHMENTS";
    public static final String COL_12 = "TAG";

    public RecipesDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SL_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 + " TEXT NOT NULL, " +
                COL_2 + " TEXT NOT NULL, " +
                COL_3 + " TEXT NOT NULL, " +
                COL_4 + " TEXT NOT NULL, " +
                COL_5 + " TEXT NOT NULL, " +
                COL_6 + " TEXT NOT NULL, " +
                COL_7 + " TEXT, " +
                COL_8 + " TEXT NOT NULL, " +
                COL_9 + " TEXT NOT NULL, " +
                COL_10 + " TEXT NOT NULL, " +
                COL_11 + " TEXT NOT NULL, " +
                COL_12 + " BOOLEAN NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_SL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //for displaying the table data in the listview
    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    //deletes a specific row depending on the name.
    public void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + "=?", new String[] {id});
    }

    public boolean insertData(String name, String desc, String dirts, String ings, String qings,  String category, String source, String servings, String pt, String nv, String attach, boolean tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, name);
        cv.put(COL_2, desc);
        cv.put(COL_3, dirts);
        cv.put(COL_4, ings);
        cv.put(COL_5, qings);
        cv.put(COL_6, category);
        cv.put(COL_7, source);
        cv.put(COL_8, servings);
        cv.put(COL_9, pt);
        cv.put(COL_10, nv);
        cv.put(COL_11, attach);
        cv.put(COL_12, tag);

        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    public String getDesc(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(2);
        return result;
    }

    public String getDirects(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(3);
        return result;
    }

    public String getIngredients(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(4);
        return result;
    }

    public String getQuantities(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(5);
        return result;
    }

    public String getCategory(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(6);
        return result;
    }

    public String getSource(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(7);
        return result;
    }

    public String getServings(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(8);
        return result;
    }

    public String getPrepTime(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(9);
        return result;
    }

    public String getNutritionalValues(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(10);
        return result;
    }

    public String getAttachments(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = cursor.getString(11);
        return result;
    }

    public boolean getTag(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        boolean result = cursor.getInt(12) > 0;
        return result;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
