package com.example.uefi.seniorproject.hospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    LatLng sydney = new LatLng(13.741163702, 100.534331196);
    LatLng myHome = new LatLng(13.7373388, 100.56059759999994);
    String text;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_item);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.itemMap);
        mapFragment.getMapAsync(this);

        Intent intentExtra = getIntent();
        Bundle extraBundle = intentExtra.getExtras();
        if(!extraBundle.isEmpty()){
            text = extraBundle.getString("itemHospital");
        }

        textView1 = (TextView) findViewById(R.id.textItem);
        textView1.setText(text);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng myHome = new LatLng(13.7373388, 100.56059759999994);
        mMap.addMarker(new MarkerOptions().position(myHome).title("My Home").snippet("Hello my home"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myHome, 100));

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
                
                TextView snippet = ((TextView) infoWindow.findViewById(R.id.textViewSnippet));
                snippet.setText(marker.getSnippet());

                ImageView imageView = (ImageView) infoWindow.findViewById(R.id.imageView);
                imageView.setImageResource(R.drawable.ic_city);
                if ("My Home".equals(marker.getTitle())) {
                    imageView.setImageResource(R.drawable.ic_home);
                }
                return infoWindow;
            }
        });
    }
}
