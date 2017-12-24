package com.example.uefi.seniorproject.hospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

import org.w3c.dom.Text;

public class HospitalItem extends AppCompatActivity {
    String text;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_item);

        Intent intentExtra = getIntent();
        Bundle extraBundle = intentExtra.getExtras();
        if(!extraBundle.isEmpty()){
            text = extraBundle.getString("itemHospital");
        }

        textView1 = (TextView) findViewById(R.id.textItem);
        textView1.setText(text);
    }
}
