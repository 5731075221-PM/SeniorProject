package com.example.uefi.seniorproject.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
//import android.widget.SearchView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.breakiterator.LongLexTo;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by UEFI on 21/1/2561.
 */

public class SearchSymptomFragment extends Fragment implements SearchView.OnQueryTextListener {
    DBHelperDAO dbHelperDAO;
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();
    ConstraintLayout conslayout;
    RecyclerView recyclerView;
    SearchView searchView;
    SearchView.SearchAutoComplete mSearchAutoComplete;
    TextView empty;
    ArrayList<String> dictList, stopwordList, idfDoc, docLength, diseaseName, diseaseNameDefault, filteredValues, data;
    ArrayList<Double> keywordSymptom, queryDotDoc;
    ArrayList<Symptom> allSymptoms, mainSymptoms;
    ArrayList<ArrayList<String>> vectordata;
    ArrayList<Pair<Double, String>> simDoc;
    double queryLength, sum;
    String type;
    LongLexTo tokenizer;
    ProgressDialog progressBar;
    AppBarLayout appBarLayout;
    ArrayAdapter<String> adapterAuto;

    String[] gridViewString = {"ระบบกระดูกและข้อ", "ระบบทางเดินปัสสาวะ", "ระบบทางเดินอาหาร", "ระบบศีรษะและลำคอ", "ระบบทางเดินหายใจ",
            "ระบบหูคอจมูก", "ระบบตา", "ระบบหัวใจและหลอดเลือด", "ระบบโรคไต", "ระบบโรคผิวหนัง", "ระบบอวัยวะสืบพันธุ์", "ระบบต่อมไร้ท่อ",
            "ระบบประสาทวิทยา", "ระบบโรคติดเชื้อ", "ระบบมะเร็งวิทยา", "ระบบจิตเวช", "ระบบน้ำเหลือง", "ระบบกล้ามเนื้อและกระดูก", "ระบบภูมิคุ้มกัน", "อื่นๆ"
    };

    int[] gridViewImageId = {
            R.drawable.selector_grid1, R.drawable.selector_grid2, R.drawable.selector_grid3, R.drawable.selector_grid4, R.drawable.selector_grid5,
            R.drawable.selector_grid6, R.drawable.selector_grid7, R.drawable.selector_grid8, R.drawable.selector_grid9, R.drawable.selector_grid10,
            R.drawable.selector_grid11, R.drawable.selector_grid12, R.drawable.selector_grid13, R.drawable.selector_grid14, R.drawable.selector_grid15,
            R.drawable.selector_grid16, R.drawable.selector_grid17, R.drawable.selector_grid18, R.drawable.selector_grid19, R.drawable.selector_grid20
    };


    class createTokenizer extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage("กรุณารอสักครู่...");
            progressBar.setIndeterminate(false);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                diseaseNameDefault = dbHelperDAO.getDiseaseName();
                mainSymptoms = dbHelperDAO.getMainSymptoms();
                addSearchList();
                allSymptoms = dbHelperDAO.getAllSymptoms();
                vectordata = dbHelperDAO.getVectorData();
                docLength = dbHelperDAO.getDocLength();
                idfDoc = dbHelperDAO.getFreq();
                dictList = getArguments().getStringArrayList("dict");
                stopwordList = getArguments().getStringArrayList("stop");
                tokenizer = new LongLexTo(dictList, stopwordList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_symptom, container, false);

        if(appBarLayout != null) appBarLayout.setExpanded(true, true);

        empty = (TextView) view.findViewById(R.id.textEmpty);
        if (diseaseName.isEmpty()) empty.setVisibility(View.VISIBLE);
        else empty.setVisibility(View.INVISIBLE);

        searchView = (SearchView) view.findViewById(R.id.searchSymptom);
        searchView.setBackgroundResource(R.drawable.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหา");
        searchView.setOnQueryTextListener(this);

        String dataArray[] = new String[data.size()];
        data.toArray(dataArray);
        adapterAuto = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataArray);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        mSearchAutoComplete.setDropDownBackgroundResource(R.color.cardview_light_background);
        mSearchAutoComplete.setDropDownAnchor(R.id.searchSymptom);
        mSearchAutoComplete.setAdapter(adapterAuto);

        mSearchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                mSearchAutoComplete.setText("" + queryString);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.searchSymptomList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        conslayout = (ConstraintLayout) view.findViewById(R.id.searchSymptomLayout);
        empty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
//        diseaseNameDefault = dbHelperDAO.getDiseaseName();
        diseaseName = new ArrayList<>();
//        mainSymptoms = dbHelperDAO.getMainSymptoms();
//        addSearchList();
        new createTokenizer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void addSearchList() {
        data = new ArrayList<>();
        for (Symptom str : mainSymptoms) data.add(str.getWord());
    }

    public void initialQueryVector() {
        keywordSymptom = new ArrayList<>();
        for (int i = 0; i < mainSymptoms.size(); i++) {
            keywordSymptom.add(0.0);
        }
    }

    public void resetSearch() {
        diseaseName = new ArrayList<>();
        empty.setVisibility(View.VISIBLE);
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

        filteredValues = new ArrayList<>();
        resetSearch();//diseaseNameDefault;
        try {
            String output = tokenizer.genOutput(newText.trim(), tokenizer);
            if (!(output.equals(null) || output.equals(""))) {
                empty.setVisibility(View.INVISIBLE);
                System.out.println("output = " + output);
//                System.out.println("allsymptomsize = " + allSymptoms.size());
                String[] keyword = output.split("-");
                System.out.println("keywordlength = " + keyword.length);

                // initail query vector
                initialQueryVector();

                ArrayList<Integer> indexList = new ArrayList<>();
                queryLength = 0.0;
                double calvar = 0.0;
                // 1. check freq of query vector with mainsymptoms
//                for (int i = 0; i < keyword.length; i++) {
//                    System.out.println("keyword[i] = " + keyword[i]);
                indexList = dbHelperDAO.getIndexSymptom(dbHelperDAO.checkKeyword(keyword));
                System.out.println("Indexlist = " + indexList.toString());
                for (int i = 0; i < indexList.size(); i++)
                    keywordSymptom.set(indexList.get(i), keywordSymptom.get(indexList.get(i)) + 1);
                for (int j = 0; j < indexList.size(); j++) {
                    // 2. normalize query and calculate weight of terms
                    calvar = (Math.log10(keywordSymptom.get(indexList.get(j))) + 1) * Double.parseDouble(idfDoc.get(indexList.get(j)));
                    keywordSymptom.set(indexList.get(j), calvar);
                    queryLength += Math.pow(calvar, 2);
                }
//                }
                queryLength = Math.sqrt(queryLength);
//                System.out.println("keywordSymptom = " + keywordSymptom.toString());

//                for (int i = 0; i < keywordSymptom.size(); i++) {
//                    if (keywordSymptom.get(i) > 0.0) {
//                        System.out.println("keyword = " + keywordSymptom.get(i) + " ");
//                        System.out.println("keyword = " + Double.parseDouble(idfDoc.get(i)) + " ");
//                        keywordSymptom.set(i, (Math.log10(keywordSymptom.get(i)) + 1) * Double.parseDouble(idfDoc.get(i)));
//                        // calculate query vector length
//                        queryLength += Math.pow(keywordSymptom.get(i), 2);
//                    }
//                }
//for(int i = 0; i<keywordSymptom.size();i++){
//    System.out.println("keywordSymptom "+i+" "+keywordSymptom.get(i));
//}

                System.out.println("keywordSymptom = " + keywordSymptom.toString());

                // 3. dot product between query vector and documents vector
                queryDotDoc = new ArrayList<>();
                simDoc = new ArrayList<>();
                boolean isOK = true;
                for (int i = 0; i < vectordata.size(); i++) {
                    sum = 0.0;
                    for (int j = 0; j < indexList.size(); j++) {
//                        if(Double.parseDouble(vectordata.get(i).get(indexList.get(j))) <= 0.0) {
//                            isOK = false;
//                            break;
//                        }
//                        if ((keywordSymptom.get(indexList.get(j)) > 0.0) && (Double.parseDouble(vectordata.get(i).get(indexList.get(j))) > 0.0)) {
//                        System.out.println("i = "+i+" j = "+j+" "+" vectordata)="+vectordata.get(i).get(indexList.get(j))+" keysym"+keywordSymptom.get(indexList.get(j)));
                        sum += Double.parseDouble(vectordata.get(i).get(indexList.get(j))) * keywordSymptom.get(indexList.get(j));
//                        }
                    }
                    // 4. calculate similarity
//                    if(!isOK) isOK = true;
//                    else{
                    double var = sum / (queryLength * Double.parseDouble(docLength.get(i)));
                    System.out.println("var sum = " +i+" "+ var);
                    if (var > 0.0)
                        simDoc.add(new Pair<Double, String>(var, diseaseNameDefault.get(i)));
//                    }
//                    queryDotDoc.add(sum);
                }
//                System.out.println("queryDotDoc = " + queryDotDoc.toString());
//                System.out.println("simDoc1 = " + simDoc.toString());

//                simDoc = new ArrayList<>();
//                for (int i = 0; i < vectordata.size(); i++) {
//                    simDoc.add(new Pair<Double, String>(queryDotDoc.get(i) / (queryLength * Double.parseDouble(docLength.get(i))), diseaseName.get(i)));
//                }

                // 5. sort documents
                Collections.sort(simDoc, new Comparator<Pair<Double, String>>() {
                    @Override
                    public int compare(Pair<Double, String> t0, Pair<Double, String> t1) {
                        if (t0.first < t1.first) return 1;
                        else if (t0.first > t1.first) return -1;
                        else return 0;
                    }
                });
//                System.out.println("simDoc2 = " + simDoc.toString());

                for (int i = 0; i < simDoc.size(); i++) {
                    System.out.println("simDoc3 = " + simDoc.get(i).first + " " + simDoc.get(i).second);
                    filteredValues.add(simDoc.get(i).second);
                }
//                System.out.println(filteredValues.toString());
                diseaseName = filteredValues;
            } else resetSearch();

        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView name, type;
        ImageView img;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.diseasetextView1);
            type = (TextView) itemView.findViewById(R.id.diseasetextView2);
            img = (ImageView) itemView.findViewById(R.id.diseaseimageView1);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>/* implements SectionIndexer*/ {
        private final int VIEW_TYPE_HEADER = 0;
        private final int VIEW_TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if(viewType == VIEW_TYPE_HEADER){
//                View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_list, parent, false);
//                return new SearchSymptomFragment.HeaderViewHolder(view);
//            }else if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_disease_list, parent, false);
            return new SearchSymptomFragment.ViewHolder(view);
//            }
//            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
            type = dbHelperDAO.getDiseaseType(diseaseName.get(position));
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.name.setText(diseaseName.get(position));
            viewHolder.name.setTypeface(tf);
            viewHolder.type.setText(type);
            viewHolder.type.setTypeface(tf);
            viewHolder.img.setBackgroundResource(gridViewImageId[Arrays.asList(gridViewString).indexOf(type.split(",")[0])]);
            viewHolder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick, MotionEvent motionEvent) {
                    if (!isLongClick) {
                        SelectItemFragment fragment = new SelectItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", diseaseName.get(position));
                        bundle.putString("type", type);
                        fragment.setArguments(bundle);
//                        getFragmentManager().beginTransaction()
                        getParentFragment().getFragmentManager().beginTransaction()
                                .replace(R.id.container_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return diseaseName.size();
        }

//        @Override
//        public Object[] getSections() {
//            String letters = "[ก-ฮ]";
//            mDataArray = diseaseName;
//            List<String> sections = new ArrayList<>(44);
//            mSectionPositions = new ArrayList<>(44);
//            for (int i = 0, size = mDataArray.size(); i < size; i++) {
//                String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
//                if(!section.matches(letters)){
//                    section = String.valueOf(mDataArray.get(i).charAt(1)).toUpperCase();
//                }
//                if (!sections.contains(section)) {
//                    sections.add(section);
//                    mSectionPositions.add(i);
//                }
//            }
//            return sections.toArray(new String[0]);
//        }
//
//        @Override
//        public int getPositionForSection(int i) {
//            return mSectionPositions.get(i);
//        }
//
//        @Override
//        public int getSectionForPosition(int i) {
//            return 0;
//        }
    }
}
