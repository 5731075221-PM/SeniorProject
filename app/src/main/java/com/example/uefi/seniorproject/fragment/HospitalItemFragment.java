package com.example.uefi.seniorproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.example.uefi.seniorproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

/**
 * Created by UEFI on 27/12/2560.
 */

public class HospitalItemFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    String serverKey = "AIzaSyBfgUci7cUhr0q2jqFgvuLUPrdCDhDfsuU";
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Double lat, lng;
    LatLng location;
    String name, address, phone, website;
    TextView textName, textAddress, textPhone, textDrivingDistance, textDrivingTime, topicTextAddress, topicTextPhone;
    Button buttonPhone, buttonWeb;
    LocationManager locationManager;
    LocationListener locationListener;
    String provider;
    Boolean isGPS = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_item, container, false);

        final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");
        final Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");

        System.out.println("onCreateView");
//        if (mMap != null) {
//            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
//        }

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.itemMap);
        mapFragment.getMapAsync(this);

        topicTextAddress = (TextView) view.findViewById(R.id.topicTextAddress);
        topicTextPhone = (TextView) view.findViewById(R.id.topicTextPhone);
        textName = (TextView) view.findViewById(R.id.textName);
        textAddress = (TextView) view.findViewById((R.id.textAddress));
        textPhone = (TextView) view.findViewById(R.id.textPhone);
        textDrivingDistance = (TextView) view.findViewById(R.id.drivingDistance);
        textDrivingTime = (TextView) view.findViewById(R.id.drivingTime);

        topicTextAddress.setTypeface(tf);
        topicTextPhone.setTypeface(tf);
        textName.setText(name);
        textName.setTypeface(tf);
        textAddress.setText(address);
        textPhone.setText(phone);
        textAddress.setTypeface(tf2);
        textPhone.setTypeface(tf2);

        buttonPhone = (Button) view.findViewById(R.id.callPhone);
        buttonWeb = (Button) view.findViewById(R.id.linkWebsite);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] tmp = textPhone.getText().toString().split("-");
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("ยืนยันการโทรออก?");
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callActivity = new Intent(Intent.ACTION_CALL);
                        callActivity.setData(Uri.parse("tel:0876739362"));
//                        callActivity.setData(Uri.parse("tel:"+tmp[0]+tmp[1]+tmp[2]));
                        startActivity(callActivity);
                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Web = ", website);
                Intent linkActivity = new Intent(Intent.ACTION_VIEW);
                linkActivity.setData(Uri.parse("http://" + website));
                startActivity(linkActivity);
            }
        });
        if (isGPS) {
            System.out.println("GPS = " + isGPS);
            GoogleDirection.withServerKey(serverKey)
                    .from(new LatLng(lat, lng))
                    .to(new LatLng(location.latitude, location.longitude))
                    .language(Language.THAI)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            System.out.println(direction.getStatus());
                            if (direction.isOK()) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                System.out.println("Route = " + leg.getDistance().getText() + " " + leg.getDuration().getText());
                                textDrivingDistance.setText(leg.getDistance().getText());
                                textDrivingTime.setText(leg.getDuration().getText());
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                            System.out.println("Fail");
                        }
                    });
        }
        return view;
    }

    public void statusCheck() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {
            if (!provider.contains("gps")) { // if gps is disabled
                System.out.println("Provider");
                isGPS = false;
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                getActivity().sendBroadcast(poke);
            }

            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 500, 0, this);

            if (location != null) {
                isGPS = true;
                onLocationChanged(location);
            } else
                location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                isGPS = true;

                onLocationChanged(location);
            } else
                Toast.makeText(getActivity().getBaseContext(), "Location can't be retrieved",
                        Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity().getBaseContext(), "No Provider Found",
                    Toast.LENGTH_SHORT).show();
        }
        System.out.println("isGPS = " + isGPS);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extraBundle = getArguments();
        if (!extraBundle.isEmpty()) {
            name = extraBundle.getString("name");
            location = new LatLng(extraBundle.getDouble("lat"), extraBundle.getDouble("lng"));
            address = extraBundle.getString("address");
            phone = extraBundle.getString("phone");
            website = extraBundle.getString("website");
        }
        statusCheck();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(name).snippet(null));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 12));

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
    public void onLocationChanged(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
