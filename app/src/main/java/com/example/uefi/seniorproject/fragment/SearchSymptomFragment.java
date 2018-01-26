package com.example.uefi.seniorproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.breakiterator.LongLexTo;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by UEFI on 21/1/2561.
 */

public class SearchSymptomFragment extends Fragment implements SearchView.OnQueryTextListener {
    DBHelperDAO dbHelperDAO;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<String> dictList, stopwordList, idfDoc, docLength;
    ArrayList<Double> keywordSymptom;
    ArrayList<Symptom> allSymptoms, mainSymptoms;
    ArrayList<ArrayList<String>> vectordata;
    ArrayList<Double> queryDotDoc;
    ArrayList<String> diseaseName, diseaseNameDefault;
    ArrayList<Pair<Double, String>> simDoc;
    double queryLength, sum;
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

        searchView = (SearchView) view.findViewById(R.id.searchSymptom);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหา");
        searchView.setOnQueryTextListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.searchSymptomList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void initialQueryVector() {
        keywordSymptom = new ArrayList<>();
        for (int i = 0; i < mainSymptoms.size(); i++) {
            keywordSymptom.add(0.0);
        }
    }

    public void resetSearch() {
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
            if (!(output.equals(null) || output.equals(""))) {
                System.out.println("output = " + output);
                allSymptoms = dbHelperDAO.getAllSymptoms();
                System.out.println("allsymptomsize = " + allSymptoms.size());
                String[] keyword = output.split("-");
                System.out.println("keywordlength = " + keyword.length);

                // initail query vector
                initialQueryVector();

                ArrayList<Integer> indexList = new ArrayList<>();
                // 1. check freq of query vector with mainsymptoms
                int index = 0;
                for (int i = 0; i < keyword.length; i++) {
                    System.out.println("keyword[i] = " + keyword[i]);
                    indexList = dbHelperDAO.getIndexSymptom(dbHelperDAO.checkKeyword(keyword[i]));
                    for(int j = 0; j< indexList.size(); j++){
                        System.out.println("Index = "+index);
                        keywordSymptom.set(indexList.get(j),keywordSymptom.get(indexList.get(j)) + 1);
                    }
                }

                System.out.println("keywordSymptom = " + keywordSymptom.toString());

                // 2. normalize query and calculate weight of terms
                queryLength = 0.0;
                for (int i = 0; i < keywordSymptom.size(); i++) {
                    if (keywordSymptom.get(i) > 0.0) {
                        System.out.println("keyword = " + keywordSymptom.get(i) + " ");
                        System.out.println("keyword = " + Double.parseDouble(idfDoc.get(i)) + " ");
                        keywordSymptom.set(i, (Math.log10(keywordSymptom.get(i)) + 1) * Double.parseDouble(idfDoc.get(i)));
                        // calculate query vector length
                        queryLength += Math.pow(keywordSymptom.get(i), 2);
                    }
                }
                queryLength = Math.sqrt(queryLength);
                System.out.println("keywordSymptom = " + keywordSymptom.toString());

                // 3. dot product between query vector and documents vector
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

                // 4. calculate similarity
                simDoc = new ArrayList<>();
                for (int i = 0; i < vectordata.size(); i++) {
                    simDoc.add(new Pair<Double, String>(queryDotDoc.get(i) / (queryLength * Double.parseDouble(docLength.get(i))), diseaseName.get(i)));
                }
                System.out.println("simDoc = " + simDoc.toString());

                // 5. sort documents
                Collections.sort(simDoc, new Comparator<Pair<Double, String>>() {
                    @Override
                    public int compare(Pair<Double, String> t0, Pair<Double, String> t1) {
                        if (t0.first < t1.first) return 1;
                        else if (t0.first > t1.first) return -1;
                        else return 0;
                    }
                });
                System.out.println("simDoc = " + simDoc.toString());

                for (int i = 0; i < simDoc.size(); i++) {
                    System.out.println("simDoc = " + simDoc.get(i).first + " " + simDoc.get(i).second);
                    if (simDoc.get(i).first > 0.0) filteredValues.add(simDoc.get(i).second);
                }
//                System.out.println(filteredValues.toString());
                diseaseName = filteredValues;
            } else diseaseName = diseaseNameDefault;

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
