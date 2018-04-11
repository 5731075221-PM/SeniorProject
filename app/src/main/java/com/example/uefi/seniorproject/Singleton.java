package com.example.uefi.seniorproject;

import java.util.ArrayList;

/**
 * Created by UEFI on 9/4/2561.
 */

public class Singleton {
    private static Singleton instance;

    private ArrayList<String> dictList, stopword;

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
