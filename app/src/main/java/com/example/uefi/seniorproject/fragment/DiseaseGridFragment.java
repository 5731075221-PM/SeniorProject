package com.example.uefi.seniorproject.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uefi.seniorproject.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by UEFI on 11/3/2561.
 */

public class DiseaseGridFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    AppBarLayout appBarLayout;

    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ",
            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช", "ระบบน้ำเหลือง", "ระบบกล้ามเนื้อและกระดูก", "ระบบภูมิคุ้มกัน", "อื่นๆ"
    } ;
    int[] gridViewImageId = {
            R.drawable.ic_disease1_select, R.drawable.ic_disease2_select, R.drawable.ic_disease3_select, R.drawable.ic_disease4_select, R.drawable.ic_disease5_select,
            R.drawable.ic_disease6_select, R.drawable.ic_disease7_select, R.drawable.ic_disease8_select, R.drawable.ic_disease9_select, R.drawable.ic_disease10_select,
            R.drawable.ic_disease11_select, R.drawable.ic_disease12_select, R.drawable.ic_disease13_select, R.drawable.ic_disease14_select, R.drawable.ic_disease15_select,
            R.drawable.ic_disease16_select, R.drawable.ic_disease17_select, R.drawable.ic_disease18_select, R.drawable.ic_disease19_select, R.drawable.ic_disease20_select
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease, container, false);

        appBarLayout.setExpanded(true, true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerDisease);

        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener{
            TextView textView;
            ImageView imageView;
            private ItemClickListener itemClickListener;

            public void setOnClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }

            public ViewHolder(View v){
                super(v);
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");
                textView = (TextView) v.findViewById(R.id.diseae_grid_text);
                textView.setTypeface(tf);
                imageView = (ImageView) v.findViewById(R.id.diseae_grid_image);
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_disease_grid_layout,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder Vholder, int position){
            Vholder.textView.setText(gridViewString[position]);
            Vholder.imageView.setImageResource(gridViewImageId[position]);
            Vholder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    Toast.makeText(getActivity(), "GridView Item: " + position, Toast.LENGTH_LONG).show();
                    DiseaseGridSelectFragment fragment = new DiseaseGridSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", gridViewString[position]);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_fragment, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        @Override
        public int getItemCount(){
            return gridViewString.length;
        }
    }
}
