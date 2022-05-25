package com.example.capstone42_sancheck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.adapter.CartListViewAdapter;
import com.example.capstone42_sancheck.object.CartMountain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    
    private ListView mListView;
    private ImageView iv_back;
    private FirebaseDatabase database, mountainDB;
    private DatabaseReference databaseReference, mountaindatabaseReference;
    private FirebaseAuth auth;
    private CartListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mListView = (ListView) findViewById(R.id.lv_cart);
        adapter = new CartListViewAdapter();

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Users");
        mountainDB = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com/");

        databaseReference.child(uid).child("trailPlan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartMountain> mountainArrayList = new ArrayList<>();
                mountainArrayList.clear();
                adapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mountaindatabaseReference = mountainDB.getReference(String.valueOf(Integer.parseInt(dataSnapshot.getValue(Long.class).toString()) - 1));
                    mountaindatabaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("CartActivity", "Error getting data", task.getException());
                            }
                            else {
                                CartMountain mountain = new CartMountain();
                                mountain.setMNTN_NM(task.getResult().child("MNTN_NM").getValue(String.class));
                                mountain.setPMNTN_NM(task.getResult().child("PMNTN_NM").getValue(String.class));
                                mountain.setPMNTN_DFFL(task.getResult().child("PMNTN_DFFL").getValue(String.class));
                                mountain.setDrawableId(R.drawable.home_mission_ex);
                                mountain.setIndex(Integer.parseInt(dataSnapshot.getValue(Long.class).toString()));
                                mountain.setPMNTN_LT(task.getResult().child("PMNTN_LT").getValue(Double.class));
                                mountain.setSTART_PNT(task.getResult().child("START_PNT").getValue(String.class));
                                mountain.setEND_PNT(task.getResult().child("END_PNT").getValue(String.class));
                                mountainArrayList.add(mountain);
                                adapter.addItem(mountain.getMNTN_NM(), mountain.getPMNTN_NM(), mountain.getPMNTN_DFFL(), mountain.getDrawableId(), mountain.getIndex()
                                , mountain.getPMNTN_LT(), mountain.getSTART_PNT(), mountain.getEND_PNT());
                                mListView.setAdapter(adapter);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CartMountain mountain = (CartMountain) adapter.getItem(i);
                Intent intent = new Intent(view.getContext(), CartSearchActivity.class);
                intent.putExtra("index", mountain.getIndex());
                intent.putExtra("m_name", mountain.getMNTN_NM());
                intent.putExtra("pm_name", mountain.getPMNTN_NM());
                intent.putExtra("lt", mountain.getPMNTN_LT());
                intent.putExtra("pm_dffl", mountain.getPMNTN_DFFL());
                intent.putExtra("start", mountain.getSTART_PNT());
                intent.putExtra("end", mountain.getEND_PNT());
                startActivity(intent);
            }
        });
    }
}