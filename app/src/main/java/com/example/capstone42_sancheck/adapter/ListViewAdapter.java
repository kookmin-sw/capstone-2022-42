package com.example.capstone42_sancheck.adapter;

import com.example.capstone42_sancheck.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Mountain> arrayList = new ArrayList<>();
    private TextView MNTN_NM;
    private TextView PMNTN_NM;
    private TextView PMNTN_LT;
    private TextView PMNTN_TIME;
    private TextView PMNTN_DFFL;
    private TextView PEOPLE;
    private ImageView heart;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public ListViewAdapter() {

    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.frag_search_listview_item, viewGroup, false);
        }

        MNTN_NM = (TextView) view.findViewById(R.id.M_Name);
        PMNTN_NM = (TextView) view.findViewById(R.id.PM_Name);
        PMNTN_LT = (TextView) view.findViewById(R.id.LT);
        PMNTN_TIME = (TextView) view.findViewById(R.id.TIME);
        PMNTN_DFFL = (TextView) view.findViewById(R.id.DFFL);
        PEOPLE = (TextView) view.findViewById(R.id.PEOPLE);
        heart = (ImageView) view.findViewById(R.id.mountain_heart);

        Mountain searchListViewItem = arrayList.get(i);

        MNTN_NM.setText(searchListViewItem.getMNTN_NM());
        PMNTN_NM.setText(searchListViewItem.getPMNTN_NM());
        PMNTN_LT.setText(searchListViewItem.getPMNTN_LT().toString() + "km");
        PMNTN_TIME.setText(searchListViewItem.getTime() + "분");
        PMNTN_DFFL.setText(searchListViewItem.getPMNTN_DFFL());
        PEOPLE.setText(searchListViewItem.getPEOPLE() + "명 등산중!");

        database = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Users");

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();
                databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(User.class) != null){
                            Toast.makeText(context, "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                            User user = snapshot.getValue(User.class);
                            user.setTrailPlanAdd(searchListViewItem.getIndex(), user.trailPlan);
                            databaseReference.child(uid).setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }

    public void addItem(int index, String MNTN_NM, String PMNTN_NM, Double PMNTN_LT, Double PMNTN_UPPL,
                        Double PMNTN_GODN, String PMNTN_DFFL, String START_PNT, String END_PNT) {

        Mountain searchListViewItem = new Mountain();
        searchListViewItem.setIndex(index);
        searchListViewItem.setMNTN_NM(MNTN_NM);
        searchListViewItem.setPMNTN_NM(PMNTN_NM);
        searchListViewItem.setPMNTN_LT(PMNTN_LT);
        searchListViewItem.setPMNTN_UPPL(PMNTN_UPPL);
        searchListViewItem.setPMNTN_GODN(PMNTN_GODN);
        searchListViewItem.setPMNTN_DFFL(PMNTN_DFFL);
        searchListViewItem.setSTART_PNT(START_PNT);
        searchListViewItem.setEND_PNT(END_PNT);

        arrayList.add(searchListViewItem);
    }

    public void clear() {
        arrayList.clear();
    }
}
