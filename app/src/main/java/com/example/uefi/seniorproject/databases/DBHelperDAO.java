package com.example.uefi.seniorproject.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;

import com.example.uefi.seniorproject.firstaid.Firstaid;
import com.example.uefi.seniorproject.fragment.Hospital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ArrayList<Hospital> getHospital() {
        ArrayList<Hospital> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM hospital", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] tmp = (cursor.getString(cursor.getColumnIndex("location"))).split(", ");
            list.add(new Hospital(cursor.getString(cursor.getColumnIndex("name")),
                    Double.parseDouble(tmp[0]),Double.parseDouble(tmp[1]),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("phone")),
                    cursor.getString(cursor.getColumnIndex("website")),
                    cursor.getString(cursor.getColumnIndex("zone"))
                                )
                    );
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Hospital> selectHospital(String p, String z) {
        ArrayList<Hospital> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM hospital", null);
        if(p != "" && z != "") cursor = database.rawQuery("SELECT * FROM hospital WHERE province='"+p+"' and zone='"+z+"'", null);
        else if(p == "" && z != "") cursor = database.rawQuery("SELECT * FROM hospital WHERE zone='"+z+"'", null);
        else if(p != "" && z == "") cursor = database.rawQuery("SELECT * FROM hospital WHERE province='"+p+"' ORDER BY name", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] tmp = (cursor.getString(cursor.getColumnIndex("location"))).split(", ");
            list.add(new Hospital(cursor.getString(cursor.getColumnIndex("name")),
                            Double.parseDouble(tmp[0]),Double.parseDouble(tmp[1]),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getString(cursor.getColumnIndex("phone")),
                            cursor.getString(cursor.getColumnIndex("website")),
                            cursor.getString(cursor.getColumnIndex("zone"))
                    )
            );
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("ProvName = ",list.size()+"");
        return list;
    }

    public ArrayList<String> getFirstaid1Name() {
        ArrayList<String>  list = new ArrayList<String>();
//        Map<Integer,String> map = new HashMap<Integer,String>();
        Cursor cursor = database.rawQuery("SELECT * FROM firstaid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
//            map.put(cursor.getInt(cursor.getColumnIndex("firstaid_id")),cursor.getString(cursor.getColumnIndex("firstaid_name")));
            list.add(cursor.getString(cursor.getColumnIndex("firstaid_name")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Map<Integer,String> getFirstaid() {
//        Map<Integer,String>  list = new ArrayList<Map<Integer,String>>();
        Map<Integer,String> map = new HashMap<Integer,String>();
        Cursor cursor = database.rawQuery("SELECT * FROM firstaid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            map.put(cursor.getInt(cursor.getColumnIndex("firstaid_id")),cursor.getString(cursor.getColumnIndex("firstaid_name")));
//            list.add(cursor.getString(cursor.getColumnIndex("firstaid_name")));
            cursor.moveToNext();
        }
        cursor.close();
        return map;
    }

    public ArrayList<String> getFirstaidSubject(int firstaid) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM firstaid_subject WHERE firstaid_id ='"+firstaid+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("firstaid_subject_name")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public ArrayList<Firstaid> getFirstaid1() {
        ArrayList<Firstaid> list = new ArrayList<Firstaid>();

        Cursor cursor = database.rawQuery("SELECT * FROM firstaid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Firstaid firstaid = new Firstaid();
            Map<List<String>,Map<Integer,String>> mapSubject = new HashMap<List<String>,Map<Integer,String>>();
            List<String> subject = new ArrayList<String>();
            Map<Integer,String> mapDetail = new HashMap<Integer,String>();

            firstaid.setFirstaid_id(cursor.getInt(cursor.getColumnIndex("firstaid_id")));
            firstaid.setFirstaid_name(cursor.getString(cursor.getColumnIndex("firstaid_name")));
            firstaid.setFirstaid_detail(cursor.getString(cursor.getColumnIndex("firstaid_detail")));

            int idFirstaid = cursor.getInt(cursor.getColumnIndex("firstaid_id"));

            Cursor cursor1 = database.rawQuery("SELECT * FROM firstaid_subject WHERE firstaid_id ='"+idFirstaid+"'", null);
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {

                int idSubject = cursor1.getInt(cursor.getColumnIndex("firstaid_subject_id"));

                Cursor cursor2 = database.rawQuery("SELECT * FROM firstaid_detail WHERE firstaid_subject_id ='"+idSubject+"'", null);
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast()) {
                    mapDetail.put(cursor2.getInt(cursor2.getColumnIndex("firstaid_detail_number")),cursor.getString(cursor.getColumnIndex("firstaid_detail_name")));
                    cursor2.moveToNext();
                }
                cursor2.close();
                subject.add(cursor1.getString(cursor.getColumnIndex("firstaid_subject_name")));
                subject.add(cursor1.getString(cursor.getColumnIndex("firstaid_subject_detail")));
                mapSubject.put(subject,mapDetail);
                cursor1.moveToNext();
            }
            cursor1.close();
            cursor.moveToNext();
            firstaid.setDetail(mapSubject);
            list.add(firstaid);
        }
        cursor.close();

        return list;
    }

//    public ArrayList<Pair<Double,Double>> getLatLng() {
//        ArrayList<Pair<Double,Double>> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT location FROM hospital", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String[] tmp = (cursor.getString(0)).split(", ");
////            Log.d("tmp = ",tmp.toString());
//            Log.d("tmp[0] = ",Double.parseDouble(tmp[0])+"");
//            Log.d("tmp[1] = ",Double.parseDouble(tmp[1])+"");
//            list.add(new Pair<Double, Double>(Double.parseDouble(tmp[0]),Double.parseDouble(tmp[1])));
////            list.add(new Pair<Double, Double>(cursor.getDouble(cursor.getColumnIndex("lat")),cursor.getDouble(cursor.getColumnIndex("lng"))));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//    public ArrayList<Pair<String,String>> getAddrPhone() {
//        ArrayList<Pair<String,String>> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT address,phone FROM hospital", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(new Pair<String, String>(cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("phone"))));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
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
