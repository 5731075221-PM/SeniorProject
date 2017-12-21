package com.example.uefi.seniorproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by UEFI on 20/12/2560.
 */

public class DBHelperDAO {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBHelperDAO(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<String> getAllList(){
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM hospital", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
