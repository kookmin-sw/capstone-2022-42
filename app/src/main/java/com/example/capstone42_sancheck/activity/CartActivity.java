package com.example.capstone42_sancheck.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.adapter.RecyclerViewAdapter;
import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.object.SearchListViewItem;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mRecyclerView = findViewById(R.id.rv_cart);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Mountain> mountainArrayList = new ArrayList<>();
        Mountain mountainInfo = new Mountain();
        mountainInfo.setDrawableId(R.drawable.home_mission_ex); // 사진
        mountainInfo.setM_Name("관악산"); // 산이름
        mountainInfo.setDiff("3"); // 난이도
        mountainArrayList.add(mountainInfo);

        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mountainArrayList);

        mRecyclerView.setAdapter(myAdapter);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
}