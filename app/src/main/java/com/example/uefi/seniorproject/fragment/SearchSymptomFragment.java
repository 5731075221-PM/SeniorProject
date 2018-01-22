package com.example.uefi.seniorproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.breakiterator.LongLexTo;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by UEFI on 21/1/2561.
 */

public class SearchSymptomFragment extends Fragment {
    DBHelperDAO dbHelperDAO;
    ArrayList<String> dictList, stopwordList, idfDoc, docLength;
    ArrayList<Double> keywordSymptom = new ArrayList<>();
    ArrayList<Symptom> allSymptoms,mainSymptoms;
    ArrayList<ArrayList<String>> vectordata;
    ArrayList<Double> queryDotDoc, simDoc;
    double queryLength = 0.0;

    public SearchSymptomFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_symptom, container, false);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        dictList = dbHelperDAO.getLexitron();
        stopwordList = dbHelperDAO.getStopword();
        mainSymptoms = dbHelperDAO.getMainSymptoms();
        vectordata = dbHelperDAO.getVectorData();
        docLength = dbHelperDAO.getDocLength();
        idfDoc = dbHelperDAO.getFreq();
        queryDotDoc = new ArrayList<>();
        simDoc = new ArrayList<>();

        final int sizeDoc = vectordata.size();
        final String query = "ไข้สูงมาก รู้สึกหนาว";

        Button search = (Button)view.findViewById(R.id.searchSymptopm);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LongLexTo tokenizer = new LongLexTo(dictList,stopwordList);
                    String output = tokenizer.genOutput(query.trim(),"nat.html",tokenizer);
                    Log.i("myvalue = ",output);
                    allSymptoms = dbHelperDAO.getAllSymptoms();
                    Log.i("myvalue = ",allSymptoms.size()+"");
                    String[] keyword = output.split("-");
                    Log.i("myvalue = ",keyword.length+" ");

                    // initail query vector
                    for(int i = 0; i< mainSymptoms.size(); i++){
                        keywordSymptom.add(0.0);
                    }

                    // check freq of query vector with mainsymptoms
                    for(int i = 0; i<keyword.length;i++){
                        for(int j = 0; j<mainSymptoms.size(); j++){
                            if(keyword[i].equals(mainSymptoms.get(j).getWord()) || (mainSymptoms.get(j).getWord().contains(keyword[i]))){
                                keywordSymptom.set(j,keywordSymptom.get(j)+1);
//                                Log.d("keyword = ",keywordSymptom.get(j)+"");
                            }
                        }
                    }
                    System.out.println("keywordSymptom = "+keywordSymptom.toString());

                    //normalize query and calculate weight of terms
                    for(int i = 0; i<keywordSymptom.size();i++){
                        if(keywordSymptom.get(i) > 0){
                            keywordSymptom.set(i,(Math.log10(keywordSymptom.get(i)) + 1) * Double.parseDouble(idfDoc.get(i)));
                            // calculate query vector length
                            queryLength += Math.pow(keywordSymptom.get(i),2);
//                            System.out.println("keyword = "+keywordSymptom.get(i)+" ");
                        }
                    }
                    queryLength = Math.sqrt(queryLength);
                    System.out.println("keywordSymptom = "+keywordSymptom.toString());

                    // dot product between query vector and documents vector
                    double sum;
                    for(int i = 0; i<sizeDoc; i++){
                        sum = 0.0;
                        for(int j = 0; j<vectordata.get(i).size(); j++){
                            if((Double.parseDouble(vectordata.get(i).get(j)) > 0.0) && (keywordSymptom.get(j) > 0.0)){
                                sum += Double.parseDouble(vectordata.get(i).get(j))*keywordSymptom.get(j);
                            }
                        }
                        queryDotDoc.add(sum);
                    }
                    System.out.println("queryDotDoc = "+queryDotDoc.toString());

                    // calculate similarity
                    for(int i = 0; i<sizeDoc;i++){
                        simDoc.add(queryDotDoc.get(i)/(queryLength*Double.parseDouble(docLength.get(i))));
                    }
                    System.out.println(simDoc.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
