package com.example.uefi.seniorproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HospitalActivity extends AppCompatActivity {
    ListView hospitalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hospitalView = (ListView) findViewById(R.id.hospital_list);
//        DBHelperDAO dbHelperDAO = new DBHelperDAO(getApplicationContext());
        DBHelperDAO dbHelperDAO = DBHelperDAO.getInstance(this);
        dbHelperDAO.open();
        ArrayList<String> list = dbHelperDAO.getAllList();
        dbHelperDAO.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,list);
        hospitalView.setAdapter(adapter);
    }
}
