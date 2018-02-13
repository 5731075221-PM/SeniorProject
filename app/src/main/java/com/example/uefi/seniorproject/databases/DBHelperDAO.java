package com.example.uefi.seniorproject.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;

import com.example.uefi.seniorproject.firstaid.DetailItem;
import com.example.uefi.seniorproject.firstaid.PicDetailItem;
import com.example.uefi.seniorproject.firstaid.PicItem;
import com.example.uefi.seniorproject.firstaid.SubjectItem;
import com.example.uefi.seniorproject.fragment.Hospital;
import com.example.uefi.seniorproject.fragment.Symptom;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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
        return list;
    }

    public ArrayList<String> getLexitron(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM lexitron", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getStopword(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM stopword", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Symptom> getAllSymptoms(){
        ArrayList<Symptom> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM allsymptoms", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Symptom(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("word")),
                    cursor.getString(cursor.getColumnIndex("parent"))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Symptom> getMainSymptoms(){
//        System.out.println("CheckDB = Main");
        ArrayList<Symptom> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM mainsymptoms", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Symptom(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("word")),
                    null
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<ArrayList<String>> getVectorData(){
//        System.out.println("CheckDB = Vectordata");
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM vectordata", null);
        cursor.moveToFirst();
        int size = cursor.getColumnCount()-1, n = 0;

        for(int i = 0; i<cursor.getCount() ;i++){
            list.add(new ArrayList<String>());
        }

        while (!cursor.isAfterLast()) {
            for(int i = 0; i < size; i++){
//                System.out.println("CheckDB = i "+i);
                list.get(n).add(cursor.getString(cursor.getColumnIndex(i+"")));
            }
            n++;
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getFreq(){
//        System.out.println("CheckDB = Freq");
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM idfdoc", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getDocLength(){
//        System.out.println("CheckDB = Doclength");
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM vectorlength", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("length")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getDiseaseName(){
//        System.out.println("CheckDB = Name");
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM diseasesandsymptoms", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getContent(String str,String type){
        String list = "";
        Cursor cursor = database.rawQuery("SELECT "+type+" FROM diseases WHERE name="+"'"+str+"'", null);
        cursor.moveToFirst();
        list = cursor.getString(0);
        cursor.close();
        return list;
    }

    public ArrayList<String> checkKeyword(String[] w){
        System.out.println("Setdatabase = "+w.toString());
        Set<String> set = new LinkedHashSet<>();
        ArrayList<String> list;
        String parent = "";
        for(int i = 0; i<w.length;i++) {
            Cursor cursor = database.rawQuery("SELECT * FROM allsymptoms WHERE word like '%" + w[i] + "%' ORDER BY word ASC", null);
            cursor.moveToFirst();
            System.out.println("Setdatabase = " + cursor.getCount());
            while (!cursor.isAfterLast()) {
                parent = cursor.getString(cursor.getColumnIndex("parent"));
                System.out.println("Setdatabase = " + parent);
                if (parent != null && parent != "") {
                    set.add(parent);
                } else set.add(cursor.getString(cursor.getColumnIndex("word")));
                cursor.moveToNext();
            }
        }
            list = new ArrayList<>(set);
            return list;
    }

    public ArrayList<Integer> getIndexSymptom(ArrayList<String> arr){
        ArrayList list =  new ArrayList();
        for(int i = 0; i<arr.size();i++){
            Cursor cursor = database.rawQuery("SELECT * FROM mainsymptoms WHERE word='"+arr.get(i)+"' ORDER BY word ASC", null);
            cursor.moveToFirst();
            list.add(cursor.getInt(cursor.getColumnIndex("id")));
        }
        return list;
    }


    public ArrayList<String> getFirstaidList(int indicator){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM firstaid WHERE id_subject='"+indicator+"' ORDER BY subject ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("subject")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public int getFirstaidId(String indicator){
        int id = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM firstaid WHERE subject='"+indicator+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id=cursor.getInt(cursor.getColumnIndex("id"));
            cursor.moveToNext();
        }
        cursor.close();
        return id;
    }

    public ArrayList getFirstaidDetail(int indicator){
        ArrayList list = new ArrayList();
        Cursor cursor = database.rawQuery("SELECT * FROM firstaidDetail WHERE idFirstaid='"+indicator+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(!cursor.isNull(2)) {
                list.add(new SubjectItem(cursor.getString(cursor.getColumnIndex("subject"))));
                if(!cursor.isNull(3)) {
                    String[] check = {"2.","3.","4.","5.","6.","7.","8.","9."};
                    String temp = cursor.getString(cursor.getColumnIndex("detail"));
                    String add = "";
                    for(int i =0;i<check.length;i++){
                        if(temp.toLowerCase().contains(check[i].toLowerCase())){

                            int index = temp.indexOf(check[i], 0);

                            add = temp.substring(0, index);
                            temp = temp.substring(index);

                            list.add(new DetailItem(add));
                        }
                    }
                    list.add(new DetailItem(temp+"\n"));
                }
            }else if(!cursor.isNull(3)) {
                String[] check = {"2.","3.","4.","5.","6.","7.","8.","9."};
                String temp = cursor.getString(cursor.getColumnIndex("detail"));
                String add = "";
                for(int i =0;i<check.length;i++){
                    if(temp.toLowerCase().contains(check[i].toLowerCase())){

                        int index = temp.indexOf(check[i], 0);

                        add = temp.substring(0, index);
                        temp = temp.substring(index);

                        list.add(new DetailItem(add));
                    }
                }
                list.add(new DetailItem(temp+"\n"));
            }else if (!cursor.isNull(4)) {
                list.add(new PicItem(cursor.getBlob(4)));
                if (!cursor.isNull(5)) {
                    list.add(new PicDetailItem(cursor.getString(cursor.getColumnIndex("pictureDetail"))));
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public byte[] test(){
//        byte[] img;
        Cursor cursor = database.rawQuery("SELECT * FROM test WHERE id='"+2+"'", null);
        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(cursor.getString(cursor.getColumnIndex("subject")));
            byte[] img = cursor.getBlob(1);
            cursor.moveToNext();
//        }
        cursor.close();
        return img;
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
