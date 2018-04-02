package com.example.uefi.seniorproject.reminder;

import java.util.ArrayList;

/**
 * Created by palida on 31-Mar-18.
 */

public class Symptom_Note {

    int id;
    String symptom;
    int id_note;

    public Symptom_Note(){
    }

    public Symptom_Note(int id, String symptom, int id_note){
        this.id = id;
        this.symptom = symptom;
        this.id_note = id_note;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_note() {
        return id_note;
    }

    public void setId_note(int id_note) {
        this.id_note = id_note;
    }
}
