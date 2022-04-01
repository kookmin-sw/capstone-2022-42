package com.example.capstone42_sancheck.activity;

import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.adapter.ListViewAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView lv1;
    Button btn1;
    EditText et1;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lv1 = (ListView) findViewById(R.id.lv1);
        btn1 = (Button) findViewById(R.id.btnJson);
        et1 = (EditText) findViewById(R.id.et1);
        adapter = new ListViewAdapter();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                AssetManager assetManager = getAssets();
                String keyword = et1.getText().toString();

                try {
                    InputStream is = assetManager.open("jsons/seoul_mountain.json");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null){
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }
                    String jsondata = buffer.toString();

                    // json 데이터가 []로 시작하는 배열일 때
                    JSONArray jsonArray = new JSONArray(jsondata);
                    List mountainList = new ArrayList();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        Mountain mountain  = new Mountain();
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
                            adapter.addItem(jo.optString("MNTN_NM", "NOT VALUE"), jo.optString("PMNTN_NM", "NOT VALUE"));
                        }
                        lv1.setAdapter(adapter);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}