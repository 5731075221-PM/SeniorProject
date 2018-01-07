package com.example.uefi.seniorproject;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class InitailFont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/THSarabunNew.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
