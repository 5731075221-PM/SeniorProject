package com.example.uefi.seniorproject.firstaid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by palida on 01-Feb-18.
 */

public class Firstaid {
    private int firstaid_id;
    private String firstaid_name;
    private String firstaid_detail;

    Map<List<String>,Map<Integer,String>> detail = new HashMap<List<String>,Map<Integer,String>>();

    public Firstaid (){
        this.firstaid_name="doo";
    }

    public String getFirstaid_name() {
        return firstaid_name;
    }

    public void setFirstaid_id(int firstaid_id) {
        this.firstaid_id = firstaid_id;
    }

    public void setFirstaid_name(String firstaid_name) {
        this.firstaid_name = firstaid_name;
    }

    public void setFirstaid_detail(String firstaid_detail) {
        this.firstaid_detail = firstaid_detail;
    }

    public void setDetail(Map<List<String>, Map<Integer, String>> detail) {
        this.detail = detail;
    }
}