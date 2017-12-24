package com.example.uefi.seniorproject.hospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;

public class HospitalActivity extends AppCompatActivity {
    ListView hospitalView;
    ArrayList<String> nameList;
    ArrayList<Pair<Double,Double>> latlngList;
//    ArrayList<>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hospitalView = (ListView) findViewById(R.id.hospital_list);
//      DBHelperDAO dbHelperDAO = new DBHelperDAO(getApplicationContext());

        DBHelperDAO dbHelperDAO = DBHelperDAO.getInstance(this);
        dbHelperDAO.open();

        nameList = dbHelperDAO.getNameHospital();
        latlngList = dbHelperDAO.getLatLng();

        dbHelperDAO.close();

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), nameList/*, resId*/);
        hospitalView.setAdapter(adapter);
        hospitalView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                intent = new Intent(getApplicationContext(), HospitalItem.class);
                Bundle bundle = new Bundle();
                bundle.putString("itemHospital", nameList.get(i));
                bundle.putDouble("lat",latlngList.get(i).first);
                bundle.putDouble("lng",latlngList.get(i).second);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                android.R.layout.simple_list_item_1,list);
//        hospitalView.setAdapter(adapter);
    }
}
