package com.example.uefi.seniorproject.fragment;

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
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.hospital.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by UEFI on 27/12/2560.
 */

public class HospitalFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    ArrayList<Hospital> defaultList;
    ArrayList<Hospital> hospitalList;
    String search = "";
    RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    public HospitalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecyclerViewAdapter());

        DBHelperDAO dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        hospitalList = dbHelperDAO.getHospital();
        defaultList = hospitalList;

        dbHelperDAO.close();

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
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
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
        if(s.length() > search.length()) {
            filteredValues = new ArrayList<Hospital>(hospitalList);
            for (Hospital value : hospitalList) {
                if (!value.getName().toLowerCase().contains(s.toLowerCase())) {
                    filteredValues.remove(value);
                }
            }
        }else {
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
    // Search

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener{
        TextView name;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView1);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<HospitalFragment.ViewHolder>{

        @Override
        public HospitalFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list,parent,false);
            return new HospitalFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(hospitalList.get(position).getName());
            holder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    Log.d("position = ",position+"");
                    if(!isLongClick){
                        HospitalItemFragment fragment = new HospitalItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", hospitalList.get(position).getName());
                        bundle.putDouble("lat",hospitalList.get(position).getLat());
                        bundle.putDouble("lng",hospitalList.get(position).getLng());
                        bundle.putString("address",hospitalList.get(position).getAddress());
                        bundle.putString("phone",hospitalList.get(position).getPhone());
                        bundle.putString("website",hospitalList.get(position).getWebsite());
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