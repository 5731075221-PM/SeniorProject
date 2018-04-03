package com.example.uefi.seniorproject.reminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

import java.util.ArrayList;

/**
 * Created by palida on 31-Mar-18.
 */

public class CustomAdapterNote  extends RecyclerView.Adapter{
    private ArrayList mItems;
    private Context mContext;
    private final int NOTE_DAY_ITEM = 0;
    private final int NOTE_ITEM = 1;

    interface OnItemClickListener{
        void onItemClick(View item,int position,int note_id);
    }

    private  OnItemClickListener mListener;

    public void setItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public CustomAdapterNote(Context context,ArrayList dataset) {
        mContext = context;
        mItems = dataset;
    }

    @Override
    public  int getItemViewType(int position){
        if(mItems.get(position) instanceof DayItem){
            return NOTE_DAY_ITEM;
        }else if (mItems.get(position) instanceof NoteItem){
            return NOTE_ITEM;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
//        final RecyclerView.ViewHolder vHolder;
        if(viewType == NOTE_DAY_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_note_day,parent,false);
//            vHolder = new DayHolder(v);
            final DayHolder vHolder = new DayHolder(v);
            return vHolder;
        } else if(viewType == NOTE_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_note_item,parent,false);
//            vHolder = new NoteHolder(v);
            final NoteHolder vHolder = new NoteHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int pos = vHolder.getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(view,pos,vHolder.note_id);
                        }
                    }
                }
            });
            return vHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(type == NOTE_DAY_ITEM){
            DayItem item = (DayItem) mItems.get(position);
            DayHolder detailHolder = (DayHolder) holder;
            detailHolder.name.setText(item.text);
        }else if(type == NOTE_ITEM){
            NoteItem item = (NoteItem) mItems.get(position);
            NoteHolder detailHolder = (NoteHolder) holder;
            detailHolder.name.setText(item.text);
            detailHolder.note_id = item.note_id;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class DayHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public DayHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.day);

        }

        public void onClick(View view){

        }

    }
    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private int note_id;

        public NoteHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.note);

        }

        public void onClick(View view){

        }

    }


}