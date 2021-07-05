package com.ark.robokart_robotics.Activities.shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String CONTACTS_COLUMN_CITY = "mrp";
    public static final String CONTACTS_COLUMN_EMAIL = "image";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "price";
    public static final String CONTACTS_COLUMN_STREET = "qty";
    public static final String CONTACTS_TABLE_NAME = "cart";
    public static final String DATABASE_NAME = "Robokartshop.db";

    /* renamed from: hp */
    private HashMap f7hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cart (id integer primary key, name text,image text,qty text, mrp text,price text,item_id text)");
        db.execSQL("create table fav (id integer primary key, name text,image text, mrp text,price text,item_id text)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS fav");
        onCreate(db);
    }

    public boolean insertCart(String name, String image, String qty, String mrp, String price, String item_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", image);
        contentValues.put(CONTACTS_COLUMN_STREET, qty);
        contentValues.put(CONTACTS_COLUMN_CITY, mrp);
        contentValues.put("price", price);
        contentValues.put(FirebaseAnalytics.Param.ITEM_ID, item_id);
        db.insert(CONTACTS_TABLE_NAME, (String) null, contentValues);
        return true;
    }
    public boolean insertFav(String name, String image, String mrp, String price, String item_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", image);
        contentValues.put(CONTACTS_COLUMN_CITY, mrp);
        contentValues.put("price", price);
        contentValues.put(FirebaseAnalytics.Param.ITEM_ID, item_id);
        db.insert("fav", (String) null, contentValues);
        return true;
    }

    public Cursor getData() {
        return getReadableDatabase().rawQuery("select * from cart", (String[]) null);
    }
    public Cursor getFavData() {
        return getReadableDatabase().rawQuery("select * from fav", (String[]) null);
    }

    public int numberOfRows() {
        return (int) DatabaseUtils.queryNumEntries(getReadableDatabase(), CONTACTS_TABLE_NAME);
    }

    public boolean updateCart(Integer id, String qty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_STREET, qty);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id.intValue())});
        return true;
    }

    public Integer deleteContact(Integer id) {
        return Integer.valueOf(getWritableDatabase().delete(CONTACTS_TABLE_NAME, "id = ? ", new String[]{Integer.toString(id.intValue())}));
    }
    public Integer deleteFav(Integer id) {
        return Integer.valueOf(getWritableDatabase().delete("fav", "id = ? ", new String[]{Integer.toString(id.intValue())}));
    }

    public boolean fireSql(String sql) {
        getReadableDatabase().rawQuery(sql, (String[]) null);
        return true;
    }

    public ArrayList<String> getAllCart() {
        ArrayList<String> array_list = new ArrayList<>();
        Cursor res = getReadableDatabase().rawQuery("select * from cart", (String[]) null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllFav() {
        ArrayList<String> array_list = new ArrayList<>();
        Cursor res = getReadableDatabase().rawQuery("select * from fav", (String[]) null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }
}
