package com.example.uefi.seniorproject.fragment;

/**
 * Created by UEFI on 16/3/2561.
 */

public class Disease {
    private String name,type;

    public Disease(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
