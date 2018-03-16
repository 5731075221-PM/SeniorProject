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
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by UEFI on 14/3/2561.
 */

public class DiseaseListFragment extends Fragment{
    DBHelperDAO dbHelperDAO;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    IndexFastScrollRecyclerView recyclerView;
    ArrayList<String> diseaseName;
    private ArrayList<Integer> mSectionPositions;
    private List<String> mDataArray;

    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ",
            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช", "ระบบน้ำเหลือง", "ระบบกล้ามเนื้อและกระดูก", "อื่นๆ"
    } ;

    int[] gridViewImageId = {
            R.drawable.ic_disease1_select, R.drawable.ic_disease2_select, R.drawable.ic_disease3_select, R.drawable.ic_disease4_select, R.drawable.ic_disease5_select,
            R.drawable.ic_disease6_select, R.drawable.ic_disease7_select, R.drawable.ic_disease8_select, R.drawable.ic_disease9_select, R.drawable.ic_disease10_select,
            R.drawable.ic_disease11_select, R.drawable.ic_disease12_select, R.drawable.ic_disease13_select, R.drawable.ic_disease14_select
            , R.drawable.ic_disease15_select, R.drawable.ic_disease16_select, R.drawable.ic_disease17_select, R.drawable.ic_disease18_select, R.drawable.ic_disease19_select
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);

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

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        diseaseName = dbHelperDAO.getDiseaseName();
        sortList();
    }

    public void sortList(){
        String letters = "[ก-ฮ]";
        List<String> sect = new ArrayList<>(44);
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < diseaseName.size(); i++) {
            String section = String.valueOf(diseaseName.get(i).charAt(0)).toUpperCase();
            if(!section.matches(letters)){
                section = String.valueOf(diseaseName.get(i).charAt(1)).toUpperCase();
            }
            if (!sect.contains(section)) {
                sect.add(section);
                temp.add(section);
            }
            temp.add(diseaseName.get(i));
        }
        diseaseName = new ArrayList<>(temp);
        for(int i = 0;i< diseaseName.size();i++){
            System.out.println(diseaseName.get(i));
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
            if(holder instanceof ViewHolder){
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
                String type = dbHelperDAO.getDiseaseType(diseaseName.get(position));
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.name.setText(diseaseName.get(position));
                viewHolder.name.setTypeface(tf);
                viewHolder.type.setText(type);
                viewHolder.type.setTypeface(tf);
                viewHolder.img.setImageResource(gridViewImageId[Arrays.asList(gridViewString).indexOf(type.split(",")[0])]);
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                        if (!isLongClick) {
                            System.out.println("ABC = "+diseaseName.get(position));
                            SelectItemFragment fragment = new SelectItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name",diseaseName.get(position));
                            fragment.setArguments(bundle);
                            //getFragmentManager().beginTransaction()

                            getParentFragment().getFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });

            }else if(holder instanceof  HeaderViewHolder){
                HeaderViewHolder headerHolder = (HeaderViewHolder)holder;
                headerHolder.name.setText(diseaseName.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return diseaseName.get(position).length() == 1 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return diseaseName.size();
        }

        @Override
        public Object[] getSections() {
            String letters = "[ก-ฮ]";
            mDataArray = diseaseName;
            List<String> sections = new ArrayList<>(44);
            mSectionPositions = new ArrayList<>(44);
            for (int i = 0, size = mDataArray.size(); i < size; i++) {
                String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
                if(!section.matches(letters)){
                    section = String.valueOf(mDataArray.get(i).charAt(1)).toUpperCase();
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
