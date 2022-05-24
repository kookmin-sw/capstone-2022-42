package com.example.capstone42_sancheck.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.adapter.trailCompleteListviewAdapter;
import com.example.capstone42_sancheck.object.CompleteMountain;
import com.example.capstone42_sancheck.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private View view;

    private FirebaseAuth auth;
    private DatabaseReference database, databaseReference;

    private TextView tv_name;
    private ImageView iv_profile;
    private ImageView iv_edit;

    private TextView tv_totalSteps;
    private TextView tv_score;
    private TextView tv_rank;

    private TextView tv_walkSun;
    private TextView tv_walkMon;
    private TextView tv_walkTue;
    private TextView tv_walkWen;
    private TextView tv_walkThu;
    private TextView tv_walkFri;
    private TextView tv_walkSat;

    private ProgressBar mokpyoprogressBar;

    private ListView lv_trailMemo;
    private trailCompleteListviewAdapter adapter;
    private FirebaseDatabase mountainDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);

        tv_name = view.findViewById(R.id.tv_name);
        iv_profile = view.findViewById(R.id.iv_profile);
        iv_edit = (ImageView) view.findViewById(R.id.iv_edit);

        tv_totalSteps = (TextView) view.findViewById(R.id.tv_totalSteps);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        tv_rank = (TextView) view.findViewById(R.id.tv_rank);

        tv_walkSun = (TextView) view.findViewById(R.id.tv_walkSun);
        tv_walkMon = (TextView) view.findViewById(R.id.tv_walkMon);
        tv_walkTue = (TextView) view.findViewById(R.id.tv_walkTue);
        tv_walkWen = (TextView) view.findViewById(R.id.tv_walkWen);
        tv_walkThu = (TextView) view.findViewById(R.id.tv_walkThu);
        tv_walkFri = (TextView) view.findViewById(R.id.tv_walkFri);
        tv_walkSat = (TextView) view.findViewById(R.id.tv_walkSat);

        mokpyoprogressBar = (ProgressBar) view.findViewById(R.id.mokpyoprogressBar);

        lv_trailMemo = (ListView) view.findViewById(R.id.lv_trailMemo);
        adapter = new trailCompleteListviewAdapter();
        mountainDB = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com");
        databaseReference = mountainDB.getReference();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        final String uid = user.getUid();

        database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class) != null){
                    User post = snapshot.getValue(User.class);
                    tv_name.setText(post.getName());
                    Log.d("FragmentHome", "유저 정보 불러오기 성공!");

                    iv_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                            ad.setIcon(R.mipmap.ic_launcher);
                            ad.setTitle("닉네임 변경");
                            ad.setMessage("변경할 닉네임을 입력하세요.(1~10자)");

                            final EditText et = new EditText(view.getContext());
                            ad.setView(et);

                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String result = et.getText().toString();
                                    tv_name.setText(result);
                                    post.setName(result);
                                    database.child("Users").child(uid).setValue(post)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("iv_edit", "유저 정보 수정 확인!");
                                                }
                                            });
                                    dialogInterface.dismiss();
                                }
                            });

                            ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            ad.show();

                        }
                    });

                    Glide.with(view)
                            .load(user.getPhotoUrl())
                            .error(R.drawable.profile)
                            .into(iv_profile);

                    tv_totalSteps.setText(String.valueOf(post.getWalkTotal()));
                    tv_score.setText(String.valueOf(post.getScore()));
                    tv_rank.setText(String.valueOf(post.getRank()));

                    tv_walkSun.setText(String.valueOf(post.getWalkSun()));
                    tv_walkMon.setText(String.valueOf(post.getWalkMon()));
                    tv_walkTue.setText(String.valueOf(post.getWalkTue()));
                    tv_walkWen.setText(String.valueOf(post.getWalkWen()));
                    tv_walkThu.setText(String.valueOf(post.getWalkThu()));
                    tv_walkFri.setText(String.valueOf(post.getWalkFri()));
                    tv_walkSat.setText(String.valueOf(post.getWalkSat()));

                    mokpyoprogressBar.setProgress(post.getWalkDaily());

                    List<CompleteMountain> mountainList = new ArrayList<>();
                    mountainList.clear();
                    adapter.clear();

                    if (post.trailComplited != null){
                        for (int i = (post.trailComplited).size()-1; i > (post.trailComplited).size()-11; i--){
                            if (i < 0){
                                break;
                            }
                            else{
                                String monthDay = post.trailComplitedDate.get(i).substring(5);
                                String year = post.trailComplitedDate.get(i).substring(0, 4);

                                databaseReference.child(String.valueOf(post.trailComplited.get(i))).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(!task.isSuccessful()){
                                            Log.d("trailComplitedListView", "Error");
                                        }
                                        else{
                                            CompleteMountain completeMountain = new CompleteMountain(monthDay, year, task.getResult().child("MNTN_NM").getValue(String.class), task.getResult().child("PMNTN_NM").getValue(String.class));
                                            mountainList.add(completeMountain);
                                            adapter.addItem(monthDay, year, task.getResult().child("MNTN_NM").getValue(String.class), task.getResult().child("PMNTN_NM").getValue(String.class));
                                            Log.d("adapter", "add Item");
                                            lv_trailMemo.setAdapter(adapter);
                                        }
                                    }
                                });
                            }
                        }
                        //lv_trailMemo.setAdapter(adapter);
                    }
                } else{
                    Log.d("FragmentHome", "유저 정보 없음...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FragmentHome", "유저 정보 불러오기 실패ㅠ");
            }
        });

        return view;

    }
}
