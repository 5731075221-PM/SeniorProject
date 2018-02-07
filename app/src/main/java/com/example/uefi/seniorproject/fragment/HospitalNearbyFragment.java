package com.example.uefi.seniorproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by UEFI on 7/2/2561.
 */

public class HospitalNearbyFragment extends Fragment implements SearchView.OnQueryTextListener{
    private static final String API_KEY = "AIzaSyBXXxey1s3u4Vdz5VfWMik6DT_kcAprtCA";
    ArrayList<Hospital> defaultList, hospitalList;
    RecyclerView recyclerView;
    SearchView searchView;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    DBHelperDAO dbHelperDAO;
    LocationManager locationManager;
    LocationListener listener;
    double lat, lng;
    int index, indexSet;
    String search = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_nearby, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchNearbyHospital);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหา");
        searchView.setOnQueryTextListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.nearbyHospitalRecyclerView);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        final boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//        listener = new LocationListener() {
//            public void onLocationChanged(Location loc) {
//                lat = loc.getLatitude();
//                lng = loc.getLongitude();
//                System.out.println("loc = "+lat+" "+lng);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            public void onProviderEnabled(String provider) {
//            }
//
//            public void onProviderDisabled(String provider) {
//            }
//        };

//        if (isGPS) sendRequest();
//        System.out.println("isGPS = " + isGPS);
//        System.out.println("default = " + defaultList.size());
//        System.out.println("hospital = " + hospitalList.size());
        return view;
    }

    private void sortHospital() {
        Collections.sort(defaultList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital h1, Hospital h2) {
                if (h1.getDistanceValue() > h2.getDistanceValue()) return 1;
                else if (h1.getDistanceValue() < h2.getDistanceValue()) return -1;
                else return 0;
            }
        });
        hospitalList = defaultList;
        System.out.println("Sort");
    }

    private void sendRequest() {
        System.out.println("SendReq");
        LatLng origin = new LatLng(lat, lng);
        System.out.println("Origin = " + lat + " " + lng);
        LatLng destination;

        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=");
        urlString.append(Double.toString(lat) + "," + Double.toString(lng));
        urlString.append("&destinations=");
        indexSet = 0;
        for (index = indexSet; index < defaultList.size(); index++) {
            urlString.append(Double.toString(defaultList.get(index).getLat()) + "," + Double.toString(defaultList.get(index).getLng()));
            if (index != 0 && (((index+1) % 25) == 0) || index == defaultList.size()-1) {
                System.out.println("ifffff");
                urlString.append("&language=th-TH");
                urlString.append("&key=" + API_KEY);
                System.out.println("urlString = " + urlString);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                System.out.println("Http");
                HttpURLConnection urlConnection = null;
                URL url = null;

                try {
                    url = new URL(urlString.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    System.out.println("Connect");
                    InputStream inStream = urlConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

                    String temp, response = "";
                    while ((temp = bReader.readLine()) != null) {
                        response += temp;
                    }

                    bReader.close();
                    inStream.close();
                    urlConnection.disconnect();
                    System.out.println("Disconnect");

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                    System.out.println("Object = " + object.toString());
                    JSONArray array = object.getJSONArray("rows");
                    System.out.println("Elements = "+array.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text"));

                    System.out.println("Status = "+object.getString("status"));
                    System.out.println("index = "+index+" indexSer = "+indexSet);
                    int idx = 0;
                    System.out.println("Index = " + index);
                    System.out.println("IndexSet = " + indexSet);
                    System.out.println(object.getString("status").equals("OK"));
                    for (; indexSet <= index; indexSet++) {
                        System.out.println("IndexSet = " + indexSet);
                        System.out.println(array.getJSONObject(0).getJSONArray("elements").getJSONObject(idx).getJSONObject("distance").getString("text"));
                        defaultList.get(indexSet).setDistance(array.getJSONObject(0).getJSONArray("elements").getJSONObject(idx).getJSONObject("distance").getString("text"));
                        idx++;
                    }
                    urlString = new StringBuilder();
                    urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=");
                    urlString.append(Double.toString(lat) + "," + Double.toString(lng));
                    urlString.append("&destinations=");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                urlString.append("|");
            }
        }
        sortHospital();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        hospitalList = dbHelperDAO.getHospital();
        defaultList = hospitalList;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        listener = new LocationListener() {
            public void onLocationChanged(Location loc) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                System.out.println("loc = "+lat+" "+lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        System.out.println("gps create = "+isGPS);
        if (isGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                System.out.println("loc = "+lat+" "+lng);
            }
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPS) sendRequest();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s == null || s.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        ArrayList<Hospital> filteredValues;
        if (s.length() > search.length()) {
            filteredValues = new ArrayList<Hospital>(hospitalList);
            for (Hospital value : hospitalList) {
                if (!value.getName().toLowerCase().contains(s.toLowerCase())) {
                    filteredValues.remove(value);
                }
            }
        } else {
            filteredValues = new ArrayList<Hospital>(defaultList);
            for (Hospital value : defaultList) {
                if (!value.getName().toLowerCase().contains(s.toLowerCase())) {
                    filteredValues.remove(value);
                }
            }
        }
        search = s;
        hospitalList = filteredValues;
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return false;
    }

    public void resetSearch() {
        hospitalList = defaultList;
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener {
        TextView name, distance;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");
            name = (TextView) itemView.findViewById(R.id.textView1);
            name.setTypeface(tf);
            distance = (TextView) itemView.findViewById(R.id.distanceHospital);
            itemView.setOnClickListener(this);
        }

        public void setOnClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false, null);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true, null);
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            itemClickListener.onClick(view, getAdapterPosition(), false, motionEvent);
            return true;
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<HospitalNearbyFragment.ViewHolder> {

        @Override
        public HospitalNearbyFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
            return new HospitalNearbyFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(hospitalList.get(position).getName());
            holder.distance.setText((hospitalList.get(position).getDistance()));
            holder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (!isLongClick) {
                        HospitalItemFragment fragment = new HospitalItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", hospitalList.get(position).getName());
                        bundle.putDouble("lat", hospitalList.get(position).getLat());
                        bundle.putDouble("lng", hospitalList.get(position).getLng());
                        bundle.putString("address", hospitalList.get(position).getAddress());
                        bundle.putString("phone", hospitalList.get(position).getPhone());
                        bundle.putString("website", hospitalList.get(position).getWebsite());
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return hospitalList.size();
        }
    }
}
