package com.example.travelpack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_Trips extends SQLiteOpenHelper {
    public static final String DBNAME = "Trips.db";

    public DBHelper_Trips(Context context) {
        super(context, "Trips.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table trips(trip_no INTEGER primary key," +
                "user_email TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists trips");
        onCreate(db);
    }

    public Boolean insertTrip(Integer trip_no, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("trip_no", trip_no);
        contentValues.put("user_email", email);
//        contentValues.put("destination", destination);
//        contentValues.put("date", date);
//        contentValues.put("type", type);
//        contentValues.put("no_days", no_days);
        long result = MyDB.insert("trips", null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

//    public Integer getMaxTripNo() {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        Cursor cursor = MyDB.rawQuery("Select max(trip_no) from trips", null);
//        if (cursor.getCount() <= 0) {
//            return 0;
//        }
//        cursor.moveToFirst();
//        return cursor.getInt(cursor.getColumnIndex("trip_no"));
//    }
}
