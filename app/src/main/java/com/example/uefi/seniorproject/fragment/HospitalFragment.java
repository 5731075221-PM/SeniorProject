package com.example.uefi.seniorproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.hospital.HospitalItem;
import com.example.uefi.seniorproject.hospital.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by UEFI on 27/12/2560.
 */

public class HospitalFragment extends Fragment {
    private boolean mToolBarNavigationListenerIsRegistered = true;
    ArrayList<String> nameList;
    ArrayList<Pair<Double,Double>> latlngList;
    ArrayList<Pair<String,String>> addrPhone;

    public HospitalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_hospital, container, false);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecyclerViewAdapter());

        DBHelperDAO dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        nameList = dbHelperDAO.getNameHospital();
        latlngList = dbHelperDAO.getLatLng();
        addrPhone = dbHelperDAO.getAddrPhone();

        dbHelperDAO.close();

        return view;
    }

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
            holder.name.setText(nameList.get(position));
            holder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    Log.d("position = ",position+"");
                    if(!isLongClick){
//                        Intent intent;
//                        intent = new Intent(getContext(), HospitalItemFragment.class);
                        HospitalItemFragment fragment = new HospitalItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("itemHospital", nameList.get(position));
                        bundle.putDouble("lat",latlngList.get(position).first);
                        bundle.putDouble("lng",latlngList.get(position).second);
                        bundle.putString("address",addrPhone.get(position).first);
                        bundle.putString("phone",addrPhone.get(position).second);
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return nameList.size();
        }
    }
}