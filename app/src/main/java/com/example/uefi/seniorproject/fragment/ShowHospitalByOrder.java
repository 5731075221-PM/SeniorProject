package com.example.uefi.seniorproject.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by UEFI on 7/3/2561.
 */

public class ShowHospitalByOrder extends Fragment {
    DBHelperDAO dbHelperDAO;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    IndexFastScrollRecyclerView recyclerView;
    SearchView searchView;
    ArrayList<Hospital> defaultList, hospitalList;
    private ArrayList<Integer> mSectionPositions;
    private List<Hospital> mDataArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_by_order, container, false);

//        searchView = (SearchView) view.findViewById(R.id.searchHospitalByOrder);
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);
//        searchView.clearFocus();
//        searchView.setQueryHint("ค้นหา");
//        searchView.setOnQueryTextListener(this);

        recyclerView = (IndexFastScrollRecyclerView) view.findViewById(R.id.searchHospitalList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setIndexBarColor("#f9f9f9");
        recyclerView.setIndexBarTextColor("#4d4d4d");
        recyclerView.setIndexBarHighLateTextVisibility(true);
        recyclerView.setIndexbarHighLateTextColor("#4cd29f");

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        hospitalList = dbHelperDAO.getHospitalByOrder();
        defaultList = hospitalList;
        sortList();
    }

    public void sortList(){
        String letters = "[ก-ฮ]";
//        ArrayList<String> provinceList = new ArrayList<String>((Arrays.asList(getResources().getStringArray(R.array.ProvinceList))));
        List<String> sect = new ArrayList<>(77);
        List<String> alpha = new ArrayList<>(44);
        ArrayList<Hospital> temp = new ArrayList<>();
        for (int i = 0; i < hospitalList.size(); i++) {
            String section = String.valueOf(hospitalList.get(i).getProvince()).toUpperCase();
            if(!sect.contains(section)){
                section = String.valueOf(hospitalList.get(i).getProvince()).toUpperCase();
                sect.add(section);
                temp.add(new Hospital("",section));
                temp.add(hospitalList.get(i));

                section = String.valueOf(hospitalList.get(i).getProvince().charAt(0)).toUpperCase();
                section = !section.matches(letters) ? String.valueOf(hospitalList.get(i).getProvince().charAt(1)).toUpperCase() : section;
                if(!alpha.contains(section)){
                    alpha.add(section);
                }
            }else {
                temp.add(hospitalList.get(i));
            }
        }
        hospitalList = new ArrayList<>(temp);
        for(int i = 0;i< hospitalList.size();i++){
            System.out.println(hospitalList.get(i).getProvince());
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.headerItem);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, distance;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JasmineUPC.ttf");
            name = (TextView) itemView.findViewById(R.id.textView1);
            name.setTypeface(tf);
            distance = (TextView) itemView.findViewById(R.id.distanceHospital);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {
        private final int VIEW_TYPE_HEADER = 0;
        private final int VIEW_TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == VIEW_TYPE_HEADER){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_list, parent, false);
                return new ShowHospitalByOrder.HeaderViewHolder(view);
            }else if(viewType == VIEW_TYPE_ITEM){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
                return new ShowHospitalByOrder.ViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof ViewHolder){
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.name.setText(hospitalList.get(position).getName());
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                        if (!isLongClick) {
                            SelectItemFragment fragment = new SelectItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name",hospitalList.get(position).getName());
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });

            }else if(holder instanceof  HeaderViewHolder){
                HeaderViewHolder headerHolder = (HeaderViewHolder)holder;
                headerHolder.name.setText(hospitalList.get(position).getProvince());
            }
        }

        @Override
        public int getItemViewType(int position) {
            return hospitalList.get(position).getName() == "" ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return hospitalList.size();
        }

        @Override
        public Object[] getSections() {
            String letters = "[ก-ฮ]";
            mDataArray = hospitalList;
            List<String> sections = new ArrayList<>(44);
            mSectionPositions = new ArrayList<>(44);
            for (int i = 0, size = mDataArray.size(); i < size; i++) {
                String section = String.valueOf(mDataArray.get(i).getProvince().charAt(0)).toUpperCase();
                if(!section.matches(letters)){
                    section = String.valueOf(mDataArray.get(i).getProvince().charAt(1)).toUpperCase();
                }
                if (!sections.contains(section)) {
                    sections.add(section);
                    mSectionPositions.add(i);
                }
            }
            return sections.toArray(new String[0]);
        }

        @Override
        public int getPositionForSection(int i) {
            return mSectionPositions.get(i);
        }

        @Override
        public int getSectionForPosition(int i) {
            return 0;
        }
    }
}
