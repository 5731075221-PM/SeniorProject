package com.example.uefi.seniorproject.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by UEFI on 7/2/2561.
 */

public class HospitalNearbyFragment extends Fragment implements SearchView.OnQueryTextListener {
    //    private static final String API_KEY = "AIzaSyBdiEiSJsjfhGQYTOhf10kcHwsII_J3xJ8";
    private static final String API_KEY = "AIzaSyBJJRq-nPY5HUjFLzvhp5CxW-GI_48rC4o";
    ArrayList<Hospital> defaultList, hospitalList, hosList;
    RecyclerView recyclerView;
    SearchView searchView;
    private RecyclerViewAdapter adapter;
    DBHelperDAO dbHelperDAO;
    LocationManager locationManager;
    ConnectivityManager connectionManager;
    LocationListener listener;
    double lat = 0.0, lng = 0.0;
    int index, indexSet;
    String search = "";
    boolean isGPS, isNetwork;
    ProgressDialog progressBar;
    AppBarLayout appBarLayout;
    TextView title;
    boolean isLoading = false;
    SortNearbyHospital sort;
    GetHospitalList get;
    RequestData request;

    class RequestData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            sendRequestInBackground();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class SortNearbyHospital extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("doIn lat = " + lat + " lng = " + lng);
            while (lat == 0.0 || lng == 0.0) {
                System.out.println("while lat = " + lat + " lng = " + lng);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc != null) {
                    lat = loc.getLatitude();
                    lng = loc.getLongitude();
                    System.out.println("loc = " + lat + " " + lng);
                }
            }
            calculateDistance();
            sendRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            request = (RequestData) new RequestData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            sortHospital();
            adapter.notifyDataSetChanged();
        }
    }

    class GetHospitalList extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage("กรุณารอสักครู่...");
            progressBar.setIndeterminate(false);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            hospitalList = dbHelperDAO.getHospital();
            defaultList = hospitalList;
            hosList = new ArrayList<Hospital>(hospitalList.subList(0, 10));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.dismiss();
            adapter = new RecyclerViewAdapter(hosList);
            recyclerView.setAdapter(adapter);
            if (isGPS && isNetwork) sort = (SortNearbyHospital) new SortNearbyHospital().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_nearby, container, false);

        appBarLayout.setExpanded(true, true);

//        searchView = (SearchView) view.findViewById(R.id.searchNearbyHospital);
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);
//        searchView.clearFocus();
//        searchView.setQueryHint("ค้นหา");
//        searchView.setOnQueryTextListener(this);
        title = getActivity().findViewById(R.id.textTool);
        title.setText("ค้นหาโรงพยาบาลใกล้เคียง");

        recyclerView = (RecyclerView) view.findViewById(R.id.nearbyHospitalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter(hosList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adapter.setOnLoadMoreListener(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (hosList.size() <= hospitalList.size()) {
                    isLoading = true;
                    hosList.add(null);
                    adapter.notifyItemInserted(hosList.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hosList.remove(hosList.size() - 1);
                            adapter.notifyItemRemoved(hosList.size());

                            //Generating more data
                            int index = hosList.size();
                            int end = index + 10;
                            for (int i = index; i < end; i++) {
                                hosList.add(hospitalList.get(i));
                            }
                            if (isGPS && isNetwork && lat != 0.0 && lng != 0.0) sortHospital();
                            adapter.notifyDataSetChanged();
                            isLoading = false;
//                            adapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(getActivity(), "โหลดข้อมูลเสร็จสมบูรณ์", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void calculateDistance() {
        float dis[] = new float[1];
        for (int i = 0; i < hospitalList.size(); i++) {
            Location.distanceBetween(lat, lng, hospitalList.get(i).getLat(), hospitalList.get(i).getLng(), dis);
            hospitalList.get(i).setPriority(dis[0]);
            System.out.println("DistPoints = " + dis[0] + " " + hospitalList.get(i).getName());
        }
        //Sort by distance
        Collections.sort(hospitalList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital h1, Hospital h2) {
                if (h1.getPriority() > h2.getPriority()) return 1;
                else if (h1.getPriority() < h2.getPriority()) return -1;
                else return 0;
            }
        });
        for (int i = 0; i < hospitalList.size(); i++) {
            System.out.println("calculate = " + hospitalList.get(i).getPriority() + " " + hospitalList.get(i).getName());
        }
    }

    private void sortHospital() {
        Collections.sort(hosList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital h1, Hospital h2) {
                if (h1.getDistanceValue() > h2.getDistanceValue()) return 1;
                else if (h1.getDistanceValue() < h2.getDistanceValue()) return -1;
                else return 0;
            }
        });
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
        for (index = indexSet; index < hosList.size(); index++) {
            urlString.append(Double.toString(hospitalList.get(index).getLat()) + "," + Double.toString(hospitalList.get(index).getLng()));
            if (indexSet < hosList.size() - 1) urlString.append("|");
        }
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
            System.out.println("Elements = " + array.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text"));

            System.out.println("Status = " + object.getString("status"));
            System.out.println("index = " + index + " indexSer = " + indexSet);
            int idx = 0;
            System.out.println("Index = " + index);
            System.out.println("IndexSet = " + indexSet);
            System.out.println(object.getString("status").equals("OK"));
            for (; indexSet < index; indexSet++) {
                System.out.println("IndexSet = " + indexSet);
                System.out.println(array.getJSONObject(0).getJSONArray("elements").getJSONObject(idx).getJSONObject("distance").getString("text"));
                hospitalList.get(indexSet).setDistance(array.getJSONObject(0).getJSONArray("elements").getJSONObject(idx).getJSONObject("distance").getString("text"));
                if (indexSet < hosList.size()) hosList.set(indexSet, hospitalList.get(indexSet));
                else hosList.add(hospitalList.get(indexSet));
                System.out.println("Sizeof =" + hosList.get(indexSet).getDistanceValue());
                idx++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRequestInBackground() {
        System.out.println("SendReq");
        LatLng origin = new LatLng(lat, lng);
        System.out.println("Origin = " + lat + " " + lng);
        LatLng destination;

        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=");
        urlString.append(Double.toString(lat) + "," + Double.toString(lng));
        urlString.append("&destinations=");

        for (index = indexSet; index < hosList.size() + 10; index++) {
            urlString.append(Double.toString(hospitalList.get(index).getLat()) + "," + Double.toString(hospitalList.get(index).getLng()));
            if (indexSet < hosList.size() + 9) urlString.append("|");
        }

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
            System.out.println("Elements = " + array.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text"));

            System.out.println("Status = " + object.getString("status"));
            System.out.println("index = " + index + " indexSer = " + indexSet);
            int idx = 0;
            System.out.println("Index = " + index);
            System.out.println("IndexSet = " + indexSet);
            System.out.println(object.getString("status").equals("OK"));
            for (; indexSet <= index; indexSet++) {
                System.out.println("IndexSet = " + indexSet);
                System.out.println(array.getJSONObject(0).getJSONArray("elements").getJSONObject(idx).getJSONObject("distance").getString("text"));
                hospitalList.get(indexSet).setDistance(array.getJSONObject(0).getJSONArray("elements").getJSONObject(idx).getJSONObject("distance").getString("text"));
                idx++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        get = (GetHospitalList) new GetHospitalList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        hospitalList = dbHelperDAO.getHospital();
//        defaultList = hospitalList;
//        hosList = new ArrayList<Hospital>(hospitalList.subList(0, 10));

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        connectionManager = (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);
        if (connectionManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connectionManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectionManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectionManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            isNetwork = true;
        } else isNetwork = false;
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        listener = new LocationListener() {
            public void onLocationChanged(Location loc) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                System.out.println("loc = " + lat + " " + lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        System.out.println("gps create = " + isGPS);
        System.out.println("network create = " + isNetwork);
        if (isGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                System.out.println("loc = " + lat + " " + lng);
            }
        }
        System.out.println("location = " + lat + " " + lng);
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_map, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_map:
//                    HospitalMapFragment fragment = new HospitalMapFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("type", "0");
//                    fragment.setArguments(bundle);
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.container_fragment, fragment)
//                            .addToBackStack(null)
//                            .commit();
//                return true;
////            case R.id.search_by_name:
////                getFragmentManager().beginTransaction()
////                        .replace(R.id.container_fragment, new SearchHospitalByName())
////                        .addToBackStack(null)
////                        .commit();
////                return true;
//////
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
        adapter = new RecyclerViewAdapter(hosList);
        recyclerView.setAdapter(adapter);

        return false;
    }

    public void resetSearch() {
        hospitalList = defaultList;
        adapter = new RecyclerViewAdapter(hosList);
        recyclerView.setAdapter(adapter);
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBarItem);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, distance, type;
        ImageView image;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
            image = (ImageView)itemView.findViewById(R.id.imageView1);
            name = (TextView) itemView.findViewById(R.id.textView1);
            name.setTypeface(tf);
            distance = (TextView) itemView.findViewById(R.id.distanceHospital);
            distance.setTypeface(tf);
            type = (TextView) itemView.findViewById(R.id.typeHospital);
            type.setTypeface(tf);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(get != null){
            System.out.println("Aget");
            get.cancel(true);
        }
        if(sort != null){
            System.out.println("Bget");
            sort.cancel(true);
        }
//        request.cancel(true);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private LoadMore onLoadMoreListener;
//        private boolean isLoading;
        private List<Hospital> hos;
        private int visibleThreshold = 1;
        private int lastVisibleItem, totalItemCount;

        public void setOnLoadMoreListener(LoadMore mOnLoadMoreListener) {
            this.onLoadMoreListener = mOnLoadMoreListener;
        }

        public RecyclerViewAdapter(List<Hospital> hos) {
            this.hos = hos;

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        isLoading = true;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
//                        isLoading = true;
                    }
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return hos.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
                return new HospitalNearbyFragment.ViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_loading, parent, false);
                return new HospitalNearbyFragment.LoadingViewHolder(view);
            }
            return null;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            System.out.println(position);
            if (holder instanceof ViewHolder) {
                Hospital h = hos.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;
                if(h.getType().equals("รัฐบาล")) {
                    viewHolder.image.setBackgroundResource(R.drawable.ic_hospital1);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.grid9));
                } else if(h.getType().equals("เอกชน")) {
                    viewHolder.image.setBackgroundResource(R.drawable.ic_hospital2);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.grid1));
                } else if(h.getType().equals("ชุมชน")) {
                    viewHolder.image.setBackgroundResource(R.drawable.ic_hospital3);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.grid4));
                } else if(h.getType().equals("ศูนย์")) {
                    viewHolder.image.setBackgroundResource(R.drawable.ic_hospital4);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.grid8));
                } else {
                    viewHolder.image.setBackgroundResource(R.drawable.ic_hospital5);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.grid3));
                }
                viewHolder.name.setText(h.getName().length() > 35 ? h.getName().substring(0,35)+".." : h.getName());
                viewHolder.distance.setText(h.getDistance() == "" ? "- กม." : h.getDistance());
                viewHolder.type.setText(h.getType());
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                        if (!isLongClick) {
                            HospitalItemFragment fragment = new HospitalItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name", hosList.get(position).getName());
                            bundle.putDouble("lat", hosList.get(position).getLat());
                            bundle.putDouble("lng", hosList.get(position).getLng());
                            bundle.putString("address", hosList.get(position).getAddress());
                            bundle.putString("phone", hosList.get(position).getPhone());
                            bundle.putString("website", hosList.get(position).getWebsite());
                            bundle.putString("type", hosList.get(position).getType());
                            bundle.putInt("val",1);
                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.name.setText(hospitalList.get(position).getName());
//            holder.distance.setText((hospitalList.get(position).getDistance()));
//            holder.setOnClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
//                    if (!isLongClick) {
//                        HospitalItemFragment fragment = new HospitalItemFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("name", hospitalList.get(position).getName());
//                        bundle.putDouble("lat", hospitalList.get(position).getLat());
//                        bundle.putDouble("lng", hospitalList.get(position).getLng());
//                        bundle.putString("address", hospitalList.get(position).getAddress());
//                        bundle.putString("phone", hospitalList.get(position).getPhone());
//                        bundle.putString("website", hospitalList.get(position).getWebsite());
//                        fragment.setArguments(bundle);
//                        getFragmentManager().beginTransaction()
//                                .replace(R.id.container_fragment, fragment)
//                                .addToBackStack(null)
//                                .commit();
//                    }
//                }
//            });
//        }

        @Override
        public int getItemCount() {
            return hos == null ? 0 : hos.size();
        }

        public void setLoaded() {
            isLoading = false;
            new RequestData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}