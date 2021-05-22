package com.example.travelpack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.libraries.places.widget.Autocomplete;

public class DBHelper_Trips extends SQLiteOpenHelper {
    public static final String DBNAME = "Trips.db";

    public DBHelper_Trips(Context context) {
        super(context, "Trips.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table trips(trip_no INTEGER primary key," +
                "user_email TEXT,"+
                "destination TEXT,"+
                "date TEXT," +
                "type TEXT," +
                "no_days INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists trips");
        onCreate(db);
    }

    public Boolean insertTrip(Integer trip_no, String email, String destination, String date, String type, Integer no_days) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("trip_no", trip_no);
        contentValues.put("user_email", email);
        contentValues.put("destination", destination);
        contentValues.put("date", date);
        contentValues.put("type", type);
        contentValues.put("no_days", no_days);
        long result = MyDB.insert("trips", null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

    public int getMaxTripNo() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from trips where trip_no = (select max(trip_no) from trips)", null);
        if (cursor.getCount() <= 0) {
            return 0;
        }
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("trip_no"));
    }

    public Cursor getTripsUser(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from trips where user_email = ? ",
                new String[] {email});
        return cursor;
    }
}
