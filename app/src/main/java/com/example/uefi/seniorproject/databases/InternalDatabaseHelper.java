package com.example.uefi.seniorproject.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by palida on 09-Mar-18.
 */

public class InternalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Note";
    private static final int VERSION =1;
    private static InternalDatabaseHelper internalDatabaseHelper;

    private InternalDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    public static synchronized InternalDatabaseHelper getInstance(Context c){
        if(internalDatabaseHelper==null){
            internalDatabaseHelper = new InternalDatabaseHelper(c.getApplicationContext());
        }
        return internalDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_notes = "CREATE TABLE notes (" +
                "id_note INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "date INTEGER, "+
                "month INTEGER, "+
                "year INTEGER, "+
                ")";
        sqLiteDatabase.execSQL(sql_notes);

        String symptom_notes = "CREATE TABLE symptom_notes (" +
                "id_symptom_note INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "symptom TEXT, "+
                "id_note INTEGER, "+
                ")";
        sqLiteDatabase.execSQL(symptom_notes);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS symptom_notes");

        // create new tables
        onCreate(sqLiteDatabase);
    }
}
