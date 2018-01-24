package com.example.uefi.seniorproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.breakiterator.LongLexTo;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.hospital.ItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by UEFI on 21/1/2561.
 */

public class SearchSymptomFragment extends Fragment implements SearchView.OnQueryTextListener{
    DBHelperDAO dbHelperDAO;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<String> dictList, stopwordList, idfDoc, docLength;
    ArrayList<Double> keywordSymptom;
    ArrayList<Symptom> allSymptoms,mainSymptoms;
    ArrayList<ArrayList<String>> vectordata;
    ArrayList<Double> queryDotDoc, simDoc;
    ArrayList<String> diseaseName,diseaseNameDefault;
    double queryLength,sum;
    String search = "";
    final String query = "ไข";
    LongLexTo tokenizer;

    public SearchSymptomFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_symptom, container, false);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_MOVE){
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//                    searchView.clearFocus();
//                }
//                return true;
//            }
//        });
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        dictList = dbHelperDAO.getLexitron();
        stopwordList = dbHelperDAO.getStopword();
        mainSymptoms = dbHelperDAO.getMainSymptoms();
        vectordata = dbHelperDAO.getVectorData();
        docLength = dbHelperDAO.getDocLength();
        idfDoc = dbHelperDAO.getFreq();
        diseaseName = dbHelperDAO.getDiseaseName();
        diseaseNameDefault = diseaseName;

        try {
            tokenizer = new LongLexTo(dictList, stopwordList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchView=(SearchView) view.findViewById(R.id.searchSymptom);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหา");
        searchView.setOnQueryTextListener(this);

        recyclerView = (RecyclerView)view.findViewById(R.id.searchSymptomList);
        recyclerView.setAdapter(adapter);

//        Button search = (Button)view.findViewById(R.id.searchSymptopm);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    LongLexTo tokenizer = new LongLexTo(dictList,stopwordList);
//                    String output = tokenizer.genOutput(query.trim(),"nat.html",tokenizer);
//                    Log.i("myvalue = ",output);
//                    allSymptoms = dbHelperDAO.getAllSymptoms();
//                    Log.i("myvalue = ",allSymptoms.size()+"");
//                    String[] keyword = output.split("-");
//                    Log.i("myvalue = ",keyword.length+" ");
//
//                    // initail query vector
//                    for(int i = 0; i< mainSymptoms.size(); i++){
//                        keywordSymptom.add(0.0);
//                    }
//
//                    // check freq of query vector with mainsymptoms
//                    for(int i = 0; i<keyword.length;i++){
//                        for(int j = 0; j<mainSymptoms.size(); j++){
//                            if(keyword[i].equals(mainSymptoms.get(j).getWord()) || (mainSymptoms.get(j).getWord().contains(keyword[i]))){
//                                keywordSymptom.set(j,keywordSymptom.get(j)+1);
////                                Log.d("keyword = ",keywordSymptom.get(j)+"");
//                            }
//                        }
//                    }
//                    System.out.println("keywordSymptom = "+keywordSymptom.toString());
//
//                    //normalize query and calculate weight of terms
//                    for(int i = 0; i<keywordSymptom.size();i++){
//                        if(keywordSymptom.get(i) > 0){
//                            keywordSymptom.set(i,(Math.log10(keywordSymptom.get(i)) + 1) * Double.parseDouble(idfDoc.get(i)));
//                            // calculate query vector length
//                            queryLength += Math.pow(keywordSymptom.get(i),2);
////                            System.out.println("keyword = "+keywordSymptom.get(i)+" ");
//                        }
//                    }
//                    queryLength = Math.sqrt(queryLength);
//                    System.out.println("keywordSymptom = "+keywordSymptom.toString());
//
//                    // dot product between query vector and documents vector
//                    double sum;
//                    for(int i = 0; i<sizeDoc; i++){
//                        sum = 0.0;
//                        for(int j = 0; j<vectordata.get(i).size(); j++){
//                            if((Double.parseDouble(vectordata.get(i).get(j)) > 0.0) && (keywordSymptom.get(j) > 0.0)){
//                                sum += Double.parseDouble(vectordata.get(i).get(j))*keywordSymptom.get(j);
//                            }
//                        }
//                        queryDotDoc.add(sum);
//                    }
//                    System.out.println("queryDotDoc = "+queryDotDoc.toString());
//
//                    // calculate similarity
//                    for(int i = 0; i<sizeDoc;i++){
//                        simDoc.add(queryDotDoc.get(i)/(queryLength*Double.parseDouble(docLength.get(i))));
//                    }
//                    System.out.println(simDoc.toString());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return view;
    }

    public void initialQueryVector(){
        keywordSymptom = new ArrayList<>();
        for (int i = 0; i < mainSymptoms.size(); i++) {
            keywordSymptom.add(0.0);
        }
    }

    public void resetSearch(){
        diseaseName = diseaseNameDefault;
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        ArrayList<String> filteredValues = new ArrayList<>();
        diseaseName = diseaseNameDefault;
        try {
            String output = tokenizer.genOutput(newText.trim(), "nat.html", tokenizer);
            if(!(output.equals(null) || output.equals(""))){
                System.out.println("output = "+output);
                allSymptoms = dbHelperDAO.getAllSymptoms();
                System.out.println("allsymptomsize = "+allSymptoms.size());
                String[] keyword = output.split("-");
                System.out.println("keywordlength = "+keyword.length);

                // initail query vector
                initialQueryVector();

                // check freq of query vector with mainsymptoms
                int index = 0;
                for (int i = 0; i < keyword.length; i++) {
                    System.out.println("keyword[i] = "+keyword[i]);
                    if((index = dbHelperDAO.checkKeyword(keyword[i])) != -1){
                        System.out.println("Index = "+(index-1));
                        keywordSymptom.set(index-1,keywordSymptom.get(index-1) + 1);
                    }
                }
//                for (int i = 0; i < keyword.length; i++) {
//                    for (int j = 0; j < mainSymptoms.size(); j++) {
//                        System.out.println("keyword[i] = "+keyword[i]);
//                        System.out.println("mainSymptoms.get(j).getWord() = "+mainSymptoms.get(j).getWord());
//                        if (keyword[i].equals(mainSymptoms.get(j).getWord()) || (mainSymptoms.get(j).getWord().contains(keyword[i]))) {
//                            System.out.println("Match");
//                            keywordSymptom.set(j, keywordSymptom.get(j) + 1);
//                            //                                Log.d("keyword = ",keywordSymptom.get(j)+"");
//                        }
//                    }
//                }
                System.out.println("keywordSymptom = " + keywordSymptom.toString());

                //normalize query and calculate weight of terms
                queryLength = 0.0;
                for (int i = 0; i < keywordSymptom.size(); i++) {
                    if (keywordSymptom.get(i) > 0) {
                        keywordSymptom.set(i, (Math.log10(keywordSymptom.get(i)) + 1) * Double.parseDouble(idfDoc.get(i)));
                        // calculate query vector length
                        queryLength += Math.pow(keywordSymptom.get(i), 2);
                        //                            System.out.println("keyword = "+keywordSymptom.get(i)+" ");
                    }
                }
                queryLength = Math.sqrt(queryLength);
                System.out.println("keywordSymptom = " + keywordSymptom.toString());

                // dot product between query vector and documents vector
                queryDotDoc = new ArrayList<>();
                for (int i = 0; i < vectordata.size(); i++) {
                    sum = 0.0;
                    for (int j = 0; j < vectordata.get(i).size(); j++) {
                        if ((Double.parseDouble(vectordata.get(i).get(j)) > 0.0) && (keywordSymptom.get(j) > 0.0)) {
                            sum += Double.parseDouble(vectordata.get(i).get(j)) * keywordSymptom.get(j);
                        }
                    }
                    queryDotDoc.add(sum);
                }
                System.out.println("queryDotDoc = " + queryDotDoc.toString());

                // calculate similarity
                simDoc = new ArrayList<>();
                for (int i = 0; i < vectordata.size(); i++) {
                    simDoc.add(queryDotDoc.get(i) / (queryLength * Double.parseDouble(docLength.get(i))));
                }
                System.out.println("simDoc = " + simDoc.toString());

//                Collections.sort(simDoc);
                for(int i = 0; i<simDoc.size();i++){
                    System.out.println("simDoc = "+simDoc.get(i));
                    if(simDoc.get(i) > 0.0) filteredValues.add(diseaseName.get(i));
                }

                System.out.println(filteredValues.toString());
                diseaseName = filteredValues;
            }else diseaseName = diseaseNameDefault;

        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnLongClickListener, View.OnTouchListener {
        TextView name;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView1);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<SearchSymptomFragment.ViewHolder> {

        @Override
        public SearchSymptomFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_hospital_list, parent, false);
            return new SearchSymptomFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(diseaseName.get(position));
            holder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (!isLongClick) {
//                        HospitalItemFragment fragment = new HospitalItemFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("name", hospitalList.get(position).getName());
//                        bundle.putDouble("lat", hospitalList.get(position).getLat());
//                        bundle.putDouble("lng", hospitalList.get(position).getLng());
//                        bundle.putString("address", hospitalList.get(position).getAddress());
//                        bundle.putString("phone", hospitalList.get(position).getPhone());
//                        bundle.putString("website", hospitalList.get(position).getWebsite());
//                        fragment.setArguments(bundle);
//                        getFragmentManager().beginTransaction()
//                                .replace(R.id.container_fragment, fragment)
//                                .addToBackStack(null)
//                                .commit();
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
