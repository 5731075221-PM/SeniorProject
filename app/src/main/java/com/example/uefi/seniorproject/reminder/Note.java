package com.example.uefi.seniorproject.reminder;

import java.util.ArrayList;

/**
 * Created by palida on 31-Mar-18.
 */

public class Note {

    int id,day,month,year;
    String comment;
    ArrayList<Symptom_Note> symptom_note;

    public Note(){
    }

    public Note(int id, int day, int month,int year , String comment,ArrayList<Symptom_Note> symptom_note){
        this.id = id;
        this.day= day;
        this.month = month;
        this.year = year;
        this.comment = comment;
        this.symptom_note = symptom_note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Symptom_Note> getSymptom_note() {
        return symptom_note;
    }

    public void setSymptom_note(ArrayList<Symptom_Note> symptom_note) {
        this.symptom_note = symptom_note;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
