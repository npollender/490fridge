package com.example.usf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RecipesDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "recipes.db";
    public static final int VERSION = 1;
    public static final String _ID = "ID";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "PREP_TIME";
    public static final String COL_3 = "INGREDIENTS";
    public static final String COL_4 = "DESCRIPTION";

    public RecipesDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
