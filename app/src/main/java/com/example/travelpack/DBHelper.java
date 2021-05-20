package com.example.travelpack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(email TEXT primary key," +
                                         "username TEXT," +
                                         "password TEXT)");
        MyDB.execSQL("create Table luggage(id_luggage INTEGER primary key," +
                                           "name TEXT," +
                                           "trip_no INTEGER," +
                                           "user_email TEXT," +
                                           "foreign key(user_email) references users(email))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists luggage");
    }

    public Boolean insertData(String email, String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

    public Boolean insertItem(Integer id_luggage, String name_item, Integer trip_no, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_luggage", id_luggage);
        contentValues.put("name", name_item);
        contentValues.put("trip_no", trip_no);
        contentValues.put("user_email", email);
        long result = MyDB.insert("luggage", null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

    public Boolean deleteItem(String name_item, Integer trip_no, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("DELETE FROM luggage WHERE email='"+email+"' and trip_no='"+trip_no+"' and name_item='"+name_item+"'  ");
        return true;
    }

    public Boolean deleteTrip(String name_item, Integer trip_no, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("DELETE FROM luggage WHERE email='"+email+"' and trip_no='"+trip_no+"'");
        return true;
    }

    public Boolean selectItem(String name_item, Integer trip_no, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from luggage where item = ? and email = ? and trip_no = ?",
                                        new String[] {name_item, email, Integer.toString(trip_no)});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[] {email});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password = ?", new String[] {email,password});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }
}
