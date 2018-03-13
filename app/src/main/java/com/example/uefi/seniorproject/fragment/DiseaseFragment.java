package com.example.uefi.seniorproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uefi.seniorproject.R;

/**
 * Created by UEFI on 11/3/2561.
 */

public class DiseaseFragment extends Fragment {
    RecyclerView recyclerView;
    Context context;
    RecyclerView.Adapter recyclerView_Adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ ",
            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช "
    } ;
    int[] gridViewImageId = {
            R.drawable.ic_disease1_select, R.drawable.ic_disease2, R.drawable.ic_disease3, R.drawable.ic_disease4, R.drawable.ic_disease5,
            R.drawable.ic_disease6, R.drawable.ic_disease7, R.drawable.ic_disease8, R.drawable.ic_disease9, R.drawable.ic_disease10,
            R.drawable.ic_disease11, R.drawable.ic_disease12, R.drawable.ic_disease13, R.drawable.ic_disease14
            , R.drawable.ic_disease15, R.drawable.ic_disease16
    };
    int[] gridViewBgId = {
            R.color.colorMacawBlueGreen, R.color.colorGreen, R.color.colorMacawBlueGreen, R.color.colorGreen,
            R.color.colorMacawBlueGreen, R.color.colorGreen, R.color.colorMacawBlueGreen, R.color.colorGreen,
            R.color.colorMacawBlueGreen, R.color.colorGreen, R.color.colorMacawBlueGreen, R.color.colorGreen,
            R.color.colorMacawBlueGreen, R.color.colorGreen, R.color.colorMacawBlueGreen, R.color.colorGreen,
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease, container, false);

        context = getActivity().getApplicationContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerDisease);

        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
        recyclerViewLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView_Adapter = new RecyclerViewAdapter(context,gridViewString,gridViewImageId,gridViewBgId);
        recyclerView.setAdapter(recyclerView_Adapter);

        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        String[] text;
        int[] image, background;
        Context context1;

        public RecyclerViewAdapter(Context context2,String[] str, int[] img, int[] bg){
            text = str;
            image = img;
            background = bg;
            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener{
            TextView textView;
            ImageView imageView;
            LinearLayout linearLayout;
            private ItemClickListener itemClickListener;

            public void setOnClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }

            public ViewHolder(View v){
                super(v);
                textView = (TextView) v.findViewById(R.id.gridview_text);
                imageView = (ImageView) v.findViewById(R.id.gridview_image);
                linearLayout = (LinearLayout) v.findViewById(R.id.gridview_layout);
                v.setOnClickListener(this);
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

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view1 = LayoutInflater.from(context1).inflate(R.layout.fragment_disease_layout,parent,false);
            ViewHolder viewHolder1 = new ViewHolder(view1);
            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(ViewHolder Vholder, int position){
            Vholder.textView.setText(text[position]);
            Vholder.imageView.setImageResource(image[position]);
//            Vholder.linearLayout.setBackgroundResource(gridViewBgId[position]);
            Vholder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    Toast.makeText(getActivity(), "GridView Item: " + position, Toast.LENGTH_LONG).show();
                }
//                @Override
//                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
//                    if (!isLongClick) {
//                        HospitalItemFragment fragment = new HospitalItemFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("name", hosList.get(position).getName());
//                        bundle.putDouble("lat", hosList.get(position).getLat());
//                        bundle.putDouble("lng", hosList.get(position).getLng());
//                        bundle.putString("address", hosList.get(position).getAddress());
//                        bundle.putString("phone", hosList.get(position).getPhone());
//                        bundle.putString("website", hosList.get(position).getWebsite());
//                        bundle.putString("type", hosList.get(position).getType());
//                        fragment.setArguments(bundle);
//                        getFragmentManager().beginTransaction()
//                                .replace(R.id.container_fragment, fragment)
//                                .addToBackStack(null)
//                                .commit();
//                    }
//                }
            });
        }
        @Override
        public int getItemCount(){
            return gridViewString.length;
        }
    }
}