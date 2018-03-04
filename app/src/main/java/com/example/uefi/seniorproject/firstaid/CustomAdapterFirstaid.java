package com.example.uefi.seniorproject.firstaid;

/**
 * Created by palida on 01-Feb-18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.fragment.ItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class CustomAdapterFirstaid extends RecyclerView.Adapter{
    private ArrayList<String> mFirstaids;
    private Context mContext;


    interface OnItemClickListener{
        void onItemClick(View item,int position);
    }

    private  OnItemClickListener mListener;
    public void setItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public CustomAdapterFirstaid(Context context,ArrayList<String> dataset) {
        mContext = context;
        mFirstaids = dataset;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_item_firstaid,parent,false);
        final ListViewHolder viewHolder = new ListViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    int pos = viewHolder.getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        mListener.onItemClick(view,pos);
                    }
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder)holder).blindView(position);

        String name = mFirstaids.get(position);
        String letter = String.valueOf(name.charAt(0));
        if(letter.equals("เ") ||letter.equals("ใ") ||letter.equals("ไ") ||letter.equals("โ") ||letter.equals("แ")){
            letter = String.valueOf(name.charAt(1));
        }
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter,generator.getRandomColor());
        ((ListViewHolder) holder).charac.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mFirstaids.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private ImageView charac;

        public ListViewHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.firstaid_subject_item);
            charac = (ImageView) itemView.findViewById(R.id.charac);
            itemView.setOnClickListener(this);

        }

        public void blindView(int position){
            name.setText(mFirstaids.get(position));

        }

        public void onClick(View view){

        }

    }
}