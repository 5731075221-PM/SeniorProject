package com.example.uefi.seniorproject.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uefi.seniorproject.MainActivity;
import com.example.uefi.seniorproject.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

/**
 * Created by UEFI on 10/4/2561.
 */

public class PagerAdapterSlider extends PagerAdapter {

    private ArrayList<String> IMAGES, TITLES, DETAIL, LINKS;
    private LayoutInflater inflater;
    private Context context;
    FragmentManager manager;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    public PagerAdapterSlider(Context context, FragmentManager manager, ArrayList<String> IMAGES, ArrayList<String> TITLES, ArrayList<String> DETAIL, ArrayList<String> LINKS) {
        this.context = context;
        this.IMAGES = IMAGES;
        this.TITLES = TITLES;
        this.DETAIL = DETAIL;
        this.LINKS = LINKS;
        this.manager = manager;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final int pos = position;
        View imageLayout = inflater.inflate(R.layout.custom_slider_layout, view, false);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("pos = "+pos);
                Bundle args = new Bundle();
                args.putString("link", LINKS.get(pos));
                args.putString("header",TITLES.get(pos));
                args.putString("image",IMAGES.get(pos));
                SelectContentFragment fragment = new SelectContentFragment();
                fragment.setArguments(args);
                manager.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        assert imageLayout != null;
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/THSarabunNew.ttf");
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageViewSlider);
        final TextView title = (TextView) imageLayout.findViewById(R.id.titleSlider);
        final TextView detail = (TextView) imageLayout.findViewById(R.id.detailSlider);
        Picasso.with(context).load(IMAGES.get(position)).into(imageView);
        title.setText(TITLES.get(position).split("&")[0]);
        detail.setText(DETAIL.get(position));
        detail.setTypeface(tf);
//        URL url = null;
//        try {
//            url = new URL(IMAGES.get(position));
//            imageView.setImageResource(R.drawable.ic_hospital_cross);
////            imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}