package com.dicoding.syahrul.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_github_favorite";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USERS = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.UsersColumns.TABLE_NAME,
            DatabaseContract.UsersColumns._ID,
            DatabaseContract.UsersColumns.USERNAME,
            DatabaseContract.UsersColumns.AVATAR
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.UsersColumns.TABLE_NAME);
        onCreate(db);
    }

}