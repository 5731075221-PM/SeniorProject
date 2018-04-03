package com.example.uefi.seniorproject.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by UEFI on 27/12/2560.
 */

public class SearchHospitalByName extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, AdapterView.OnItemSelectedListener {
    ArrayList<Hospital> defaultList, hospitalList;
    String search = "";
    boolean isProvince = false, isZone = false;
    int provincePos = -1, zonePos = -1;
    RecyclerView recyclerView;
    SearchableSpinner province, zone;
    SearchView searchView;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    private ArrayAdapter<String> adapterProvince, adapterZone;
    private TextView headProvince, headZone;
    String[] arrayProvince, arrayZone, arrayZoneAll;
    String selectProvince, selectZone;
    DBHelperDAO dbHelperDAO;
    int[] rid = {R.array.ZoneKrabi, R.array.ZoneBangkok, R.array.ZoneKanchanaburi, R.array.ZoneKalasin, R.array.ZoneKamphaengphet,
    R.array.ZoneKhonkaen, R.array.ZoneChanthaburi, R.array.ZoneChachoengsao, R.array.ZoneChonburi, R.array.ZoneChainat, R.array.ZoneChaiyaphum,
    R.array.ZoneChumphon, R.array.ZoneChiangrai, R.array.ZoneChiangmai, R.array.ZoneTrang, R.array.ZoneTrat, R.array.ZoneTak, R.array.ZoneNakhonnayok,
    R.array.ZoneNakhonpathom, R.array.ZoneNakhonphanom, R.array.ZoneNakhonratchasima, R.array.ZoneNakhonsithammarat, R.array.ZoneNakhonsawan,
    R.array.ZoneNonthaburi, R.array.ZoneNarathiwat, R.array.ZoneNan, R.array.ZoneBuengkan, R.array.ZoneBuriram, R.array.ZonePathumthani,
    R.array.ZonePrachuapkirikhan, R.array.ZonePrachinburi, R.array.ZonePattani, R.array.ZonePhranakhonsiayutthaya, R.array.ZonePhangnga,
    R.array.ZonePhatthalung, R.array.ZonePhichit, R.array.ZonePhitsanulok, R.array.ZonePhetchaburi, R.array.ZonePhetchabun, R.array.ZonePhrae,
    R.array.ZonePhayao, R.array.ZonePhuket, R.array.ZoneMahasarakham, R.array.ZoneMukdahan, R.array.ZoneMaehongson, R.array.ZoneYala, R.array.ZoneYasothon,
    R.array.ZoneRoiet, R.array.ZoneRanong, R.array.ZoneRayong, R.array.ZoneRatchaburi, R.array.ZoneLopburi, R.array.ZoneLampang, R.array.ZoneLamphun,
    R.array.ZoneLoei, R.array.ZoneSisaket, R.array.ZoneSakonnakhon, R.array.ZoneSongkhla, R.array.ZoneSatun, R.array.ZoneSamutprakarn, R.array.ZoneSamutsongkhram,
    R.array.ZoneSamutsakhon, R.array.ZoneSakaeo, R.array.ZoneSaraburi, R.array.ZoneSingburi, R.array.ZoneSukhothai, R.array.ZoneSuphanburi,
    R.array.ZoneSuratthani, R.array.ZoneSurin, R.array.ZoneNongkhai, R.array.ZoneNongbualamphu, R.array.ZoneAngthong, R.array.ZoneUdonthani,
    R.array.ZoneUthaithani, R.array.ZoneUttaradit, R.array.ZoneUbonratchathani, R.array.ZoneAmnartcharoen};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);

        final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        searchView = (SearchView) view.findViewById(R.id.searchHospital);
        searchView.setBackgroundResource(R.drawable.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหา");
        searchView.setOnQueryTextListener(this);

        province = (SearchableSpinner) view.findViewById(R.id.spinnerProvince);
        province.setTitle("กรุณาเลือกจังหวัด");
        province.setPositiveButton("ยกเลิก");
        adapterProvince = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayProvince);
        province.setAdapter(adapterProvince);
        province.setOnItemSelectedListener(this);

        zone = (SearchableSpinner) view.findViewById(R.id.spinnerZone);
        zone.setTitle("กรุณาเลือกเขต");
        zone.setPositiveButton("ยกเลิก");
        adapterZone = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, arrayZone);
        zone.setAdapter(adapterZone);
        zone.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Search
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        hospitalList = dbHelperDAO.getHospital();
        defaultList = hospitalList;
        selectProvince = "";
        selectZone = "";

        arrayProvince = getResources().getStringArray(R.array.ProvinceList);
        arrayZone = getResources().getStringArray(R.array.ZoneAll);
        arrayZoneAll = arrayZone;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        searchView = (SearchView) searchItem.getActionView();
//        searchView.setBackgroundResource(R.drawable.search_view);
//        searchView.setOnQueryTextListener(this);
//        searchView.setQueryHint("ค้นหา");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            isProvince = true;
            isZone = true;
            provincePos = province.getSelectedItemPosition();
            zonePos = zone.getSelectedItemPosition();
            search = "";

            HospitalMapFragment fragment = new HospitalMapFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "0");
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (isProvince) {
            if (adapterView.getId() == province.getId() && i == provincePos) {
                isProvince = false;
                province.setSelection(provincePos);
            }
        }
        if (isZone) {
            if (adapterView.getId() == zone.getId() && i == zonePos) {
                isZone = false;
                zone.setSelection(zonePos);
            }
        }
        if (!isProvince && !isZone) {
            if (adapterView.getId() == province.getId()) {
                if(i==0) {
                    selectProvince = "";
                    adapterZone = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayZoneAll);
                    arrayZone = arrayZoneAll;
                    zone.setAdapter(adapterZone);
                }else {
                    selectProvince = arrayProvince[i];
                    adapterZone = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(rid[i-1]));
                    arrayZone = getResources().getStringArray(rid[i-1]);
                    zone.setAdapter(adapterZone);
                }
//                if (i == 0) {
//                    selectProvince = "";
//                    adapterZone = new ArrayAdapter<String>(getActivity(),
//                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1All));
//                    arrayZone = getResources().getStringArray(R.array.Zone1All);
//                    zone.setAdapter(adapterZone);
//                } else selectProvince = arrayProvince[i];
//                if (i == 1) {
//                    adapterZone = new ArrayAdapter<String>(getActivity(),
//                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1Bangkok));
//                    arrayZone = getResources().getStringArray(R.array.Zone1Bangkok);
//                    zone.setAdapter(adapterZone);
//                } else if (i == 2) {
//                    adapterZone = new ArrayAdapter<String>(getActivity(),
//                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1Nonthaburi));
//                    arrayZone = getResources().getStringArray(R.array.Zone1Nonthaburi);
//                    zone.setAdapter(adapterZone);
//                } else if (i == 3) {
//                    adapterZone = new ArrayAdapter<String>(getActivity(),
//                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1Patumthani));
//                    arrayZone = getResources().getStringArray(R.array.Zone1Patumthani);
//                    zone.setAdapter(adapterZone);
//                } else if (i == 4) {
//                    adapterZone = new ArrayAdapter<String>(getActivity(),
//                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1SamutPrakarn));
//                    arrayZone = getResources().getStringArray(R.array.Zone1SamutPrakarn);
//                    zone.setAdapter(adapterZone);
//                }
            } else {
                if (i == 0) selectZone = "";
                else selectZone = arrayZone[i];
            }
            defaultList = dbHelperDAO.selectHospital(selectProvince, selectZone);
            hospitalList = defaultList;

            if (search != "") {
                ArrayList<Hospital> filteredValues = new ArrayList<Hospital>(defaultList);
                for (Hospital value : defaultList) {
                    if (!value.getName().toLowerCase().contains(search.toLowerCase())) {
                        filteredValues.remove(value);
                    }
                }
                hospitalList = filteredValues;
            }
            adapter = new RecyclerViewAdapter();
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
    // Search

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, distance, type;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<SearchHospitalByName.ViewHolder> {

        @Override
        public SearchHospitalByName.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
            return new SearchHospitalByName.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(hospitalList.get(position).getName());
            holder.distance.setText((hospitalList.get(position).getDistance()));
            holder.type.setText(hospitalList.get(position).getType());
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
                        bundle.putString("key", hospitalList.get(position).getType());
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