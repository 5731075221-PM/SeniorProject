package com.example.uefi.seniorproject.reminder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.uefi.seniorproject.R;

import java.util.ArrayList;

/**
 * Created by palida on 23-Mar-18.
 */

public class CustomAdapterNoteAddChoice extends RecyclerView.Adapter{
    private ArrayList<ChoiceItem> mChoice;
    private Context mContext;


    interface OnItemCheckListener {
        void onItemCheck(ChoiceItem item);
        void onItemUncheck(ChoiceItem item);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public CustomAdapterNoteAddChoice(Context context, ArrayList<ChoiceItem> dataset, @NonNull OnItemCheckListener onItemCheckListener) {
        mContext = context;
        mChoice = dataset;
        this.onItemCheckListener = onItemCheckListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_note_add_choice,parent,false);
        final ListViewHolder viewHolder = new ListViewHolder(view);

//        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = viewHolder.getAdapterPosition();
//                ChoiceItem currentItem = mChoice.get(pos);
//                if(viewHolder.checkBox.isChecked()){
//                    onItemCheckListener.onItemCheck(currentItem);
//                    currentItem.setCheck(true);
//                }else{
//                    onItemCheckListener.onItemUncheck(currentItem);
//                    currentItem.setCheck(false);
//                }
//            }
//        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ChoiceItem currentItem = mChoice.get(position);

        final ListViewHolder viewHolder = (ListViewHolder)holder;
        viewHolder.choice.setText(mChoice.get(position).text);
        viewHolder.checkBox.setChecked(mChoice.get(position).check);
        if(mChoice.get(position).check){
            onItemCheckListener.onItemCheck(currentItem);
        }else{
            onItemCheckListener.onItemUncheck(currentItem);
        }

        ((ListViewHolder) holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListViewHolder) viewHolder).checkBox.setChecked(
                        !((ListViewHolder) viewHolder).checkBox.isChecked());
                if (((ListViewHolder) viewHolder).checkBox.isChecked()) {
                    onItemCheckListener.onItemCheck(currentItem);
                } else {
                    onItemCheckListener.onItemUncheck(currentItem);
                }
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewHolder.getAdapterPosition();
                ChoiceItem currentItem = mChoice.get(pos);
                if(viewHolder.checkBox.isChecked()){
                    onItemCheckListener.onItemCheck(currentItem);
                }else{
                    onItemCheckListener.onItemUncheck(currentItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChoice.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView choice;
        private CheckBox checkBox;

        public ListViewHolder (View itemView){
            super(itemView);
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/THSarabunNew.ttf");

            choice = (TextView) itemView.findViewById(R.id.choice);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
            choice.setTypeface(tf);


        }

        public void onClick(View view){

        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }

    }
}