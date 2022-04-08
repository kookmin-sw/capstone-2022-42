package com.example.capstone42_sancheck.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.adapter.rankAdapter;
import com.example.capstone42_sancheck.object.Rank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentRank extends Fragment {
    private ArrayList<Rank> arrayList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;




    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_rank, container, false);

        database = FirebaseDatabase.getInstance();//파이어베이스연동
        databaseReference = database.getReference("User");
        Context context =view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        rankAdapter adapter = new rankAdapter(arrayList,context);
        databaseReference.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                arrayList.clear(); //기존배열초기화
                for (DataSnapshot snap : snapshot.getChildren()) {
                    snap.child("rank").getRef().setValue(snapshot.getChildrenCount()-i);
                    Rank user = snap.getValue(Rank.class);
                    arrayList.add(user);
                    i++;
                }

                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();//리스트 저장 및 새로고침

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("tag", "error");
            }
        });

        recyclerView.setAdapter(adapter);

         //   recyclerView.setHasFixedSize(true);//리사이클러뷰 성능강화

        return view;


    }

}
