package com.example.uefi.seniorproject.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by UEFI on 20/12/2560.
 */

public class DBHelperDAO {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBHelperDAO instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DBHelperDAO(Context context) {
        this.openHelper = new DBHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DBHelperDAO getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelperDAO(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public ArrayList<String> getNameHospital() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM hospital", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Pair<Double,Double>> getLatLng() {
        ArrayList<Pair<Double,Double>> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT lat,lng FROM hospital", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Pair<Double, Double>(cursor.getDouble(cursor.getColumnIndex("lat")),cursor.getDouble(cursor.getColumnIndex("lng"))));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Pair<String,String>> getAddrPhone() {
        ArrayList<Pair<String,String>> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT address,phone FROM hospital", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Pair<String, String>(cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("phone"))));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}

//public class DBHelperDAO {
//    private SQLiteDatabase database;
//    private DBHelper dbHelper;
//
//    public DBHelperDAO(Context context){
//        dbHelper = new DBHelper(context);
//    }
//
//    public void open(){
//        database = dbHelper.getWritableDatabase();
//    }
//
//    public void close(){
//        dbHelper.close();
//    }
//
//    public ArrayList<String> getAllList(){
//        ArrayList<String> list = new ArrayList<String>();
//        Cursor cursor = database.rawQuery("SELECT * FROM hospital", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()){
//            list.add(cursor.getString(1));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//}
