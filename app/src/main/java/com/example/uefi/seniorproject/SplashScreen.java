package com.example.uefi.seniorproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SplashScreen extends AppCompatActivity {
    DBHelperDAO dbHelperDAO;
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dbHelperDAO = DBHelperDAO.getInstance(this);
        dbHelperDAO.open();

        Singleton single = Singleton.getInstance();
        single.setDict(dbHelperDAO.getLexitron());
        single.setStopword(dbHelperDAO.getStopword());
        single.setAllSymptoms(dbHelperDAO.getAllSymptoms());
        single.setMainSymptoms(dbHelperDAO.getMainSymptoms());
        single.setDiseaseNameDefault(dbHelperDAO.getDiseaseName());
//        Toast.makeText(this, "GridView Item: ", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
