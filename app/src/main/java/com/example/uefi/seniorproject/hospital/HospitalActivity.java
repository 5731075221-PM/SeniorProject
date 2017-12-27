//package com.example.uefi.seniorproject.hospital;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.Pair;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.example.uefi.seniorproject.MainActivity;
//import com.example.uefi.seniorproject.R;
//import com.example.uefi.seniorproject.databases.DBHelperDAO;
//
//import java.util.ArrayList;
//
//public class HospitalActivity extends AppCompatActivity {
//    ListView hospitalView;
//    ArrayList<String> nameList;
//    ArrayList<Pair<Double,Double>> latlngList;
//    ArrayList<Pair<String,String>> addrPhone;
//    private RecyclerView recyclerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hospital);
//
//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//
//        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(new RecyclerViewAdapter());
//
//        /*listview
//        hospitalView = (ListView) findViewById(R.id.hospital_list);*/
//
//        DBHelperDAO dbHelperDAO = DBHelperDAO.getInstance(this);
//        dbHelperDAO.open();
//
//        nameList = dbHelperDAO.getNameHospital();
//        latlngList = dbHelperDAO.getLatLng();
//        addrPhone = dbHelperDAO.getAddrPhone();
//
//        dbHelperDAO.close();
//
//        /*listview
//        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), nameList);
//        hospitalView.setAdapter(adapter);
//        hospitalView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent;
//                intent = new Intent(getApplicationContext(), HospitalItem.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("itemHospital", nameList.get(i));
//                bundle.putDouble("lat",latlngList.get(i).first);
//                bundle.putDouble("lng",latlngList.get(i).second);
//                bundle.putString("address",addrPhone.get(i).first);
//                bundle.putString("phone",addrPhone.get(i).second);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });*/
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
//            , View.OnLongClickListener, View.OnTouchListener{
//        TextView name;
//        private ItemClickListener itemClickListener;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.textView1);
//            itemView.setOnClickListener(this);
//        }
//
//        public void setOnClickListener(ItemClickListener itemClickListener) {
//            this.itemClickListener = itemClickListener;
//        }
//
//        @Override
//        public void onClick(View view) {
//            itemClickListener.onClick(view, getAdapterPosition(), false, null);
//        }
//
//        @Override
//        public boolean onLongClick(View view) {
//            itemClickListener.onClick(view, getAdapterPosition(), true, null);
//            return true;
//        }
//
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            itemClickListener.onClick(view, getAdapterPosition(), false, motionEvent);
//            return true;
//        }
//    }
//
//    public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_hospital_list,parent,false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.name.setText(nameList.get(position));
//            holder.setOnClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
//                    if(!isLongClick){
//                        Intent intent;
//                        intent = new Intent(getApplicationContext(), HospitalItem.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("itemHospital", nameList.get(position));
//                        bundle.putDouble("lat",latlngList.get(position).first);
//                        bundle.putDouble("lng",latlngList.get(position).second);
//                        bundle.putString("address",addrPhone.get(position).first);
//                        bundle.putString("phone",addrPhone.get(position).second);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return nameList.size();
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home) finish();
//            return super.onOptionsItemSelected(item);
//    }
//}
