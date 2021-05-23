package com.example.travelpack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.libraries.places.widget.Autocomplete;

public class DBHelper_Items extends SQLiteOpenHelper {
    public static final String DBNAME = "Items.db";

    public DBHelper_Items(Context context) {
        super(context, "Items.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table items(id_item INTEGER primary key," +
                "name TEXT," +
                "trip_no INTEGER," +
                "user_email TEXT," +
                "is_picked INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists trips");
        onCreate(db);
    }

    public void updateById(Integer id_item, String name, Integer is_picked) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_item", id_item);
        cv.put("name", name);
        cv.put("is_picked", is_picked);

        MyDB.update("items", cv, "id_item = ?", new String[] {id_item.toString()});
    }

    public Boolean insertItem(Integer id_item, String name, Integer trip_no, String email, boolean is_picked) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_item", id_item);
        contentValues.put("name", name);
        contentValues.put("trip_no", trip_no);
        contentValues.put("user_email", email);
        contentValues.put("is_picked", is_picked);

        long result = MyDB.insert("items", null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

    public int getMaxItemId() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from items where id_item = (select max(id_item) from items)", null);
        if (cursor.getCount() <= 0) {
            return 0;
        }
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("id_item"));
    }

    public Cursor getItemsTrip(Integer tripNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from items where trip_no = ? ",
                new String[] {tripNo.toString()});
        return cursor;
    }
}
