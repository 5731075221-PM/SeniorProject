package com.example.uefi.seniorproject.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.uefi.seniorproject.MainActivity;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.viewpagerindicator.CirclePageIndicator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by UEFI on 27/12/2560.
 */

public class MainFragment extends Fragment /*implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener*/{
    DBHelperDAO dbHelperDAO;
    AppBarLayout appBarLayout;
    ViewPager mPager;
    PagerAdapterSlider adapter;
    int currentPage = 0, NUM_PAGES = 0;
    String d = "";
    ArrayList<String> imageList = new ArrayList<>(), titleList = new ArrayList<>(), detailList = new ArrayList<>(),
            linkList = new ArrayList<>(), content = new ArrayList<>();
    SliderLayout mDemoSlider;
    LocationManager locationManager;
    ConnectivityManager connectionManager;
    boolean isNetwork;
    ActionBarDrawerToggle toggle;
    public TextView textTool;
    ProgressDialog progressBar;
    Handler handler = new Handler();
    Runnable Update;
    Timer swipeTimer = new Timer();

    public class FetchData extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage("กรุณารอสักครู่...");
            progressBar.setIndeterminate(false);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        public void getData(String attr){
            try {
                Document doc = Jsoup.connect(attr + "").get();
                d = "";
                boolean found = false;
                for (Element div : doc.select("div.show_catlist")) {
                    Elements text = div.select("p");
                    System.out.println("size = " + text.size());
                    if(!text.isEmpty()) {
                        for (Element i : text.subList(0, text.size() - 1)) {
                            if (i.text().contains("แฟ้มภาพ")) {
                                found = true;
                                d = "";
                            } else if (found) {
                                d += "\t" + i.text();
                                d += "\n\n";
                                System.out.println("DET " + i.text());
                            } else {
                                d += "\t" + i.text();
                                d += "\n\n";
                                System.out.println("DET " + i.text());
                            }
                        }
                    }
                }
                content.add(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int index = 0;
                imageList = new ArrayList<>();
                titleList = new ArrayList<>();
                Document doc = Jsoup.connect("http://www.thaihealth.or.th/categories/2/1/73-%E0%B8%82%E0%B9%88%E0%B8%B2%E0%B8%A7%E0%B8%AA%E0%B8%B8%E0%B8%82%E0%B8%A0%E0%B8%B2%E0%B8%9E.html").get();

                for(Element div : doc.select("div.categories_bigshow")){
                    Elements img = div.getElementsByTag("img");
                    System.out.println("AAA "+img);
                    for(Element image : img){
                        System.out.println("AAA "+image.absUrl("src"));
                        imageList.add(image.absUrl("src"));
                    }
                    img = div.select("a.images_big");
                    System.out.println("TEst "+img.attr("abs:href"));
                    linkList.add(img.attr("abs:href"));
                    getData(img.attr("abs:href"));

                    Elements header = div.select("div.des_r_top");
                    Elements date = div.select("span.date_start");
                    for(Element h : header){
                        System.out.println("AAA "+h.select("a").attr("title"));
                        System.out.println("AAA "+h.select("span").text());
                        titleList.add(h.select("a").attr("title").replace("'","")+"&"+date.text());
                        System.out.println("AAA "+h.select("a").attr("title")+"&"+date.text());
                        detailList.add(h.select("span").text());
                    }
                }

                for(Element div : doc.select("div.categories_list_show")){
                    Elements detail = div.select("li");;
                    for(Element d : detail.subList(0,4)){
                        Elements e = d.getElementsByTag("img");
                        for(Element f : e){
                            System.out.println("BBB "+f.absUrl("src"));
                            imageList.add(f.absUrl("src"));
                        }
                        e = d.select("li > a");
                        Elements m = d.select("span.date_sub_content");
                        System.out.println("CCC "+e.attr("title")+"&"+m.text());
                        titleList.add(e.attr("title").replace("\\'","")+"&"+m.text());
                        e = d.select("li > p");
                        System.out.println("DDD "+e.text());
                        detailList.add(e.text());
                        e = d.select("a.text_content");
                        System.out.println("DDD "+e.attr("abs:href"));
                        linkList.add(e.attr("abs:href"));
                        getData(e.attr("abs:href"));
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
            adapter = new PagerAdapterSlider(getActivity(),getActivity().getSupportFragmentManager(),imageList, titleList, detailList, linkList);
            mPager = (ViewPager) getActivity().findViewById(R.id.pagerSlider);
            mPager.setAdapter(adapter);

            CirclePageIndicator indicator = (CirclePageIndicator) getActivity().findViewById(R.id.indicator);
            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;
            indicator.setRadius(5 * density);
            NUM_PAGES = imageList.size();

            // Auto start of viewpager
//            final Handler handler = new Handler();
            Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
//            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });
            System.out.println("SSS = "+titleList.size()+" "+content.size()+" "+detailList.size()+" "+linkList.size());
//            for(int i = 0; i<titleList.size();i++){
//                dbHelperDAO.storeNews(i,titleList.get(i),content.get(i),detailList.get(i),linkList.get(i));
//            }
            progressBar.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        appBarLayout.setExpanded(true, true);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);

//        if(imageList.size() == 5 && detailList.size() == 5 && imageList.size() == 5 && linkList.size() == 5){
//            adapter = new PagerAdapterSlider(getActivity(),getActivity().getSupportFragmentManager(),imageList, titleList, detailList, linkList);
//            mPager = (ViewPager) getActivity().findViewById(R.id.pagerSlider);
//            mPager.setAdapter(adapter);
//
//            CirclePageIndicator indicator = (CirclePageIndicator) getActivity().findViewById(R.id.indicator);
//            indicator.setViewPager(mPager);
//
//            final float density = getResources().getDisplayMetrics().density;
//            indicator.setRadius(5 * density);
//            NUM_PAGES = imageList.size();
//
//            // Auto start of viewpager
//            final Handler handler = new Handler();
//            final Runnable Update = new Runnable() {
//                public void run() {
//                    if (currentPage == NUM_PAGES) {
//                        currentPage = 0;
//                    }
//                    mPager.setCurrentItem(currentPage++, true);
//                }
//            };
//            Timer swipeTimer = new Timer();
//            swipeTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, 3000, 3000);
//
//            // Pager listener over indicator
//            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                @Override
//                public void onPageSelected(int position) {
//                    currentPage = position;
//                }
//
//                @Override
//                public void onPageScrolled(int pos, float arg1, int arg2) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int pos) {
//
//                }
//            });
//        }

//        init();
//        new FetchData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        adapter = new PagerAdapterSlider(getActivity(),imageList, titleList);
//        mPager = (ViewPager) view.findViewById(R.id.pagerSlider);
//        mPager.setAdapter(adapter);
//
//        CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
//        indicator.setViewPager(mPager);
//
//        final float density = getResources().getDisplayMetrics().density;
//        indicator.setRadius(5 * density);
//        NUM_PAGES = imageList.size();
//
//        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);
//
//        // Pager listener over indicator
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage = position;
//
//            }
//
//            @Override
//            public void onPageScrolled(int pos, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int pos) {
//
//            }
//        });


//        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);
//
//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
//        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
//        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
//
//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Hannibal",R.drawable.ic_cause_selected);
//        file_maps.put("Big Bang Theory",R.drawable.ic_disease1_selected);
//        file_maps.put("House of Cards",R.drawable.ic_disease2_selected);
//        file_maps.put("Game of Thrones", R.drawable.ic_disease3_selected);
//
//        for(String name : file_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(getActivity());
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);
//        mDemoSlider.addOnPageChangeListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        textTool.setText("Mymor");

        if(titleList.size() != 0) {
            adapter = new PagerAdapterSlider(getActivity(),getActivity().getSupportFragmentManager(),imageList, titleList, detailList, linkList);
            mPager = (ViewPager) getActivity().findViewById(R.id.pagerSlider);
            mPager.setAdapter(adapter);
//
            CirclePageIndicator indicator = (CirclePageIndicator) getActivity().findViewById(R.id.indicator);
            indicator.setViewPager(mPager);
//
            final float density = getResources().getDisplayMetrics().density;
            indicator.setRadius(5 * density);
            NUM_PAGES = imageList.size();
//
//            // Auto start of viewpager
////            Handler handler = new Handler();
//            Update = new Runnable() {
//                public void run() {
//                    if (currentPage == NUM_PAGES) {
//                        currentPage = 0;
//                    }
//                    mPager.setCurrentItem(currentPage++, true);
//                }
//            };
////            Timer swipeTimer = new Timer();
//            swipeTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, 3000, 3000);
//
//            // Pager listener over indicator
//            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                @Override
//                public void onPageSelected(int position) {
//                    currentPage = position;
//                }
//
//                @Override
//                public void onPageScrolled(int pos, float arg1, int arg2) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int pos) {
//
//                }
//            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(Update);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(Update);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

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
            titleList = dbHelperDAO.getHeaderNews();
            detailList = dbHelperDAO.getDetailNews();
            linkList = dbHelperDAO.getLinkNews();
            imageList.add("@color/grey");
            imageList.add("@color/grey");
            imageList.add("@color/grey");
            imageList.add("@color/grey");
            imageList.add("@color/grey");
//            imageList.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//            imageList.add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
//            imageList.add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
//            imageList.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//            imageList.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//            titleList.add("");
//            titleList.add("");
//            titleList.add("");
//            titleList.add("");
//            titleList.add("");
        }
    }

    //    @Override
//    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        Toast.makeText(getActivity(),"Page Changed: " + position + "",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    @Override
//    public void onStop() {
//        mDemoSlider.stopAutoCycle();
//        super.onStop();
//    }
}

