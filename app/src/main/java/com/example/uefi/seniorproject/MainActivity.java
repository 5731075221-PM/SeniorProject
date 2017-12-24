package com.example.uefi.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.uefi.seniorproject.hospital.HospitalActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, HospitalActivity.class);
        startActivity(intent);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = database.getReference();
//        final DatabaseReference HospitalRef = myRef.child("hospitalList");

        // Read from the database
//        HospitalRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                hos = (ArrayList<Hospital>) dataSnapshot.getValue();
//                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
//                    String name = (String) messageSnapshot.child("name").getValue();
//                    String address = (String) messageSnapshot.child("address").getValue();
//                    Log.d("address = ", address);
//                }

//                TextView testText = (TextView) findViewById(R.id.test);
//                Map map = (Map)dataSnapshot.getValue();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

    }
}
