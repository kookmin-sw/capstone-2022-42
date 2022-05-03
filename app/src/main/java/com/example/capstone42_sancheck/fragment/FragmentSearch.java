package com.example.capstone42_sancheck.fragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.SearchActivity;
import com.example.capstone42_sancheck.adapter.ListViewAdapter;
import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.object.SearchListViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends Fragment {
    private View view;
    private ListView lv1;
    private ImageView btn1;
    private EditText et1;
    private ListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_search, container, false);

        lv1 = (ListView) view.findViewById(R.id.lv1);
        btn1 = (ImageView) view.findViewById(R.id.btnJson);
        et1 = (EditText) view.findViewById(R.id.et1);
        adapter = new ListViewAdapter();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                AssetManager assetManager = getResources().getAssets();
                String keyword = et1.getText().toString().replace(" ", "");

                try {
                    InputStream is = assetManager.open("jsons/seoul_mountain.json");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }
                    String jsondata = buffer.toString();

                    // json 데이터가 []로 시작하는 배열일 때
                    JSONArray jsonArray = new JSONArray(jsondata);
                    List mountainList = new ArrayList();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Mountain mountain = new Mountain();
                        JSONObject jo = jsonArray.getJSONObject(i);

                        mountain.setIndex(jo.optInt("INDEX", 0));
                        mountain.setM_Name(jo.optString("MNTN_NM", "NO_VALUE"));
                        mountain.setPM_Name(jo.optString("PMNTN_NM", "NO_VALUE"));
                        mountain.setLT(jo.optDouble("PMNTN_LT", 0));
                        mountain.setUppl(jo.optDouble("PMNTN_UPPL", 0));
                        mountain.setGodn(jo.optDouble("PMNTN_GODN", 0));
                        mountain.setDiff(jo.optString("PMNTN_DFFL", "NO_VALUE"));

                        if (jo.optString("MNTN_NM", "NOT VALUE").contains(keyword)) {
                            mountainList.add(mountain);
                            adapter.addItem(jo.optInt("INDEX", -1), jo.optString("MNTN_NM", "NOT VALUE"), jo.optString("PMNTN_NM", "NOT VALUE"), jo.optDouble("PMNTN_LT", -1),
                                    jo.optDouble("PMNTN_UPPL", -1), jo.optDouble("PMNTN_GODN", -1), jo.optString("PMNTN_DFFL", "NO_VALUE"),
                                    jo.optString("START_PNT", "NOT VALUE"), jo.optString("END_PNT", "NOT VALUE"));
                        }
                        lv1.setAdapter(adapter);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchListViewItem item = (SearchListViewItem) adapter.getItem(i);
                Intent intent = new Intent(getContext(), SearchActivity.class);
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
