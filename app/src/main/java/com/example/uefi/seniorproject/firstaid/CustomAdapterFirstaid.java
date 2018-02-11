package com.example.uefi.seniorproject.firstaid;

/**
 * Created by palida on 01-Feb-18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.uefi.seniorproject.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapterFirstaid extends RecyclerView.Adapter{
    private ArrayList<String> mFirstaids;

    public CustomAdapterFirstaid(ArrayList<String> dataset) {
        mFirstaids = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_item_firstaid,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder)holder).blindView(position);
    }

    @Override
    public int getItemCount() {
        return mFirstaids.size();
    }
    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView subject;

        public ListViewHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.firstaid_subject_item);
            itemView.setOnClickListener(this);

        }

        public void blindView(int position){
            name.setText(mFirstaids.get(position));

        }

        public void onClick(View view){

        }

    }
}