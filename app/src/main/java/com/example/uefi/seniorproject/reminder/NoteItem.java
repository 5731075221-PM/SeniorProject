package com.example.uefi.seniorproject.reminder;

/**
 * Created by palida on 31-Mar-18.
 */

public class NoteItem {
    public String text;
    public int note_id;
    public int number;
    public String time;

    public NoteItem(String text,int note_id){
        this.text = text;
        this.note_id = note_id;
        this.number = 0;
        this.time = "none";
    }

    public NoteItem(String text,int note_id,int number,String time){
        this.text = text;
        this.note_id = note_id;
        this.number = number;
        this.time = time;
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
}