package com.example.uefi.seniorproject.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by UEFI on 5/2/2561.
 */

public class SelectItemFragment extends Fragment {
    String name = "";
//    ArrayList<String> list = new ArrayList<>();
    String list = "";
    TextView t;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_item, container, false);

        t = (TextView)view.findViewById(R.id.testtt);
        Bundle extraBundle = getArguments();
        if(!extraBundle.isEmpty()){
            name = extraBundle.getString("name");
        }
        new FetchData().execute();
        return view;
    }

    private class FetchData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("http://haamor.com/th/"+name).get();
                for(Element div : doc.select("h2, h2 ~ p, h2 ~ ul")){
                    System.out.println(div.tagName()+" "+div.text());
                    list += div.text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            t.setText(list);
        }
    }
}
