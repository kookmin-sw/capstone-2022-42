package com.example.capstone42_sancheck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.adapter.RecyclerViewAdapter;
import com.example.capstone42_sancheck.object.Mountain;
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
    
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView iv_back;
    private FirebaseDatabase database, mountainDB;
    private DatabaseReference databaseReference, mountaindatabaseReference;
    private FirebaseAuth auth;
    RecyclerViewAdapter myAdapter;
    ArrayList<Mountain> mountainArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mRecyclerView = findViewById(R.id.rv_cart);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Users");
        mountainDB = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com/");
//        mountaindatabaseReference = mountainDB.getReference();

        databaseReference.child(uid).child("trailPlan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mountainArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Mountain mountainInfo = new Mountain();
                    mountainInfo.setDrawableId(R.drawable.home_mission_ex); // 사진
                    mountaindatabaseReference = mountainDB.getReference(String.valueOf(Integer.parseInt(dataSnapshot.getValue(Long.class).toString()) - 1));
                    mountaindatabaseReference.child("MNTN_NM").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("CartActivity", "Error getting data", task.getException());
                            }
                            else {
                                mountainInfo.setMNTN_NM(task.getResult().getValue(String.class));
                            }
                        }
                    });
                    mountaindatabaseReference.child("PMNTN_NM").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("CartActivity", "Error getting data", task.getException());
                            }
                            else {
                                mountainInfo.setPMNTN_NM(task.getResult().getValue(String.class));
                            }
                        }
                    });
                    mountaindatabaseReference.child("PMNTN_DFFL").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("CartActivity", "Error getting data", task.getException());
                            }
                            else {
                                mountainInfo.setPMNTN_DFFL(task.getResult().getValue(String.class));
                            }
                        }
                    });
                    mountainArrayList.add(mountainInfo);
                }
                myAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myAdapter = new RecyclerViewAdapter(mountainArrayList);
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