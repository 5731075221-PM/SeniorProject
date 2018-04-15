package com.example.uefi.seniorproject;

//import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.fragment.DiseaseNavFragment;
import com.example.uefi.seniorproject.fragment.FavoriteItemFragment;
import com.example.uefi.seniorproject.fragment.HospitalNavFragment;
import com.example.uefi.seniorproject.fragment.HospitalNearbyFragment;
import com.example.uefi.seniorproject.fragment.MainFragment;
import com.example.uefi.seniorproject.firstaid.FirstaidFragment;
import com.example.uefi.seniorproject.reminder.NotesFragment;
import com.example.uefi.seniorproject.reminder.ReminderFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DBHelperDAO dbHelperDAO;
    ArrayList<String> dictList, stopwordList;
    public TextView textTool;
    ActionBarDrawerToggle toggle;
    FragmentManager fragmentManager;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    DrawerLayout drawer;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dbHelperDAO = DBHelperDAO.getInstance(this);
//        dbHelperDAO.open();
//        dictList = dbHelperDAO.getLexitron();
//        stopwordList = dbHelperDAO.getStopword();

        Singleton single = Singleton.getInstance();
        if(single.getDict().size() != 0){
            System.out.println("size = "+single.getDict().size());
            System.out.println("size = "+single.getStopword().size());
        }

        dictList = single.getDict();
        stopwordList = single.getStopword();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textTool = (TextView) findViewById(R.id.textTool);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Kanit-Regular.ttf");
        textTool.setTypeface(font);

//        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
//        params.setScrollFlags(0);
//        collapsingToolbarLayout.setLayoutParams(params);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(R.id.container_fragment, new MainFragment())
//                .commit();


        onNavigationItemSelected(navigationView.getMenu().getItem(0));
//        navigationView.getMenu().getItem(0).setChecked(true);
//        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed() {
//        Log.d("Number = ",getSupportFragmentManager().getBackStackEntryCount()+"");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            getSupportActionBar().setTitle(R.string.app_name);
//            textTool.setText(R.string.app_name);
//            Log.d("CASE1 = ",getSupportFragmentManager().getBackStackEntryCount()+"");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            drawer.addDrawerListener(toggle);
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
            toggle.syncState();
            getSupportFragmentManager().popBackStack();
            System.out.println("IN1");


        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            System.out.println("IN2");
//            Log.d("CASE2 = ",getSupportFragmentManager().getBackStackEntryCount()+"");
            getSupportFragmentManager().popBackStack();
            toggle.setDrawerIndicatorEnabled(true);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home) {
//            fragmentManager.beginTransaction()
//                    .add(R.id.container_fragment, new MainFragment())
//                    .commit();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, new MainFragment())
//                    .addToBackStack("")
                    .commit();
        }else if (id == R.id.nav_disease) {
//            toggle.setDrawerIndicatorEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            if(!mToolBarNavigationListenerIsRegistered) {
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Doesn't have to be onBackPressed
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//            }
            Bundle args = new Bundle();
            args.putStringArrayList("dict",dictList);
            args.putStringArrayList("stop",stopwordList);
            DiseaseNavFragment fragment = new DiseaseNavFragment();
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, fragment)
//                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_firstaid) {
//            toggle.setDrawerIndicatorEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            if(!mToolBarNavigationListenerIsRegistered) {
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Doesn't have to be onBackPressed
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, new FirstaidFragment())
//                    .addToBackStack("การปฐมพยาบาล")
                    .commit();
        } else if (id == R.id.nav_hospital) {
//            toggle.setDrawerIndicatorEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            if(!mToolBarNavigationListenerIsRegistered) {
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Doesn't have to be onBackPressed
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, new HospitalNavFragment())
//                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_reminder) {
//            toggle.setDrawerIndicatorEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            if(!mToolBarNavigationListenerIsRegistered) {
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Doesn't have to be onBackPressed
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, new ReminderFragment())
//                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_food) {

        }else if(id == R.id.nav_fav){
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, new FavoriteItemFragment())
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        toggle.onConfigurationChanged(newConfig);
    }


}