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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by UEFI on 14/3/2561.
 */

public class DiseaseListFragment extends Fragment{
    DBHelperDAO dbHelperDAO;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    IndexFastScrollRecyclerView recyclerView;
    ArrayList<Disease> diseaseName;
    private ArrayList<Integer> mSectionPositions;
    private List<Disease> mDataArray;
    AppBarLayout appBarLayout;
    String[] gridViewString;

//    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ",
//            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
//            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช", "ระบบน้ำเหลือง", "ระบบกล้ามเนื้อและกระดูก", "ระบบภูมิคุ้มกัน", "อื่นๆ"
//    };

    int[] gridViewImageId = {
            R.drawable.selector_grid1, R.drawable.selector_grid2, R.drawable.selector_grid3, R.drawable.selector_grid4, R.drawable.selector_grid5,
            R.drawable.selector_grid6, R.drawable.selector_grid7, R.drawable.selector_grid8, R.drawable.selector_grid9, R.drawable.selector_grid10,
            R.drawable.selector_grid11, R.drawable.selector_grid12, R.drawable.selector_grid13, R.drawable.selector_grid14, R.drawable.selector_grid15,
            R.drawable.selector_grid16, R.drawable.selector_grid17, R.drawable.selector_grid18, R.drawable.selector_grid19, R.drawable.selector_grid20
    };

    int[] gridViewImageId2 = {
            R.drawable.ic_disease1_selected, R.drawable.ic_disease2_selected, R.drawable.ic_disease3_selected, R.drawable.ic_disease4_selected, R.drawable.ic_disease5_selected,
            R.drawable.ic_disease6_selected, R.drawable.ic_disease7_selected, R.drawable.ic_disease8_selected, R.drawable.ic_disease9_selected, R.drawable.ic_disease10_selected,
            R.drawable.ic_disease11_selected, R.drawable.ic_disease12_selected, R.drawable.ic_disease13_selected, R.drawable.ic_disease14_selected, R.drawable.ic_disease15_selected,
            R.drawable.ic_disease16_selected, R.drawable.ic_disease17_selected, R.drawable.ic_disease18_selected, R.drawable.ic_disease19_selected, R.drawable.ic_disease20_selected};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);

        if(getActivity().getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        if(appBarLayout != null) appBarLayout.setExpanded(true, true);

        recyclerView = (IndexFastScrollRecyclerView) view.findViewById(R.id.diseaseList);
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

        if(appBarLayout != null) appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
        gridViewString = getResources().getStringArray(R.array.gridString);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        diseaseName = dbHelperDAO.getDisease();
        sortList();
    }

    public void sortList(){
        String letters = "[ก-ฮ]";
        List<String> sect = new ArrayList<>(45);
        ArrayList<Disease> temp = new ArrayList<>();
        for (int i = 0; i < diseaseName.size(); i++) {
            String section = String.valueOf(diseaseName.get(i).getName().charAt(0)).toUpperCase();
            if(!section.matches(letters)){
                section = "#";//String.valueOf(diseaseName.get(i).getName().charAt(1)).toUpperCase();
            }
            if (!sect.contains(section)) {
                sect.add(section);
                temp.add(new Disease(section,""));
            }
            temp.add(diseaseName.get(i));
        }
        diseaseName = new ArrayList<>(temp);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.headerItem);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, type;
        ImageView img;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.diseasetextView1);
            type = (TextView)itemView.findViewById(R.id.diseasetextView2);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {
        private final int VIEW_TYPE_HEADER = 0;
        private final int VIEW_TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == VIEW_TYPE_HEADER){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_list, parent, false);
                return new DiseaseListFragment.HeaderViewHolder(view);
            }else if(viewType == VIEW_TYPE_ITEM){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_disease_list, parent, false);
                return new DiseaseListFragment.ViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
            if(holder instanceof ViewHolder){
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.name.setText(diseaseName.get(position).getName());
                viewHolder.name.setTypeface(tf);
                viewHolder.type.setText(diseaseName.get(position).getType());
                viewHolder.type.setTypeface(tf);
                System.out.println(diseaseName.get(position).getName()+" "+diseaseName.get(position).getType());
                viewHolder.img.setBackgroundResource(gridViewImageId[Arrays.asList(gridViewString).indexOf(diseaseName.get(position).getType().split(",")[0].trim())]);
                viewHolder.img.setImageResource(gridViewImageId2[Arrays.asList(gridViewString).indexOf(diseaseName.get(position).getType().split(",")[0].trim())]);
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                        if (!isLongClick) {
                            SelectItemFragment fragment = new SelectItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name",diseaseName.get(position).getName());
                            bundle.putString("type",diseaseName.get(position).getType());
                            bundle.putInt("val",1);
                            fragment.setArguments(bundle);
                            getParentFragment().getFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });

            }else if(holder instanceof  HeaderViewHolder){
                HeaderViewHolder headerHolder = (HeaderViewHolder)holder;
                headerHolder.name.setText(diseaseName.get(position).getName());
                headerHolder.name.setTypeface(tf);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return diseaseName.get(position).getName().length() == 1 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return diseaseName.size();
        }

        @Override
        public Object[] getSections() {
            String letters = "[ก-ฮ]";
            mDataArray = diseaseName;
            List<String> sections = new ArrayList<>(45);
            mSectionPositions = new ArrayList<>(45);
            for (int i = 0, size = mDataArray.size(); i < size; i++) {
                String section = String.valueOf(mDataArray.get(i).getName().charAt(0)).toUpperCase();
                if(!section.matches(letters)){
                    section = "#";//String.valueOf(mDataArray.get(i).getName().charAt(1)).toUpperCase();
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
