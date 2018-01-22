package com.example.uefi.seniorproject.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.hospital.ItemClickListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

/**
 * Created by UEFI on 27/12/2560.
 */

public class HospitalFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, AdapterView.OnItemSelectedListener {
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
    String[] arrayProvince, arrayZone;
    String selectProvince, selectZone;
    DBHelperDAO dbHelperDAO;

    public HospitalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);

        final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        hospitalList = dbHelperDAO.getHospital();
        defaultList = hospitalList;

//        dbHelperDAO.close();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        arrayProvince = getResources().getStringArray(R.array.Province1);
        arrayZone = getResources().getStringArray(R.array.Zone1All);

        province = (SearchableSpinner) view.findViewById(R.id.spinnerProvince);
        province.setTitle("กรุณาเลือกจังหวัด");
        province.setPositiveButton("ยกเลิก");
        adapterProvince = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, arrayProvince);
        province.setAdapter(adapterProvince);
        province.setOnItemSelectedListener(this);

        zone = (SearchableSpinner) view.findViewById(R.id.spinnerZone);
        zone.setTitle("กรุณาเลือกเขต");
        zone.setPositiveButton("ยกเลิก");
        adapterZone = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, arrayZone);
        zone.setAdapter(adapterZone);
        zone.setOnItemSelectedListener(this);

        headProvince = (TextView) view.findViewById(R.id.textViewProvince);
        headZone = (TextView) view.findViewById(R.id.textViewZone);
        headProvince.setTypeface(tf);
        headZone.setTypeface(tf);

        return view;
    }

    // Search
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("ค้นหา");

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
        return true;
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
//    if(adapterView.getChildAt(0) != null) ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
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
                if (i == 0) {
                    selectProvince = "";
                    adapterZone = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1All));
                    arrayZone = getResources().getStringArray(R.array.Zone1All);
                    zone.setAdapter(adapterZone);
                } else selectProvince = arrayProvince[i];
                if (i == 1) {
                    adapterZone = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1Bangkok));
                    arrayZone = getResources().getStringArray(R.array.Zone1Bangkok);
                    zone.setAdapter(adapterZone);
                } else if (i == 2) {
                    adapterZone = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1Nonthaburi));
                    arrayZone = getResources().getStringArray(R.array.Zone1Nonthaburi);
                    zone.setAdapter(adapterZone);
                } else if (i == 3) {
                    adapterZone = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1Patumthani));
                    arrayZone = getResources().getStringArray(R.array.Zone1Patumthani);
                    zone.setAdapter(adapterZone);
                } else if (i == 4) {
                    adapterZone = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Zone1SamutPrakarn));
                    arrayZone = getResources().getStringArray(R.array.Zone1SamutPrakarn);
                    zone.setAdapter(adapterZone);
                }
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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    // Search

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener {
        TextView name;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");
            name = (TextView) itemView.findViewById(R.id.textView1);
            name.setTypeface(tf);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<HospitalFragment.ViewHolder> {

        @Override
        public HospitalFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
            return new HospitalFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(hospitalList.get(position).getName());
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