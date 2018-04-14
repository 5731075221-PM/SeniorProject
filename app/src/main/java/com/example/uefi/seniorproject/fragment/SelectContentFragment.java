package com.example.uefi.seniorproject.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uefi.seniorproject.MainActivity;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by UEFI on 10/4/2561.
 */

public class SelectContentFragment extends Fragment {
    DBHelperDAO dbHelperDAO;
    TextView header, detail, date;
    ImageView image;
    String url, h, d = "",dy = "";
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    LocationManager locationManager;
    ConnectivityManager connectionManager;
    boolean isNetwork;

    public class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(getArguments().getString("link") + "").get();
//                System.out.println(doc.toString());
                for (Element div : doc.select("div.show_catlist > h1")) {
                    Elements img = div.getElementsByTag("img");
                    for (Element i : img) {
                        System.out.println("AAA " + i.absUrl("src"));
                        url = i.absUrl("src");
                    }
                }

                System.out.println(doc.select("div.show_catlist"));

                boolean found = false;
                for (Element div : doc.select("div.show_catlist")) {
                    Elements text = div.select("p");
                    System.out.println("size = " + text.size());
                    for (Element i : text.subList(0, text.size()-1)) {
                        if (i.text().contains("แฟ้มภาพ")) found = true;
                        else if(found){
                            d += "\t"+i.text();
                            d += "\n\n";
                            System.out.println("DET " + i.text());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            header.setText(h);
            detail.setText(d);
            date.setText(dy);
            Picasso.with(getActivity()).load(url).into(image);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_content, container, false);

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = getActivity().findViewById(R.id.toolbar);


        drawer = (DrawerLayout) (getActivity()).findViewById(R.id.drawer_layout);


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle( getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!mToolBarNavigationListenerIsRegistered) {
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


//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().onBackPressed();
//            }
//        });


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
        header = view.findViewById(R.id.headerNews);
        detail = view.findViewById(R.id.detailNews);
        detail.setTypeface(tf);
        image = view.findViewById(R.id.imageNews);
        date = view.findViewById(R.id.dateNews);
        date.setTypeface(tf);

        header.setText(h);
        detail.setText(d);
        image.setBackgroundResource(R.color.grey);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        header = (TextView) getActivity().findViewById(R.id.headerNews);
        detail = (TextView) getActivity().findViewById(R.id.detailNews);
        image = (ImageView) getActivity().findViewById(R.id.imageNews);
        date = (TextView) getActivity().findViewById(R.id.dateNews);

        h = getArguments().getString("header").split("&")[0];
        dy = getArguments().getString("header").split("&")[1];

        System.out.println("PRint "+h+" "+dy);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        connectionManager = (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);
        if (connectionManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connectionManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectionManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectionManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            isNetwork = true;
        } else isNetwork = false;

        if(isNetwork) new FetchData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            d = dbHelperDAO.getContentNews(h);
            System.out.println("PRint "+d);
        }
    }

}
