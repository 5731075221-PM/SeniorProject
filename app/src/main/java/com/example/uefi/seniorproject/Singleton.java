package com.example.uefi.seniorproject;

import com.example.uefi.seniorproject.fragment.Symptom;

import java.util.ArrayList;

/**
 * Created by UEFI on 9/4/2561.
 */

public class Singleton {
    private static Singleton instance;

    private ArrayList<String> dictList, stopword, diseaseNameDefault;
    private ArrayList<Symptom> allSymptoms, mainSymptoms;

    public ArrayList<String> getDiseaseNameDefault() {
        return diseaseNameDefault;
    }

    public void setDiseaseNameDefault(ArrayList<String> diseaseNameDefault) {
        System.out.println("AAAd"+diseaseNameDefault.size());
        this.diseaseNameDefault = diseaseNameDefault;
    }

    public ArrayList<Symptom> getAllSymptoms() {
        return allSymptoms;
    }

    public void setAllSymptoms(ArrayList<Symptom> allSymptoms) {
        System.out.println("AAA"+allSymptoms.size());
        this.allSymptoms = allSymptoms;
    }

    public ArrayList<Symptom> getMainSymptoms() {
        return mainSymptoms;
    }

    public void setMainSymptoms(ArrayList<Symptom> mainSymptoms) {
        System.out.println("AAAm"+mainSymptoms.size());
        this.mainSymptoms = mainSymptoms;
    }

    public ArrayList<String> getDict() {
        return dictList;
    }

    public void setDict(ArrayList<String> dict) {
        this.dictList = dict;
    }

    public ArrayList<String> getStopword() {
        return stopword;
    }

    public void setStopword(ArrayList<String> stopword) {
        this.stopword = stopword;
    }

    public static synchronized Singleton getInstance(){
        if(instance==null){
            instance=new Singleton();
        }
        return instance;
    }
}
