package com.example.uefi.seniorproject.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.uefi.seniorproject.reminder.ChoiceItem;
import com.example.uefi.seniorproject.reminder.DayItem;
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS symptom_notes");

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
}
