package com.example.uefi.seniorproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HospitalMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    SupportMapFragment mapFragment;
    DBHelperDAO dbHelperDAO;
    ArrayList<Hospital> hospitalList;
    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    Marker mMarker;
    double lat, lng, avgLat = 0, avgLng = 0;
    Bitmap icon;
    Paint paint = new Paint();
    ColorFilter filter;
    Canvas canvas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_map, container, false);

        if (mMap != null) {
            if (mMarker != null)
                mMarker.remove();

            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            mMarker.setTitle("คุณอยู่ที่นี่");
            mMarker.setSnippet("-1");
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
        }

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.hospitalMap);
        mapFragment.getMapAsync(this);

//        Bundle extraBundle = getArguments();
//        if (!extraBundle.isEmpty()) {
            dbHelperDAO = DBHelperDAO.getInstance(getActivity());
            dbHelperDAO.open();

//            if (extraBundle.getString("type") == "0") {
                hospitalList = dbHelperDAO.getHospital();
//            }
//        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_map_pin);
        icon = Bitmap.createBitmap(drawable.getIntrinsicWidth()+50, drawable.getIntrinsicHeight()+50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
//        icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_local_hospital_white).copy(Bitmap.Config.ARGB_8888, true);
//        filter = new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRubyRed), PorterDuff.Mode.SRC_IN);
//        paint.setColorFilter(filter);
//        canvas = new Canvas(icon);
//        canvas.drawBitmap(icon, 0, 0, paint);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        statusCheck();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                getActivity().sendBroadcast(poke);
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);

            if (location != null)
                onLocationChanged(location);
            else
                location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getActivity().getBaseContext(), "Location can't be retrieved",
                        Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getBaseContext(), "No Provider Found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void statusCheck() {
        System.out.println("statusCheck");
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
 
        if (!hospitalList.isEmpty()) {
            for (int i = 0; i < hospitalList.size(); i++) {
//                avgLat += hospitalList.get(i).getLat();
//                avgLng += hospitalList.get(i).getLng();

                Drawable drawable = getResources().getDrawable(R.drawable.ic_hospital_cross);
                Bitmap icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(icon);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

//                Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_local_hospital_white).copy(Bitmap.Config.ARGB_8888, true);
//                Paint paint = new Paint();
//                ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), R.color.colorMacawBlueGreen), PorterDuff.Mode.SRC_IN);
//                paint.setColorFilter(filter);
//                Canvas canvas = new Canvas(icon);
//                canvas.drawBitmap(icon, 0, 0, paint);

                mMap.addMarker(new MarkerOptions().position(new LatLng(hospitalList.get(i).getLat(), hospitalList.get(i).getLng()))
                        .title(hospitalList.get(i).getName()).snippet(i + "")).setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            }
//            avgLat = avgLat / hospitalList.size();
//            avgLng = avgLng / hospitalList.size();
//            mMap.addMarker(new MarkerOptions().position(new LatLng(avgLat,avgLng))
//                    .title("You're here").snippet(null));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(avgLat, avgLng), 12));
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(Integer.parseInt(marker.getSnippet()) == -1) return;
                HospitalItemFragment fragment = new HospitalItemFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", hospitalList.get(Integer.parseInt(marker.getSnippet())).getName());
                bundle.putDouble("lat", hospitalList.get(Integer.parseInt(marker.getSnippet())).getLat());
                bundle.putDouble("lng", hospitalList.get(Integer.parseInt(marker.getSnippet())).getLng());
                bundle.putString("address", hospitalList.get(Integer.parseInt(marker.getSnippet())).getAddress());
                bundle.putString("phone", hospitalList.get(Integer.parseInt(marker.getSnippet())).getPhone());
                bundle.putString("website", hospitalList.get(Integer.parseInt(marker.getSnippet())).getWebsite());
                bundle.putString("type", hospitalList.get(Integer.parseInt(marker.getSnippet())).getType());
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

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

                return infoWindow;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();
        lng = location.getLongitude();
        System.out.println("Lat = " + lat);
        System.out.println("Lng = " + lng);

        if (mMap != null) {
            if (mMarker != null)
                mMarker.remove();

            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            mMarker.setTitle("คุณอยู่ที่นี่");
            mMarker.setSnippet("-1");
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
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