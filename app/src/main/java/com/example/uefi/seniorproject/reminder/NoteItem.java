package com.example.uefi.seniorproject.reminder;

/**
 * Created by palida on 31-Mar-18.
 */

public class NoteItem {
    public String text;
    public int note_id;

    public NoteItem(String text,int note_id){
        this.text = text;
        this.note_id = note_id;
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
}