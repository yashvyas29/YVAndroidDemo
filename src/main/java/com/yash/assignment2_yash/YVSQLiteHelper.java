package com.yash.assignment2_yash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yash on 22/09/16.
 */

public class YVSQLiteHelper extends SQLiteOpenHelper {

    private static final String dbName = "CDP_Android_Assignment";
    private static final int dbVersion = 1;
    private final String pkColumnName = "_id";

    public YVSQLiteHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    private String  getCreateUserTableSQL() {
        final String sql = "CREATE TABLE USER (" + pkColumnName + " INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, username TEXT UNIQUE NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL)";
        return sql;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(getCreateUserTableSQL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(YVSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old data.");
    }
}
