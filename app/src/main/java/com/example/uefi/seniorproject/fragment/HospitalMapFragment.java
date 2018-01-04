package com.example.uefi.seniorproject.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by UEFI on 4/1/2561.
 */

public class HospitalMapFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    DBHelperDAO dbHelperDAO;
    ArrayList<Hospital> hospitalList;
    ArrayList<LatLng> latlngList;
    private GoogleMap mMap;
    LatLng location;
    String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_map, container, false);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.hospitalMap);
        mapFragment.getMapAsync(this);

        Bundle extraBundle = getArguments();
        if(!extraBundle.isEmpty()){
            dbHelperDAO = DBHelperDAO.getInstance(getActivity());
            dbHelperDAO.open();

            if(extraBundle.getString("type") == "0") {
                hospitalList = dbHelperDAO.getHospital();
            }
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        if(!hospitalList.isEmpty()) {
//            for (int i = 0; i < hospitalList.size(); i++) {
//                Log.d("TypeValue1 = ",hospitalList.get(i).getLat()+"");
//                Log.d("TypeValue2 = ",hospitalList.get(i).getLng()+"");
//                Log.d("TypeValue3 = ",hospitalList.get(i).getName()+"");
//                mMap.addMarker(new MarkerOptions().position(new LatLng(hospitalList.get(i).getLat(), hospitalList.get(i).getLng()))
//                        .title(hospitalList.get(i).getName()).snippet(null));
//            }
//        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(13.569253,100.835746)).title("123").snippet(null));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

//                TextView title = ((TextView) infoWindow.findViewById(R.id.textViewName));
//                title.setText(marker.getTitle());
//
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
}
