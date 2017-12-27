package com.example.uefi.seniorproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by UEFI on 27/12/2560.
 */

public class HospitalItemFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    LatLng location;
    String name,address,phone;
    TextView textName,textAddress,textPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_item, container, false);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.itemMap);
        mapFragment.getMapAsync(this);

        Bundle extraBundle = getArguments();
        if(!extraBundle.isEmpty()){
            name = extraBundle.getString("itemHospital");
            location = new LatLng(extraBundle.getDouble("lat"),extraBundle.getDouble("lng"));
            address = extraBundle.getString("address");
            phone = extraBundle.getString("phone");
        }

        textName = (TextView) view.findViewById(R.id.textName);
        textAddress = (TextView)view.findViewById((R.id.textAddress));
        textPhone = (TextView)view.findViewById(R.id.textPhone) ;
        textName.setText(name);
        textAddress.setText(address);
        textPhone.setText(phone);

        return view;
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
