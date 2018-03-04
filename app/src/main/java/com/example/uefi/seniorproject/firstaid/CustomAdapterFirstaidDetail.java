package com.example.uefi.seniorproject.firstaid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.uefi.seniorproject.R;

import java.util.ArrayList;

/**
 * Created by palida on 14-Feb-18.
 */

public class CustomAdapterFirstaidDetail  extends RecyclerView.Adapter{
    private ArrayList mItems;
    private Context mContext;
    private final int DETAIL_ITEM = 0;
    private final int PIC_ITEM = 1;
    private final int DETAIL_PIC_ITEM = 2;
    private final int SUBJECT_ITEM = 3;
    private final int SUBJECT_RED_ITEM = 4;

    public CustomAdapterFirstaidDetail(Context context,ArrayList dataset) {
        mContext = context;
        mItems = dataset;
    }

    @Override
    public  int getItemViewType(int position){
        if(mItems.get(position) instanceof DetailItem){
            return DETAIL_ITEM;
        }else if (mItems.get(position) instanceof PicItem){
            return PIC_ITEM;
        }else if (mItems.get(position) instanceof PicDetailItem){
            return DETAIL_PIC_ITEM;
        }else if (mItems.get(position) instanceof SubjectItem){
            return SUBJECT_ITEM;
        }else if (mItems.get(position) instanceof SubjectRedItem){
            return SUBJECT_RED_ITEM;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final RecyclerView.ViewHolder vHolder;
        if(viewType == DETAIL_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_detail_firstaid,parent,false);
            vHolder = new DetailHolder(v);
            return vHolder;
        } else if(viewType == PIC_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_pic_firstaid,parent,false);
            vHolder = new PicHolder(v);
            return vHolder;
        } else if(viewType == DETAIL_PIC_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_detail_pic_firstaid,parent,false);
            vHolder = new DetailPicHolder(v);
            return vHolder;
        }else if(viewType == SUBJECT_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_subject_firstaid,parent,false);
            vHolder = new SubjectHolder(v);
            return vHolder;
        }else if(viewType == SUBJECT_RED_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_subject_red_firstaid,parent,false);
            vHolder = new SubjectRedHolder(v);
            return vHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(type == DETAIL_ITEM){
            DetailItem item = (DetailItem) mItems.get(position);
            DetailHolder detailHolder = (DetailHolder) holder;
            detailHolder.name.setText(item.text);
        }else if(type == PIC_ITEM){
            PicItem item = (PicItem) mItems.get(position);
            PicHolder detailHolder = (PicHolder) holder;
            Bitmap bmp = BitmapFactory.decodeByteArray(item.img,0,item.img.length);
            detailHolder.charac.setImageBitmap(bmp);
        }else if(type == DETAIL_PIC_ITEM){
            PicDetailItem item = (PicDetailItem) mItems.get(position);
            DetailPicHolder detailHolder = (DetailPicHolder) holder;
            detailHolder.name.setText(item.text);
        }else if(type == SUBJECT_ITEM){
            SubjectItem item = (SubjectItem) mItems.get(position);
            SubjectHolder detailHolder = (SubjectHolder) holder;
            detailHolder.name.setText(item.text);
        }else if(type == SUBJECT_RED_ITEM){
            SubjectRedItem item = (SubjectRedItem) mItems.get(position);
            SubjectRedHolder detailHolder = (SubjectRedHolder) holder;
            detailHolder.name.setText(item.text);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public DetailHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewDetail);

        }

        public void onClick(View view){

        }

    }
    private class DetailPicHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public DetailPicHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewDetailPic);

        }

        public void onClick(View view){

        }

    }

    private class PicHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView charac;

        public PicHolder (View itemView){
            super(itemView);
            charac = (ImageView) itemView.findViewById(R.id.pic);

        }

        public void onClick(View view){

        }

    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public SubjectHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewSubject);

        }

        public void onClick(View view){

        }

    }

    private class SubjectRedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public SubjectRedHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewSubject);

        }

        public void onClick(View view){

        }

    }

}