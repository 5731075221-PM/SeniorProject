//package com.example.uefi.seniorproject.hospital;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.uefi.seniorproject.R;
//
//import java.util.ArrayList;
//
///**
// * Created by UEFI on 24/12/2560.
// */
//
//public class CustomAdapter extends BaseAdapter {
//    Context mContext;
//    ArrayList<String> strName;
//
//    public CustomAdapter(Context context, ArrayList<String> strName/*, int[] resId*/) {
//        this.mContext= context;
//        this.strName = strName;
////        this.resId = resId;
//    }
//
//    @Override
//    public int getCount() {
//        return strName.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        LayoutInflater mInflater =
//                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        if(view == null)
//            view = mInflater.inflate(R.layout.activity_hospital_list, parent, false);
//
//        TextView textView = (TextView)view.findViewById(R.id.textView1);
//        textView.setText(strName.get(position));
//
//        ImageView imageView = (ImageView)view.findViewById(R.id.imageView1);
//        imageView.setBackgroundResource(R.drawable.icon_hospital);
//
//        return view;
//    }
//}
