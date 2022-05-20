package com.example.capstone42_sancheck.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.SearchActivity;
import com.example.capstone42_sancheck.adapter.ListViewAdapter;
import com.example.capstone42_sancheck.object.Mountain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentSearch extends Fragment {
    private View view;
    private ListView lv1;
    private ImageView btn1;
    private EditText et1;
    private ListViewAdapter adapter;
    private FirebaseDatabase mountainDB;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private Spinner spinner;

    private final static Comparator<Mountain> timeComparator = new Comparator<Mountain>() {
        public int compare(Mountain o, Mountain t1) {
            return Integer.compare(o.getTime(), t1.getTime());
        }
    };

    private final static Comparator<Mountain> distanceComparator = new Comparator<Mountain>() {
        public int compare(Mountain o, Mountain t1) {
            return Double.compare(o.getPMNTN_LT(), t1.getPMNTN_LT());
        }
    };

    private final static Comparator<Mountain> diffComparator = new Comparator<Mountain>() {
        public int compare(Mountain o, Mountain t1) {
            int oDiff = 0, t1Diff = 0;
            if (o.getPMNTN_DFFL().equals("쉬움")) oDiff = 1;
            else if (o.getPMNTN_DFFL().equals("중간")) oDiff = 2;
            else if (o.getPMNTN_DFFL().equals("어려움")) oDiff = 3;

            if (t1.getPMNTN_DFFL().equals("쉬움")) t1Diff = 1;
            else if (t1.getPMNTN_DFFL().equals("중간")) t1Diff = 2;
            else if (t1.getPMNTN_DFFL().equals("어려움")) t1Diff = 3;
            
            return Integer.compare(oDiff, t1Diff);
        }
    };

    private final static Comparator<Mountain> pnameComparator = new Comparator<Mountain>() {
        public int compare(Mountain o, Mountain t1) {
            return Collator.getInstance().compare(o.getPMNTN_NM(), t1.getPMNTN_NM());
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_search, container, false);
        lv1 = (ListView) view.findViewById(R.id.lv1);
        btn1 = (ImageView) view.findViewById(R.id.btnJson);
        et1 = (EditText) view.findViewById(R.id.et1);
        adapter = new ListViewAdapter();
        mountainDB = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com");
        databaseReference = mountainDB.getReference();

        spinner = view.findViewById(R.id.spinner_list);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(container.getContext(), R.array.filter, android.R.layout.simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "검색중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();

                String keyword = et1.getText().toString().replace(" ", "");
                String filter = spinner.getSelectedItem().toString();
                List<Mountain> mountainList = new ArrayList();
                mountainList.clear();
                adapter.clear();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Mountain mountain = new Mountain();
                            mountain.setIndex(dataSnapshot.child("INDEX").getValue(int.class));
                            mountain.setMNTN_NM(dataSnapshot.child("MNTN_NM").getValue(String.class));
                            mountain.setPMNTN_NM(dataSnapshot.child("PMNTN_NM").getValue(String.class));
                            mountain.setPMNTN_LT(dataSnapshot.child("PMNTN_LT").getValue(Double.class));
                            mountain.setPMNTN_UPPL(dataSnapshot.child("PMNTN_UPPL").getValue(Double.class));
                            mountain.setPMNTN_GODN(dataSnapshot.child("PMNTN_GODN").getValue(Double.class));
                            mountain.setPMNTN_DFFL(dataSnapshot.child("PMNTN_DFFL").getValue(String.class));
                            mountain.setSTART_PNT(dataSnapshot.child("START_PNT").getValue(String.class));
                            mountain.setEND_PNT(dataSnapshot.child("END_PNT").getValue(String.class));

                            if (dataSnapshot.child("MNTN_NM").getValue(String.class).contains(keyword)) {
                                mountainList.add(mountain);
                                adapter.addItem(dataSnapshot.child("INDEX").getValue(Integer.class), dataSnapshot.child("MNTN_NM").getValue(String.class),
                                        dataSnapshot.child("PMNTN_NM").getValue(String.class), dataSnapshot.child("PMNTN_LT").getValue(Double.class),
                                        dataSnapshot.child("PMNTN_UPPL").getValue(Double.class), dataSnapshot.child("PMNTN_GODN").getValue(Double.class),
                                        dataSnapshot.child("PMNTN_DFFL").getValue(String.class), dataSnapshot.child("START_PNT").getValue(String.class),
                                        dataSnapshot.child("END_PNT").getValue(String.class));
//                                lv1.setAdapter(adapter);
                            }
                        }
                        adapter.clear();
                        if (filter.equals("소요시간순"))
                            Collections.sort(mountainList, timeComparator);

                        else if (filter.equals("길이순"))
                            Collections.sort(mountainList, distanceComparator);

                        else if (filter.equals("난이도순"))
                            Collections.sort(mountainList, diffComparator);

                        else if (filter.equals("구간이름순"))
                            Collections.sort(mountainList, pnameComparator);

                        for (Mountain m : mountainList) {
                            adapter.addItem(m.getIndex(), m.getMNTN_NM(), m.getPMNTN_NM(),
                                    m.getPMNTN_LT(), m.getPMNTN_UPPL(), m.getPMNTN_GODN(), m.getPMNTN_DFFL(),
                                    m.getSTART_PNT(), m.getEND_PNT());
                            lv1.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                아래는 기존 json 파싱하는 코드 ( 현재는 파이어베이스에서 파싱 )
//                AssetManager assetManager = getResources().getAssets();
//                String keyword = et1.getText().toString().replace(" ", "");
//
//                try {
//                    InputStream is = assetManager.open("jsons/mountain.json");
//                    InputStreamReader isr = new InputStreamReader(is);
//                    BufferedReader reader = new BufferedReader(isr);
//
//                    StringBuffer buffer = new StringBuffer();
//                    String line = reader.readLine();
//                    while (line != null) {
//                        buffer.append(line + "\n");
//                        line = reader.readLine();
//                    }
//                    String jsondata = buffer.toString();
//
//                    // json 데이터가 []로 시작하는 배열일 때
//                    JSONArray jsonArray = new JSONArray(jsondata);
//                    List mountainList = new ArrayList();
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        Mountain mountain = new Mountain();
//                        JSONObject jo = jsonArray.getJSONObject(i);
//
//                        mountain.setIndex(jo.optInt("INDEX", 0));
//                        mountain.setM_Name(jo.optString("MNTN_NM", "NO_VALUE"));
//                        mountain.setPM_Name(jo.optString("PMNTN_NM", "NO_VALUE"));
//                        mountain.setLT(jo.optDouble("PMNTN_LT", 0));
//                        mountain.setUppl(jo.optDouble("PMNTN_UPPL", 0));
//                        mountain.setGodn(jo.optDouble("PMNTN_GODN", 0));
//                        mountain.setDiff(jo.optString("PMNTN_DFFL", "NO_VALUE"));
//
//                        if (jo.optString("MNTN_NM", "NOT VALUE").contains(keyword)) {
//                            mountainList.add(mountain);
//                            adapter.addItem(jo.optInt("INDEX", -1), jo.optString("MNTN_NM", "NOT VALUE"), jo.optString("PMNTN_NM", "NOT VALUE"), jo.optDouble("PMNTN_LT", -1),
//                                    jo.optDouble("PMNTN_UPPL", -1), jo.optDouble("PMNTN_GODN", -1), jo.optString("PMNTN_DFFL", "NO_VALUE"),
//                                    jo.optString("START_PNT", "NOT VALUE"), jo.optString("END_PNT", "NOT VALUE"));
//                        }
//                        lv1.setAdapter(adapter);
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mountain item = (Mountain) adapter.getItem(i);
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("index", item.getIndex());
                intent.putExtra("m_name", item.getMNTN_NM());
                intent.putExtra("pm_name", item.getPMNTN_NM());
                intent.putExtra("lt", item.getPMNTN_LT());
                intent.putExtra("pm_dffl", item.getPMNTN_DFFL());
                intent.putExtra("pm_time", item.getTime());
                intent.putExtra("start", item.getSTART_PNT());
                intent.putExtra("end", item.getEND_PNT());
                startActivity(intent);
            }
        });
        return view;
    }
}
