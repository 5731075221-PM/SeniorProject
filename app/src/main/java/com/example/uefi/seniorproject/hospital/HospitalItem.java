package com.example.uefi.seniorproject.hospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.uefi.seniorproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class HospitalItem extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    LatLng location;
    String name,address,phone;
    TextView textName,textAddress,textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.itemMap);
        mapFragment.getMapAsync(this);

        Intent intentExtra = getIntent();
        Bundle extraBundle = intentExtra.getExtras();
        if(!extraBundle.isEmpty()){
            name = extraBundle.getString("itemHospital");
            location = new LatLng(extraBundle.getDouble("lat"),extraBundle.getDouble("lng"));
            address = extraBundle.getString("address");
            phone = extraBundle.getString("phone");
        }

        textName = (TextView) findViewById(R.id.textName);
        textAddress = (TextView)findViewById((R.id.textAddress));
        textPhone = (TextView)findViewById(R.id.textPhone) ;
        textName.setText(name);
        textAddress.setText(address);
        textPhone.setText(phone);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(null));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                TextView title = ((TextView) infoWindow.findViewById(R.id.textViewName));
                title.setText(marker.getTitle());
                
//                TextView snippet = ((TextView) infoWindow.findViewById(R.id.textViewSnippet));
//                snippet.setText(marker.getSnippet());
//
//                ImageView imageView = (ImageView) infoWindow.findViewById(R.id.imageView);
//                imageView.setImageResource(R.drawable.ic_city);
//                if ("My Home".equals(marker.getTitle())) {
//                    imageView.setImageResource(R.drawable.ic_home);
//                }
                return infoWindow;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

}
