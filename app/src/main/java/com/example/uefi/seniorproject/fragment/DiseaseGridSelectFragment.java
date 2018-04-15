package com.example.uefi.seniorproject.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ",
            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช", "ระบบน้ำเหลือง", "ระบบกล้ามเนื้อและกระดูก", "ระบบภูมิคุ้มกัน", "อื่นๆ"
    };

    int[] gridViewImageId = {
            R.drawable.selector_grid1, R.drawable.selector_grid2, R.drawable.selector_grid3, R.drawable.selector_grid4, R.drawable.selector_grid5,
            R.drawable.selector_grid6, R.drawable.selector_grid7, R.drawable.selector_grid8, R.drawable.selector_grid9, R.drawable.selector_grid10,
            R.drawable.selector_grid11, R.drawable.selector_grid12, R.drawable.selector_grid13, R.drawable.selector_grid14, R.drawable.selector_grid15,
            R.drawable.selector_grid16, R.drawable.selector_grid17, R.drawable.selector_grid18, R.drawable.selector_grid19, R.drawable.selector_grid20
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

        drawer = (DrawerLayout) (getActivity()).findViewById(R.id.drawer_layout);


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle( getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!mToolBarNavigationListenerIsRegistered) {
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Doesn't have to be onBackPressed
//                   getFragmentManager().popBackStack();
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    toggle.setDrawerIndicatorEnabled(true);
                    if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        toggle.setDrawerIndicatorEnabled(true);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                        getFragmentManager().popBackStack();
                    }
                    else
                        ((AppCompatActivity) getActivity()).onBackPressed();
                }
            });
            mToolBarNavigationListenerIsRegistered = true;
        }
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
            viewHolder.img.setBackgroundResource(gridViewImageId[Arrays.asList(gridViewString).indexOf(type.split(",")[0])]);
            viewHolder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (!isLongClick) {
                        SelectItemFragment fragment = new SelectItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", diseaseName.get(position).getName());
                        bundle.putString("type", diseaseName.get(position).getType());
                        bundle.putInt("val", 0);
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
