package com.example.uefi.seniorproject.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by UEFI on 4/4/2561.
 */

public class FavoriteItemFragment extends Fragment {
    DBHelperDAO dbHelperDAO;
    ArrayList<Object> list;
    RecyclerView recyclerView;
    TextView empty, title;
    private RecyclerViewAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_favorite_item, container, false);

        title.setText("รายการลัด");
        empty = (TextView) view.findViewById(R.id.textEmptyFav) ;
        if(list.isEmpty()) empty.setVisibility(View.VISIBLE);
        else empty.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerFavList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        title = getActivity().findViewById(R.id.textTool);
        list = dbHelperDAO.getAllFavList();
        addHeader();
        System.out.println("list.size " + list.size());
    }

    public void addHeader(){
        ArrayList<Object> tmp = new ArrayList<>();
        if(list.size() == 0) return;
        boolean hos = false, dis = false;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) instanceof Hospital) {
                if(!hos) {
                    tmp.add("โรงพยาบาล");
                    hos = true;
                }
                tmp.add(list.get(i));
            }
            else {
                tmp.add("โรค");
                tmp.addAll(list.subList(i,list.size()));
                break;
            }
        }
        list = tmp;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.headerItem);
        }
    }

    public class DiseaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, type;
        ImageView img;
        private ItemClickListener itemClickListener;

        public DiseaseViewHolder(View itemView) {
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

    public class HospitalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, distance, type;
        ImageView image;
        private ItemClickListener itemClickListener;

        public HospitalViewHolder(View itemView) {
            super(itemView);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
            image = (ImageView)itemView.findViewById(R.id.imageView1);
            image.setImageResource(R.drawable.ic_hospital_cross);
            name = (TextView) itemView.findViewById(R.id.textView1);
            name.setTypeface(tf);
            distance = (TextView) itemView.findViewById(R.id.distanceHospital);
            type = (TextView) itemView.findViewById(R.id.typeHospital);
            type.setTypeface(tf);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_TYPE_DISEASE = 0;
        private final int VIEW_TYPE_HOSPITAL = 1;
        private final int VIEW_TYPE_HEADER = 2;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == VIEW_TYPE_DISEASE){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_disease_list, parent, false);
                return new FavoriteItemFragment.DiseaseViewHolder(view);
            }else if(viewType == VIEW_TYPE_HOSPITAL){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
                return new FavoriteItemFragment.HospitalViewHolder(view);
            }else if(viewType == VIEW_TYPE_HEADER){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_list, parent, false);
                return new FavoriteItemFragment.HeaderViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
            if(holder instanceof HospitalViewHolder){
                Hospital h = (Hospital) list.get(position);
                HospitalViewHolder viewHolder = (HospitalViewHolder) holder;
                if(h.getType().equals("รัฐบาล")) {
                    viewHolder.image.getDrawable().setColorFilter(Color.parseColor("#3f51b5"), PorterDuff.Mode.SRC_IN);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                } else if(h.getType().equals("เอกชน")) {
                    viewHolder.image.getDrawable().setColorFilter(Color.parseColor("#43bfc7"),PorterDuff.Mode.SRC_IN);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.colorMacawBlueGreen));
                } else if(h.getType().equals("ชุมชน")) {
                    viewHolder.image.getDrawable().setColorFilter(Color.parseColor("#4cd29f"),PorterDuff.Mode.SRC_IN);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.colorGreen));
                } else if(h.getType().equals("ศูนย์")) {
                    viewHolder.image.getDrawable().setColorFilter(Color.parseColor("#79ccd0"),PorterDuff.Mode.SRC_IN);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.colorBlue));
                } else {
                    viewHolder.image.getDrawable().setColorFilter(Color.parseColor("#3f6fb7"),PorterDuff.Mode.SRC_IN);
                    viewHolder.distance.setTextColor(getActivity().getResources().getColor(R.color.colorBlue2));
                }
                viewHolder.name.setText(h.getName());
//                viewHolder.distance.setText(h.getDistance());
                viewHolder.type.setText(h.getType());
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                        if (!isLongClick) {
                            HospitalItemFragment fragment = new HospitalItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name", ((Hospital)list.get(position)).getName());
                            bundle.putDouble("lat", ((Hospital)list.get(position)).getLat());
                            bundle.putDouble("lng", ((Hospital)list.get(position)).getLng());
                            bundle.putString("address", ((Hospital)list.get(position)).getAddress());
                            bundle.putString("phone", ((Hospital)list.get(position)).getPhone());
                            bundle.putString("website", ((Hospital)list.get(position)).getWebsite());
                            bundle.putString("type",((Hospital)list.get(position)).getType());
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
            }else if(holder instanceof DiseaseViewHolder){
                DiseaseViewHolder viewHolder = (DiseaseViewHolder) holder;
                viewHolder.name.setText(((Disease)list.get(position)).getName());
                viewHolder.name.setTypeface(tf);
                viewHolder.type.setText(((Disease)list.get(position)).getType());
                viewHolder.type.setTypeface(tf);
                viewHolder.img.setImageResource(gridViewImageId[Arrays.asList(gridViewString).indexOf(((Disease)list.get(position)).getType().split(",")[0])]);
                viewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                        if (!isLongClick) {
                            SelectItemFragment fragment = new SelectItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name",((Disease)list.get(position)).getName());
                            bundle.putString("type",((Disease)list.get(position)).getType());
                            fragment.setArguments(bundle);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
            }else if(holder instanceof HeaderViewHolder){
                HeaderViewHolder headerHolder = (HeaderViewHolder)holder;
                headerHolder.name.setText(list.get(position).toString());
                headerHolder.name.setTypeface(tf);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position) instanceof Hospital ? VIEW_TYPE_HOSPITAL : (list.get(position) instanceof Disease) ? VIEW_TYPE_DISEASE : VIEW_TYPE_HEADER;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
