package com.example.uefi.seniorproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by UEFI on 27/12/2560.
 */

public class HospitalItemFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    String serverKey = "AIzaSyBfgUci7cUhr0q2jqFgvuLUPrdCDhDfsuU";
    DBHelperDAO dbHelperDAO;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Double lat = 0.0, lng = 0.0; //origin
    LatLng location; //destination
    String name, address, phone, website, type;
    TextView textName, textAddress, textType, textDrivingDistance, textDrivingTime, textNavigationButton, textPhoneButton;
    Button buttonPhone, buttonWeb, buttonNavigate;
    LocationManager locationManager;
    String provider;
    Boolean isGPS = false;
    String[] phonenum;
    int phoneno;
    String selected;
    ArrayList<TextView> textPhone = new ArrayList<>();
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_item, container, false);

        appBarLayout.setExpanded(true, true);

        final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");
        final Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.itemMap);
        mapFragment.getMapAsync(this);

//        topicTextAddress = (TextView) view.findViewById(R.id.topicTextAddress);
//        topicTextPhone = (TextView) view.findViewById(R.id.topicTextPhone);
        textType = (TextView) view.findViewById(R.id.textType);
        textName = (TextView) view.findViewById(R.id.textName);
        textName.requestFocus();
        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Web = ", website);
                Intent linkActivity = new Intent(Intent.ACTION_VIEW);
                linkActivity.setData(Uri.parse("http://" + website));
                startActivity(linkActivity);
            }
        });
        textAddress = (TextView) view.findViewById((R.id.textAddress));
        int[] rid = {R.id.textPhone, R.id.textPhone2, R.id.textPhone3, R.id.textPhone4};
        for (int i = 0; i < phoneno; i++) {
            final TextView phone = (TextView) view.findViewById(rid[i]);
//            phone.setTypeface(tf2);
            phone.setText(phonenum[i]);
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String[] tmp = phone.getText().toString().split("-");//textPhone.getText().toString().split("-");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("ยืนยันการโทรออก?");
                    if(tmp.length > 1) builder.setMessage("ติดต่อ " + tmp[0] + tmp[1] + tmp[2]);
                    else builder.setMessage("ติดต่อ " + tmp[0]);
                    builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent callActivity = new Intent(Intent.ACTION_CALL);
                            callActivity.setData(Uri.parse("tel:" + tmp[0] + tmp[1] + tmp[2]));
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
            textPhone.add(phone);
        }
//        textPhone = (TextView) view.findViewById(R.id.textPhone);
//        textPhone2 = (TextView) view.findViewById(R.id.textPhone2);
        textDrivingDistance = (TextView) view.findViewById(R.id.drivingDistance);
        textDrivingDistance.setTypeface(tf2);
        textDrivingTime = (TextView) view.findViewById(R.id.drivingTime);
        textDrivingTime.setTypeface(tf2);
        textNavigationButton = (TextView) view.findViewById(R.id.textNavigationButton);
        textNavigationButton.setTypeface(tf2);
        textPhoneButton = (TextView)view.findViewById(R.id.textPhoneButton);
        textPhoneButton.setTypeface(tf2);

//        topicTextAddress.setTypeface(tf);
//        topicTextPhone.setTypeface(tf);
        textName.setText(name);
        textName.setTypeface(tf);
        textAddress.setText(address);
//        textPhone.setText(phone);
        textAddress.setTypeface(tf2);
        textType.setText(type);
        textType.setTypeface(tf2);
//        textPhone.setTypeface(tf2);

        buttonPhone = (Button) view.findViewById(R.id.emergencyButton);
        buttonNavigate = (Button) view.findViewById(R.id.navigationButton);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setItems(phonenum, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = phonenum[which];
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("ยืนยันการโทรออก");
                        builder.setMessage("ติดต่อ " + selected);
                        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callActivity = new Intent(Intent.ACTION_CALL);
                                callActivity.setData(Uri.parse("tel:0876739362"));
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
                builder.setTitle("กรุณาเลือกหมายเลขโทรศัพท์");
//                builder.setMessage("ติดต่อ "+selected);
//                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent callActivity = new Intent(Intent.ACTION_CALL);
//                        callActivity.setData(Uri.parse("tel:0876739362"));
//                        startActivity(callActivity);
//                    }
//                });
//                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
                builder.show();
            }
        });
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    openGoogleMap(new LatLng(lat, lng), location);
                }
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

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setMessage("กรุณาเปิดใช้งาน GPS")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void statusCheck() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
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

    private void openGoogleMap(LatLng src, LatLng dest) {
        String url = "http://maps.google.com/maps?saddr=" + src.latitude + "," + src.longitude + "&daddr=" + dest.latitude + "," + dest.longitude + "&mode=driving";
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        if(getArguments().getInt("val") != 0) {
            drawer = (DrawerLayout) (getActivity()).findViewById(R.id.drawer_layout);

            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
//                   getFragmentManager().popBackStack();
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    toggle.setDrawerIndicatorEnabled(true);
                        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                            toggle.setDrawerIndicatorEnabled(true);
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            toggle.syncState();
                            getFragmentManager().popBackStack();
                        } else
                            ((AppCompatActivity) getActivity()).onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }
        }

        Bundle extraBundle = getArguments();
        if (!extraBundle.isEmpty()) {
            name = extraBundle.getString("name");
            location = new LatLng(extraBundle.getDouble("lat"), extraBundle.getDouble("lng"));
            address = extraBundle.getString("address");
            phone = extraBundle.getString("phone");
            website = extraBundle.getString("website");
            type = extraBundle.getString("type");
        }
        phonenum = phone.split(", ");
        phoneno = phonenum.length;
        statusCheck();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.hospital_item_menu, menu);

        MenuItem item = menu.findItem(R.id.item_star);
        Drawable drawable = item.getIcon();
        if(dbHelperDAO.checkExistItem("hospital", name)) drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        else drawable.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_star:
                    Drawable drawable = item.getIcon();
                    if(dbHelperDAO.checkExistItem("hospital", name)){
                        drawable.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                        dbHelperDAO.removeFavItem("hospital", name);
                    }else{
                        drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                        dbHelperDAO.addFavHospitalItem("hospital", name, address, location.latitude+", "+location.longitude, phone,website,type);
                    }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(name).snippet(null));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 15));

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
