package com.example.uefi.seniorproject.fragment;

/**
 * Created by UEFI on 22/1/2561.
 */

public class Symptom {
    private int id;
    private String word;
    private String parent;
    private String synonym;

    public Symptom() {
    }

    public Symptom(int id, String word, String parent, String synonym) {
        this.id = id;
        this.word = word;
        this.parent = parent;
        this.synonym = synonym;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }
}
