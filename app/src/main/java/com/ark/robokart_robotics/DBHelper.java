package com.ark.robokart_robotics;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

        // Table Name
        public static final String TABLE_NAME = "NOTIFICATIONS";

        // Table columns
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String BODY = "body";
    public static final String TIME = "time";

        // Database Information
        static final String DB_NAME = "Robokart.DB";

        // database version
        static final int DB_VERSION = 1;

        // Creating table query
        private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, " + BODY + " TEXT, " + TIME + " TEXT);";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }



    }