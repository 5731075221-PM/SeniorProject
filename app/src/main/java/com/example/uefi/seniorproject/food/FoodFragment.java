package com.example.uefi.seniorproject.food;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {
    public AppBarLayout appBarLayout;
    public TextView textTool,energy,protein,fat,carbohydrate,fibre,vitE,thiamine,vitC,can;
    SearchView searchView;
    DBHelperDAO dbHelperDAO;
    ArrayList<FoodItem> foodItems;
    ArrayList<String> listOfFoods;
    RelativeLayout relaFood;
    ImageView foodPic;

    public FoodFragment() {
        // Required empty public constructor
    }

    public static FoodFragment newInstance() {
        FoodFragment fragment = new FoodFragment();
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        appBarLayout.setExpanded(true, true);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        energy = (TextView) view.findViewById(R.id.energy);
        protein = (TextView) view.findViewById(R.id.protein);
        fat = (TextView) view.findViewById(R.id.fat);
        carbohydrate = (TextView) view.findViewById(R.id.carbohydrate);
        fibre = (TextView) view.findViewById(R.id.fibre);
        vitE = (TextView) view.findViewById(R.id.vitE);
        thiamine = (TextView) view.findViewById(R.id.thiamine);
        vitC = (TextView) view.findViewById(R.id.vitC);
        can = (TextView) view.findViewById(R.id.can);
        foodPic = (ImageView) view.findViewById(R.id.foodPic);
        relaFood = (RelativeLayout) view.findViewById(R.id.relafood);
        setVisibleRela(false);

        searchView = (SearchView) view.findViewById(R.id.searchFood);
        searchView.setBackgroundResource(R.drawable.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหารายชื่ออาหาร");
//        searchView.setOnQueryTextListener(FoodFragment.this);

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, listOfFoods);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                hideSoftKeyboard(FoodFragment.this);
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                searchAutoComplete.setSelection(searchAutoComplete.getText().length());
                int index = listOfFoods.indexOf(queryString);
                setRela(index);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int index = listOfFoods.indexOf(query);
                setRela(index);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listOfFoods.indexOf(newText)==-1){
                    setVisibleRela(false);
                }
                return false;
            }
        });

        searchAutoComplete.setThreshold(0);

        setupUI(view.findViewById(R.id.linearFood));

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        foodItems = dbHelperDAO.getFood();
        listOfFoods = new ArrayList<>();
        for(int i = 0;i<foodItems.size();i++){
            listOfFoods.add(foodItems.get(i).getThai());
        }

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

    }

    public void setRela(int index){
        if(index>-1) {
            setVisibleRela(true);
            if(foodItems.get(index).getLabel()==0){
                can.setText("สามารถรับประทานได้");
                can.setBackgroundResource(R.color.colorFoodGreen);
            }else if(foodItems.get(index).getLabel()==2){
                can.setText("ไม่ควรรับประทานในปริมาณมาก");
                can.setBackgroundResource(R.color.colorFoodYellow);
            }else if(foodItems.get(index).getLabel()==1){
                can.setText("ไม่ควรรับประทาน");
                can.setBackgroundResource(R.color.colorFoodRed);
            }
            energy.setText(foodItems.get(index).getEnergy());
            protein.setText(foodItems.get(index).getProtein());
            fat.setText(foodItems.get(index).getFat());
            carbohydrate.setText(foodItems.get(index).getCarbohydrate());
            fibre.setText(foodItems.get(index).getFibre());
            vitE.setText(foodItems.get(index).getVitE());
            thiamine.setText(foodItems.get(index).getThiamine());
            vitC.setText(foodItems.get(index).getVitC());
        }
    }

    public void setVisibleRela(boolean isVisible){
        relaFood.setVisibility(isVisible ? View.VISIBLE:View.GONE);
        foodPic.setVisibility(isVisible ? View.GONE:View.VISIBLE);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof SearchView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(FoodFragment.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void hideSoftKeyboard(Fragment activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onResume() {
        super.onResume();
        textTool.setText("เมนูของฉัน");
    }

    public void onDestroy() {
        super.onDestroy();

    }

    public void onPause() {
        super.onPause();
        hideSoftKeyboard(FoodFragment.this);
    }

}
