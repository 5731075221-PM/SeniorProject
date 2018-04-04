package com.example.uefi.seniorproject.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by UEFI on 16/3/2561.
 */

public class DiseaseGridSelectFragment extends Fragment {
    DBHelperDAO dbHelperDAO;
    RecyclerView recyclerView;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    ArrayList<Disease> diseaseName;
    TextView title;
    AppBarLayout appBarLayout;
    String type;

    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ",
            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช", "ระบบน้ำเหลือง", "ระบบกล้ามเนื้อและกระดูก", "ระบบภูมิคุ้มกัน", "อื่นๆ"
    };

    int[] gridViewImageId = {
            R.drawable.ic_disease1_select, R.drawable.ic_disease2_select, R.drawable.ic_disease3_select, R.drawable.ic_disease4_select, R.drawable.ic_disease5_select,
            R.drawable.ic_disease6_select, R.drawable.ic_disease7_select, R.drawable.ic_disease8_select, R.drawable.ic_disease9_select, R.drawable.ic_disease10_select,
            R.drawable.ic_disease11_select, R.drawable.ic_disease12_select, R.drawable.ic_disease13_select, R.drawable.ic_disease14_select, R.drawable.ic_disease15_select,
            R.drawable.ic_disease16_select, R.drawable.ic_disease17_select, R.drawable.ic_disease18_select, R.drawable.ic_disease19_select, R.drawable.ic_disease20_select
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_grid_select, container, false);

        appBarLayout.setExpanded(true, true);

        title = getActivity().findViewById(R.id.textTool);
        title.setText(getArguments().getString("type"));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerDiseaseSelect);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
        type = getArguments().getString("type");
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        diseaseName = dbHelperDAO.getDiseaseFromType(type);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, type;
        ImageView img;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.diseasetextView1);
            type = (TextView) itemView.findViewById(R.id.diseasetextView2);
            img = (ImageView) itemView.findViewById(R.id.diseaseimageView1);
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


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private final int VIEW_TYPE_HEADER = 0;
        private final int VIEW_TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if(viewType == VIEW_TYPE_HEADER){
//                View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_list, parent, false);
//                return new SearchSymptomFragment.HeaderViewHolder(view);
//            }else if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_disease_list, parent, false);
            return new DiseaseGridSelectFragment.ViewHolder(view);
//            }
//            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
            String type = diseaseName.get(position).getType();
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.name.setText(diseaseName.get(position).getName());
            viewHolder.name.setTypeface(tf);
            viewHolder.type.setText(type);
            viewHolder.type.setTypeface(tf);
            viewHolder.img.setImageResource(gridViewImageId[Arrays.asList(gridViewString).indexOf(type.split(",")[0])]);
            viewHolder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (!isLongClick) {
                        SelectItemFragment fragment = new SelectItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", diseaseName.get(position).getName());
                        bundle.putString("type", diseaseName.get(position).getType());
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return diseaseName.size();
        }
    }
}
