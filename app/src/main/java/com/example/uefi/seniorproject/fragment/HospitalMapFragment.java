package com.example.uefi.seniorproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener listener;
    Marker mMarker;
    double lat, lng;
    double avgLat = 0;
    double avgLng = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_map, container, false);

        listener = new LocationListener() {
            public void onLocationChanged(Location loc) {
                Log.d("ChangeLo = ",loc+"");
                LatLng coordinate = new LatLng(loc.getLatitude(), loc.getLongitude());
                lat = loc.getLatitude();
                lng = loc.getLongitude();

                if (mMarker != null)
                    mMarker.remove();

                Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_location_on_white).copy(Bitmap.Config.ARGB_8888, true);;
                Paint paint = new Paint();
                ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRubyRed), PorterDuff.Mode.SRC_IN);
                paint.setColorFilter(filter);
                Canvas canvas = new Canvas(icon);
                canvas.drawBitmap(icon, 0, 0, paint);

                mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.hospitalMap);
        mapFragment.getMapAsync(this);

        Bundle extraBundle = getArguments();
        if (!extraBundle.isEmpty()) {
            dbHelperDAO = DBHelperDAO.getInstance(getActivity());
            dbHelperDAO.open();

            if (extraBundle.getString("type") == "0") {
                hospitalList = dbHelperDAO.getHospital();
            }
        }
        return view;
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void onResume() {
        super.onResume();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Log.d("isNetwork",isNetwork+"");
        Log.d("isNetworkGPS",isGPS+"");


//        if (isNetwork) {
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
//                    , 5000, 10, listener);
//            Location loc = lm.getLastKnownLocation(
//                    LocationManager.NETWORK_PROVIDER);
//            if (loc != null) {
//                lat = loc.getLatitude();
//                lng = loc.getLongitude();
//            }
//        }

        if (isGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }else buildAlertMessageNoGps();
    }

    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (!hospitalList.isEmpty()) {
            for (int i = 0; i < hospitalList.size(); i++) {
                avgLat += hospitalList.get(i).getLat();
                avgLng += hospitalList.get(i).getLng();

                Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_local_hospital_white).copy(Bitmap.Config.ARGB_8888, true);;
                Paint paint = new Paint();
                ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), R.color.colorMacawBlueGreen), PorterDuff.Mode.SRC_IN);
                paint.setColorFilter(filter);
                Canvas canvas = new Canvas(icon);
                canvas.drawBitmap(icon, 0, 0, paint);

                mMap.addMarker(new MarkerOptions().position(new LatLng(hospitalList.get(i).getLat(), hospitalList.get(i).getLng()))
                        .title(hospitalList.get(i).getName()).snippet(null)).setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            }
            avgLat = avgLat / hospitalList.size();
            avgLng = avgLng / hospitalList.size();
//            mMap.addMarker(new MarkerOptions().position(new LatLng(avgLat,avgLng))
//                    .title("You're here").snippet(null));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(avgLat, avgLng), 12));
        }

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

                Button navigationButton = (Button) infoWindow.findViewById(R.id.navigationButton);
                navigationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("ClickButton = ","Yes");
                    }
                });

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
}