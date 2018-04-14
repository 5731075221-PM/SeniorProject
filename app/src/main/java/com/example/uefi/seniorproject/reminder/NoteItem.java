package com.example.uefi.seniorproject.reminder;

/**
 * Created by palida on 31-Mar-18.
 */

public class NoteItem {
    public String text;
    public int note_id;
    public int number;
    public String time;
    public int hour,minute,day,month,year,isAlert;
    //note
    public NoteItem(String text,int note_id){
        this.text = text;
        this.note_id = note_id;
        this.number = 0;
        this.time = "none";
        this.day =0;
        this.month =0;
        this.year = 0;
        this.hour = 0;
        this.minute = 0;
        this.isAlert = 0;
    }
    //appointment
    public NoteItem(int id,int day,int month,int year,int hour,int minute,String hospital,int isAlert){
        this.note_id = id;
        this.number = 0;
        this.time = "none";
        this.text = hospital;
        this.day =day;
        this.month =month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.isAlert = isAlert;
    }
    //alert
    public NoteItem(String text,int note_id,int number,String time){
        this.text = text;
        this.note_id = note_id;
        this.number = number;
        this.time = time;
        this.day =0;
        this.month =0;
        this.year = 0;
        this.hour = 0;
        this.minute = 0;
        this.isAlert = 0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
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