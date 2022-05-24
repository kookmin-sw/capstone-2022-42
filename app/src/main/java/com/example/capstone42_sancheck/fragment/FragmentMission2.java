package com.example.capstone42_sancheck.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.capstone42_sancheck.R;

import com.example.capstone42_sancheck.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FragmentMission2 extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database2;
    private DatabaseReference database;
    private DatabaseReference databaseReference;

    View view;
    int score;
    int Midx;
    int weeklyCount;
    int dailyCount;
    int walkWeekly;
    String dow;
    List<Integer> mission;
    int cnt;
    TextView text5;

   public int weeklyM(List<Integer> ComplitedM) {
       int weeklyMidx;
       while(true){
           weeklyMidx = (int) (Math.random() * 1000);
           if(!(ComplitedM.contains(weeklyMidx))) break;
       }


        return weeklyMidx;
    }

    public void changeText(int idx){
        database2 = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com");
        databaseReference = database2.getReference(Integer.toString(idx));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String r = snapshot.child("MNTN_NM").getValue(String.class);
                text5.setText("[주간 랜덤 등산로] " + r + " 등산하기");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //프
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_mission2, container, false);
       Button btn1 = (Button) view.findViewById(R.id.button1) ;
        Button btn2 = (Button) view.findViewById(R.id.button2) ;
        Button btn3 = (Button) view.findViewById(R.id.button3) ;
        Button btn4 = (Button) view.findViewById(R.id.button4) ;
        Button btn5 = (Button) view.findViewById(R.id.button5) ;

        ProgressBar progress1 = (ProgressBar) view.findViewById(R.id.progress1);
        ProgressBar progress2 = (ProgressBar) view.findViewById(R.id.progress2);
        ProgressBar progress3 = (ProgressBar) view.findViewById(R.id.progress3);
        ProgressBar progress4 = (ProgressBar) view.findViewById(R.id.progress4);
        ProgressBar progress5 = (ProgressBar) view.findViewById(R.id.progress5);



        text5 = (TextView)  view.findViewById(R.id.text5);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();
        database = FirebaseDatabase.getInstance().getReference();


        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User me = snapshot.getValue(User.class);
                walkWeekly  =(Long.valueOf(me.getWalkTotal()).intValue());


                dow =me.getWeeklyCheck();
                dailyCount = me.getMissionDailyCount();
                weeklyCount =me.getMissionWeeklyCount();
                mission = me.getMissionWeekly();
                cnt = mission.get(0);
                Midx = mission.get(7);
                if(dow.equals("월")||dow.equals("M")){
                    mission= new ArrayList(Collections.nCopies(8, 0));
                    me.setMissionDailyCount(0);
                    me.setWeeklyCheck("초기화");
                    mission.set(0,0);
                    me.setMissionWeekly(mission);
                    database.child("Users").child(uid).setValue(me);
                }
              if(mission.get(7)==0) {
                 if(me.getTrailComplited()==null) Midx = (int) (Math.random() * 1000);
                 else Midx = weeklyM(me.getTrailComplited());
                 mission.set(7,Midx);
                 database.child("Users").child(uid).setValue(me);
              }

               changeText(Midx);
                progress1.setProgress(dailyCount);
                progress2.setProgress(dailyCount);
                progress3.setProgress(walkWeekly);
                progress4.setProgress(walkWeekly);
                progress5.setProgress(mission.get(5));

                btn1.setEnabled(dailyCount<=10);
                btn2.setEnabled(dailyCount<=20);
                btn3.setEnabled(mission.get(3)!=1);
                btn4.setEnabled(mission.get(4)!=1);
                btn5.setEnabled(mission.get(5)!=1);




                //주간미션 1. 일일 미션 10개 달성
                if(mission.get(1)==0 &&dailyCount>=10){
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionWeekly").child("1").setValue(1);
                            btn1.setEnabled(false);
                            cnt++;
                            weeklyCount++;
                            database.child("Users").child(uid).child("missionWeekly").child("0").setValue(cnt);
                            database.child("Users").child(uid).child("missionWeeklyCount").setValue(weeklyCount);

                        }
                    });
                }
                //주간미션 2. 일일 미션 20개 달성
                if(mission.get(2)==0&& dailyCount>=20){
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionWeekly").child("2").setValue(1);
                            btn2.setEnabled(false);
                            cnt++;
                            weeklyCount++;
                            database.child("Users").child(uid).child("missionWeekly").child("0").setValue(cnt);
                            database.child("Users").child(uid).child("missionWeeklyCount").setValue(weeklyCount);

                        }
                    });
                }

                //주간미션 3. 주간 50000 걷기
                if(mission.get(3)==0 && walkWeekly>=50000){
                    btn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionWeekly").child("3").setValue(1);
                            weeklyCount++;
                            cnt++;
                            database.child("Users").child(uid).child("missionWeekly").child("0").setValue(cnt);
                            database.child("Users").child(uid).child("missionWeeklyCount").setValue(weeklyCount);
                            btn3.setEnabled(false);

                        }
                    });
                }

                //주간미션 4. 주간 100000 걷기
                if(mission.get(4)==0 && walkWeekly>=50000){
                    btn4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+70;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionWeekly").child("4").setValue(1);
                            btn4.setEnabled(false);
                            weeklyCount++;
                            cnt++;
                            database.child("Users").child(uid).child("missionWeekly").child("0").setValue(cnt);
                            database.child("Users").child(uid).child("missionWeeklyCount").setValue(weeklyCount);
                        }
                    });
                }

                //주간미션 5. 등산하기
                if(me.getTrailComplited()!=null){
                if(mission.get(5)==0 && (me.getTrailComplited()).contains(Midx)) {

                    btn5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score + 100;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionWeekly").child("5").setValue(1);
                            btn5.setEnabled(false);
                            weeklyCount++;
                            cnt++;
                            database.child("Users").child(uid).child("missionWeekly").child("0").setValue(cnt);
                            database.child("Users").child(uid).child("missionWeeklyCount").setValue(weeklyCount);
                        }
                    });
                }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Inflate the layout for this fragment
        return view;
    }
}