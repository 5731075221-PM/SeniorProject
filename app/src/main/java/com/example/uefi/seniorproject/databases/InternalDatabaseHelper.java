package com.example.uefi.seniorproject.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.uefi.seniorproject.reminder.ChoiceItem;
import com.example.uefi.seniorproject.reminder.DayItem;
import com.example.uefi.seniorproject.reminder.EmptyItem;
import com.example.uefi.seniorproject.reminder.NoteItem;
import com.example.uefi.seniorproject.reminder.Symptom_Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by palida on 09-Mar-18.
 */

public class InternalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Reminder";
    private static final int VERSION =1;
    private static InternalDatabaseHelper internalDatabaseHelper;
    private SQLiteDatabase database;

    private InternalDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);

    }

    public static synchronized InternalDatabaseHelper getInstance(Context c){
        if(internalDatabaseHelper==null){
            internalDatabaseHelper = new InternalDatabaseHelper(c.getApplicationContext());
        }
        return internalDatabaseHelper;
    }

    public void open() {
        this.database = internalDatabaseHelper.getWritableDatabase();
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_notes = "CREATE TABLE notes (" +
                "id_note INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "day INTEGER, "+
                "month INTEGER, "+
                "year INTEGER, "+
                "comment TEXT "+
                ")";
        sqLiteDatabase.execSQL(sql_notes);

        String sql_symptom_notes = "CREATE TABLE symptom_notes (" +
                "id_symptom_note INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "symptom TEXT, "+
                "id_note INTEGER "+
                ")";
        sqLiteDatabase.execSQL(sql_symptom_notes);

        String sql_alerts = "CREATE TABLE alerts (" +
                "id_alert INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "medicine_name TEXT, "+
                "medicine_num INTEGER, "+
                "breakfast INTEGER, "+
                "lunch INTEGER, "+
                "dinner INTEGER, "+
                "bed INTEGER, "+
                "before INTEGER, "+
                "after INTEGER, "+
                "is_alert INTEGER "+
                ")";
        sqLiteDatabase.execSQL(sql_alerts);

        String sql_settings = "CREATE TABLE settings (" +
                "id_setting INTEGER PRIMARY KEY, "+
                "breakfast_hour INTEGER, "+
                "breakfast_minute INTEGER, "+
                "lunch_hour INTEGER, "+
                "lunch_minute INTEGER, "+
                "dinner_hour INTEGER, "+
                "dinner_minute INTEGER, "+
                "bed_hour INTEGER, "+
                "bed_minute INTEGER, " +
                "vibrate INTEGER, " +
                "sound INTEGER " +
                ")";
        sqLiteDatabase.execSQL(sql_settings);

        ContentValues values = new ContentValues();
        values.put("id_setting", 1);
        values.put("breakfast_hour", 7);
        values.put("breakfast_minute",00);
        values.put("lunch_hour",12);
        values.put("lunch_minute",00);
        values.put("dinner_hour",17);
        values.put("dinner_minute",00);
        values.put("bed_hour",22);
        values.put("bed_minute",00);
        values.put("vibrate",1);
        values.put("sound",1);

        // insert row
        long alert_id = sqLiteDatabase.insert("settings", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS symptom_notes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS alerts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS settings");
        // create new tables
        onCreate(sqLiteDatabase);
    }

    //create
    public void createNote(int day,int month,int year,String comment,ArrayList<String> symptom) {
        ContentValues values = new ContentValues();
        values.put("day", day);
        values.put("month", month);
        values.put("year", year);
        values.put("comment",comment);

        // insert row
        long note_id = database.insert("notes", null, values);
        createSymptom(symptom,(int)note_id);
    }

    //create
    public void createSymptom(ArrayList<String> symptom,int note_id) {
        for(int i =0;i<symptom.size();i++) {
            ContentValues values = new ContentValues();
            values.put("symptom", symptom.get(i));
            values.put("id_note", note_id);

            // insert row
            long symptom_id = database.insert("symptom_notes", null, values);
        }
    }

    public ArrayList readAllNote(){
        ArrayList notes = new ArrayList();
        String sql = "SELECT * FROM notes"+" ORDER BY year DESC, month DESC, day DESC, id_note DESC";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        String lastDate = "";

        while (!cursor.isAfterLast()) {
            String monthS = "";
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            if(month==1){
                monthS ="มกราคม";
            }else if (month==2){
                monthS = "กุมภาพันธ์";
            }else if (month==3){
                monthS = "มีนาคม";
            }else if (month==4){
                monthS = "เมษายน";
            }else if (month==5){
                monthS = "พฤษภาคม";
            }else if (month==6){
                monthS = "มิถุนายน";
            }else if (month==7){
                monthS = "กรกฎาคม";
            }else if (month==8){
                monthS = "สิงหาคม";
            }else if (month==9){
                monthS = "กันยายน";
            }else if (month==10){
                monthS = "ตุลาคม";
            }else if (month==11){
                monthS = "พฤศจิกายน";
            }else if (month==12){
                monthS = "ธันวาคม";
            }
            String date = ""+cursor.getInt(cursor.getColumnIndex("day"))+ " "+
                    monthS + " " +
                    cursor.getInt(cursor.getColumnIndex("year"));
            if(lastDate.equals(date)){
            }else {
                notes.add(
                        new DayItem(date)
                );
            }
            lastDate = date;
            ArrayList symp = readSymptom(cursor.getInt(cursor.getColumnIndex("id_note")));
            Collections.sort(symp, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
            String note = "";
            for(int i = 0; i<symp.size(); i++){
                if(i!=symp.size()-1)
                    note +=""+symp.get(i) +" \n";
                else
                    note +=""+symp.get(i);
            }
            String comment = cursor.getString(cursor.getColumnIndex("comment"));
            if(!comment.equals("") && !note.equals("")) {
                note += "\n" + comment;
            }else{
                note += comment;
            }
            notes.add(
                    new NoteItem(note, cursor.getInt(cursor.getColumnIndex("id_note")))
            );

            cursor.moveToNext();
        }
        cursor.close();
        return notes;
    }

    //read call by readAllNote
    public ArrayList readSymptom(int id_note){

        ArrayList  symptom_note = new ArrayList();
        String sql = "SELECT * FROM symptom_notes "+
                "WHERE id_note ='"+id_note+"'";

        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            symptom_note.add(
                    cursor.getString(cursor.getColumnIndex("symptom")
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return symptom_note;
    }

    public ArrayList<ChoiceItem> getChoiceItem(int id_note,ArrayList<ChoiceItem> listNew){
        ArrayList<ChoiceItem> choice_list = new ArrayList();
        ArrayList<String> list = new ArrayList();
        String sql = "SELECT * FROM symptom_notes "+
                "WHERE id_note ='"+id_note+"'";

        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("symptom")));
//            choice_list.add(new ChoiceItem(cursor.getString(cursor.getColumnIndex("symptom")), false));
            cursor.moveToNext();
        }
        cursor.close();
        for(int i = 0;i<listNew.size();i++){
            for(int j =0;j< list.size();j++) {
                if(listNew.get(i).getText().equals(list.get(j))){
                    choice_list.add(new ChoiceItem(listNew.get(i).getText(), true));
                    break;
                }else{
                    if(j==list.size()-1) {
                        choice_list.add(new ChoiceItem(listNew.get(i).getText(), false));
                    }
                }
            }
        }
        if(list.size()==0) choice_list = listNew;
        return choice_list;
    }

    public String[] getComment(int id_note){
        String comment[] = new String[4];
        String sql = "SELECT * FROM notes "+
                "WHERE id_note ='"+id_note+"'";

        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            comment[0] = cursor.getInt(cursor.getColumnIndex("day"))+"";
            comment[1] = cursor.getInt(cursor.getColumnIndex("month"))+"";
            comment[2] = cursor.getInt(cursor.getColumnIndex("year"))+"";
            comment[3] = cursor.getString(cursor.getColumnIndex("comment"));
            cursor.moveToNext();
        }
        cursor.close();
        return comment;
    }

    public void updateNote(int id_note,int day, int month,int year,String comment,ArrayList<String> symptom){
        ContentValues values = new ContentValues();
        values.put("day",day);
        values.put("month",month);
        values.put("year",year);
        values.put("comment",comment);
        database.update("notes", values, "id_note="+id_note, null);

        updateSymptom(id_note,symptom);
    }

    public void updateSymptom(int id_note,ArrayList<String> newS){
        ArrayList<String>  oldS = readSymptom(id_note);

        ArrayList<String>  toAdd = new ArrayList();
        ArrayList<String>  toDel = new ArrayList();

        Set<String> setNewS = new HashSet<String>();
        setNewS.addAll(newS);

        Set<String> setOldS = new HashSet<String>();
        setOldS.addAll(oldS);

        ArrayList<String> common = new ArrayList<String>(newS);
        common.retainAll(oldS);

        Set<String> setCommon = new HashSet<String>();
        setCommon.addAll(common);

        setNewS.removeAll(setCommon);
        setOldS.removeAll(setCommon);


        toAdd.addAll(setNewS);
        toDel.addAll(setOldS);
        createSymptom(toAdd,id_note);
        deleteSymptom(toDel,id_note);
    }

    public void deleteNote(int id_note){
        database.delete("notes", "id_note" + "='" + id_note+"'", null);
        database.delete("symptom_notes", "id_note" + "='" + id_note +"'", null);
    }

    public void deleteSymptom(ArrayList symptom,int id_note){
        for (int i = 0;i<symptom.size();i++){
            database.delete("symptom_notes", "id_note" + "='" + id_note +"' AND symptom "+ "='"+ symptom.get(i)+"'" , null);
        }
    }

    public int createAlert(String name,int num,int breakfast,int lunch,int dinner,int bed,int before, int after,int isAlert) {
        ContentValues values = new ContentValues();
        values.put("medicine_name", name);
        values.put("medicine_num", num);
        values.put("breakfast", breakfast);
        values.put("lunch",lunch);
        values.put("dinner",dinner);
        values.put("bed",bed);
        values.put("before",before);
        values.put("after",after);
        values.put("is_alert",isAlert);

        // insert row
        long alert_id = database.insert("alerts", null, values);
        return (int) alert_id;
    }

    public ArrayList readAllAlert(){
        ArrayList alerts = new ArrayList();
        ArrayList temp = new ArrayList();

        alerts.add(new DayItem("เช้า"));
        temp = readAllAlertBy("breakfast");
        if(temp.size()==0){
            alerts.add(new EmptyItem());
        }else
            alerts.addAll(temp);

        alerts.add(new DayItem("กลางวัน"));
        temp = readAllAlertBy("lunch");
        if(temp.size()==0){
            alerts.add(new EmptyItem());
        }else
            alerts.addAll(temp);


        alerts.add(new DayItem("เย็น"));
        temp = readAllAlertBy("dinner");
        if(temp.size()==0){
            alerts.add(new EmptyItem());
        }else
            alerts.addAll(temp);

        alerts.add(new DayItem("ก่อนนอน"));
        temp = readAllAlertBy("bed");
        if(temp.size()==0){
            alerts.add(new EmptyItem());
        }else
            alerts.addAll(temp);

        // test
//        String sql = "SELECT * FROM alerts";
//        Cursor cursor = database.rawQuery(sql,null);
//        cursor.moveToFirst();
//
//        while (!cursor.isAfterLast()) {
//            alerts.add(new NoteItem(
//                    cursor.getString(cursor.getColumnIndex("medicine_name")),
//                    cursor.getInt(cursor.getColumnIndex("id_alert")),
//                    cursor.getInt(cursor.getColumnIndex("medicine_num")),
//                    "test"
//            ));
//
//            cursor.moveToNext();
//        }
//        cursor.close();
        // test

        return alerts;
    }

    public ArrayList readAllAlertBy(String column){
        ArrayList times = new ArrayList();
        String sql = "SELECT * FROM alerts "+"WHERE "+ column + " = 1" +" ORDER BY medicine_name";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            times.add(new NoteItem(
                    cursor.getString(cursor.getColumnIndex("medicine_name")),
                    cursor.getInt(cursor.getColumnIndex("id_alert")),
                    cursor.getInt(cursor.getColumnIndex("medicine_num")),
                    column
            ));

            cursor.moveToNext();
        }
        cursor.close();
        return times;
    }

    public ArrayList readAllAlertByIFAlertBefore(String column){
        ArrayList times = new ArrayList();
        String sql = "SELECT * FROM alerts "+"WHERE "+ column + " = 1" +
                " AND is_alert ='"+1+"'"+ " AND before ='"+1+"'" +" ORDER BY medicine_name";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            times.add(new NoteItem(
                    cursor.getString(cursor.getColumnIndex("medicine_name")),
                    cursor.getInt(cursor.getColumnIndex("id_alert")),
                    cursor.getInt(cursor.getColumnIndex("medicine_num")),
                    column
            ));

            cursor.moveToNext();
        }
        cursor.close();
        return times;
    }

    public ArrayList readAllAlertByIFAlertAfter(String column){
        ArrayList times = new ArrayList();
        String sql = "SELECT * FROM alerts "+"WHERE "+ column + " = 1" +
                " AND is_alert ='"+1+"'"+ " AND after ='"+1+"'" +" ORDER BY medicine_name";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            times.add(new NoteItem(
                    cursor.getString(cursor.getColumnIndex("medicine_name")),
                    cursor.getInt(cursor.getColumnIndex("id_alert")),
                    cursor.getInt(cursor.getColumnIndex("medicine_num")),
                    column
            ));

            cursor.moveToNext();
        }
        cursor.close();
        return times;
    }

    public ArrayList readAllAlertByIFAlert(String column){
        ArrayList times = new ArrayList();
        String sql = "SELECT * FROM alerts "+"WHERE "+ column + " = 1" +
                " AND is_alert ='"+1+"'" +" ORDER BY medicine_name";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            times.add(new NoteItem(
                    cursor.getString(cursor.getColumnIndex("medicine_name")),
                    cursor.getInt(cursor.getColumnIndex("id_alert")),
                    cursor.getInt(cursor.getColumnIndex("medicine_num")),
                    column
            ));

            cursor.moveToNext();
        }
        cursor.close();
        return times;
    }

    public ArrayList readAlert(int id_alert){

        ArrayList  alert = new ArrayList();
        String sql = "SELECT * FROM alerts "+
                "WHERE id_alert ='"+id_alert+"'";

        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            alert.add(cursor.getString(cursor.getColumnIndex("medicine_name")));
            alert.add(cursor.getInt(cursor.getColumnIndex("medicine_num")));
            alert.add(cursor.getInt(cursor.getColumnIndex("breakfast")));
            alert.add(cursor.getInt(cursor.getColumnIndex("lunch")));
            alert.add(cursor.getInt(cursor.getColumnIndex("dinner")));
            alert.add(cursor.getInt(cursor.getColumnIndex("bed")));
            alert.add(cursor.getInt(cursor.getColumnIndex("before")));
            alert.add(cursor.getInt(cursor.getColumnIndex("after")));
            alert.add(cursor.getInt(cursor.getColumnIndex("is_alert")));
            cursor.moveToNext();
        }
        cursor.close();
        return alert;
    }

    public void updateAlert(int id_alert,String name,int num,int breakfast,int lunch,int dinner,int bed,int before, int after,int isAlert){
        ContentValues values = new ContentValues();
        values.put("medicine_name", name);
        values.put("medicine_num", num);
        values.put("breakfast", breakfast);
        values.put("lunch",lunch);
        values.put("dinner",dinner);
        values.put("bed",bed);
        values.put("before",before);
        values.put("after",after);
        values.put("is_alert",isAlert);
        database.update("alerts", values, "id_alert="+id_alert, null);
    }

    public void deleteAlert(int id_alert,String name,int num,int breakfast,int lunch,int dinner,int bed,int before, int after,int isAlert){
        if( breakfast==0 && lunch == 0 && dinner ==0 && bed ==0)
            database.delete("alerts", "id_alert" + "='" + id_alert+"'", null);
        else
            updateAlert(id_alert,name,num,breakfast,lunch,dinner,bed,before,after,isAlert);
    }

    public void createSetting() {
        ContentValues values = new ContentValues();
        values.put("id_setting", 1);
        values.put("breakfast_hour", 7);
        values.put("breakfast_minute",00);
        values.put("lunch_hour",12);
        values.put("lunch_minute",00);
        values.put("dinner_hour",17);
        values.put("dinner_minute",00);
        values.put("bed_hour",22);
        values.put("bed_minute",00);

        // insert row
        long alert_id = database.insert("settings", null, values);
    }

    public void updateSetting(int breakfast_hour,int breakfast_minute,int lunch_hour,int lunch_minute,int dinner_hour,int dinner_minute, int bed_hour,int bed_minute,int vibrate,int sound){
        ContentValues values = new ContentValues();
        values.put("breakfast_hour", breakfast_hour);
        values.put("breakfast_minute", breakfast_minute);
        values.put("lunch_hour",lunch_hour);
        values.put("lunch_minute",lunch_minute);
        values.put("dinner_hour",dinner_hour);
        values.put("dinner_minute",dinner_minute);
        values.put("bed_hour",bed_hour);
        values.put("bed_minute",bed_minute);
        values.put("vibrate",vibrate);
        values.put("sound",sound);
        database.update("settings", values, "id_setting="+1, null);
    }

    public ArrayList<Integer> readSetting(){

        ArrayList<Integer>  setting = new ArrayList<Integer>();
        String sql = "SELECT * FROM settings "+
                "WHERE id_setting ='"+1+"'";


        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            setting.add(cursor.getInt(cursor.getColumnIndex("breakfast_hour")));
            setting.add(cursor.getInt(cursor.getColumnIndex("breakfast_minute")));
            setting.add(cursor.getInt(cursor.getColumnIndex("lunch_hour")));
            setting.add(cursor.getInt(cursor.getColumnIndex("lunch_minute")));
            setting.add(cursor.getInt(cursor.getColumnIndex("dinner_hour")));
            setting.add(cursor.getInt(cursor.getColumnIndex("dinner_minute")));
            setting.add(cursor.getInt(cursor.getColumnIndex("bed_hour")));
            setting.add(cursor.getInt(cursor.getColumnIndex("bed_minute")));
            setting.add(cursor.getInt(cursor.getColumnIndex("vibrate")));
            setting.add(cursor.getInt(cursor.getColumnIndex("sound")));

            cursor.moveToNext();
        }
        cursor.close();
        return setting;
    }

}
