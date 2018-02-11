package com.example.uefi.seniorproject.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by UEFI on 20/12/2560.
 */
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper {
//    private static final String DATABASE_NAME = "seniorproject";
    private static final String DATABASE_NAME = "seniorda.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
//public class DBHelper extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "seniorproject";
//    private static final int DATABASE_VERSION = 1;
//    Context context;
//
//    private static final String tableSQL = "CREATE TABLE hospital (" +
//            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            "name TEXT" +
//            "address TEXT"+
//            "phone TEXT"+
//            "website TEXT"+
//            ");";
//
//    public DBHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        this.context = context;
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(tableSQL);
//        String insertData1 = "INSERT INTO hospital (name,address) VALUES ('abc','123')";
//        String insertData2 = "INSERT INTO hospital (name,address) VALUES ('def','456')";
//        db.execSQL(insertData1);
//        db.execSQL(insertData2);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//}