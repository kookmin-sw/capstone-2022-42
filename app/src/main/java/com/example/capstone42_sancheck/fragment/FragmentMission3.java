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
import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentMission3 extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database2;
    private DatabaseReference databaseReference;
    private DatabaseReference database;
    TextView text8;
    View view;
    int score;
    int flag;
    List<Integer> mission;
    int Midx;
    public int monthlyM(List<Integer> ComplitedM) {
        int montlyMidx;
        while(true){
            montlyMidx = (int) (Math.random() * 1000);
            if(!(ComplitedM.contains(montlyMidx))) break;
        }
        return montlyMidx;
    }
    public void changeText(int idx){
        database2 = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com");
        databaseReference = database2.getReference(Integer.toString(idx));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String r = snapshot.child("MNTN_NM").getValue(String.class);
                text8.setText("[월간 랜덤 등산로] " + r + " 등산하기");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_mission3, container, false);

        Button btn1 = (Button) view.findViewById(R.id.button1) ;
        Button btn2 = (Button) view.findViewById(R.id.button2) ;
        Button btn3 = (Button) view.findViewById(R.id.button3) ;
        Button btn4 = (Button) view.findViewById(R.id.button4) ;
        Button btn5 = (Button) view.findViewById(R.id.button5) ;
        Button btn6 = (Button) view.findViewById(R.id.button6) ;
        Button btn7 = (Button) view.findViewById(R.id.button7) ;
        Button btn8 = (Button) view.findViewById(R.id.button8) ;


        ProgressBar progress1 = (ProgressBar) view.findViewById(R.id.progress1);
        ProgressBar progress2 = (ProgressBar) view.findViewById(R.id.progress2);
        ProgressBar progress3 = (ProgressBar) view.findViewById(R.id.progress3);
        ProgressBar progress4 = (ProgressBar) view.findViewById(R.id.progress4);
        ProgressBar progress5 = (ProgressBar) view.findViewById(R.id.progress5);
        ProgressBar progress6 = (ProgressBar) view.findViewById(R.id.progress6);
        ProgressBar progress7 = (ProgressBar) view.findViewById(R.id.progress7);
        ProgressBar progress8 = (ProgressBar) view.findViewById(R.id.progress8);
        text8 = (TextView)  view.findViewById(R.id.text8);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();
        database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User me =  snapshot.getValue(User.class);
                mission = me.getMissionMonthly();
                Midx =mission.get(11);
                flag = mission.get(13);//월간 미션 달성갯수
                if(me.getMissionWeekly().contains(-1)) {
                    mission= new ArrayList(Collections.nCopies(14, 0));
                    me.setMissionWeeklyCount(0);
                    me.setMissionMonthly(mission);
                    database.child("Users").child(uid).setValue(me);
                }
                if(mission.get(9)==0){
                    mission.set(9,4853);//북한산_백운대
                    mission.set(10,27101);// 응봉산
                    Midx =monthlyM(me.getTrailComplited());
                    mission.set(11,Midx);//랜덤
                    me.setMissionMonthly(mission);
                    database.child("Users").child(uid).setValue(me);
                }

                changeText(Midx);

                int weeklyCount = me.getMissionWeeklyCount();
                int dailyCount = mission.get(12);

                progress1.setProgress(weeklyCount);
                progress2.setProgress(weeklyCount);
                progress3.setProgress(weeklyCount);
                progress4.setProgress(dailyCount);
                progress5.setProgress(dailyCount);

                progress6.setProgress(mission.get(6));
                progress7.setProgress(mission.get(7));
                progress8.setProgress(mission.get(8));

                btn1.setEnabled(weeklyCount<=5);
                btn2.setEnabled(weeklyCount<=10);
                btn3.setEnabled(weeklyCount<=15);
                btn4.setEnabled(dailyCount<=10);
                btn5.setEnabled(dailyCount<=20);

                btn6.setEnabled(mission.get(6)!=1);
                btn7.setEnabled(mission.get(7)!=1);
                btn8.setEnabled(mission.get(8)!=1);

                //월간 미션  1. 주간 미션 5개 달성
                if(mission.get(1)==0 &weeklyCount>=10){
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("1").setValue(1);
                            btn1.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);

                        }
                    });
                }
                //월간 미션  2. 주간 미션 10개 달성
                if(mission.get(2)==0 &weeklyCount>=10){
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("2").setValue(1);
                            btn2.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);

                        }
                    });
                }
                //월간 미션  3. 주간 미션 15개 달성
                if(mission.get(3)==0 &weeklyCount>=15){
                    btn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("3").setValue(1);
                            btn3.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);

                        }
                    });
                }
                //월간 미션  4. 출석 10번 달성
                if(mission.get(4)==0 &dailyCount>=10){
                    btn4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("4").setValue(1);
                            btn4.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);

                        }
                    });
                }
                //월간 미션  5. 출석 20 번 달성
                if(mission.get(5)==0 &dailyCount>=20){
                    btn5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+50;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("5").setValue(1);
                            btn5.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);

                        }
                    });
                }
                //월간 미션  6. 등산하기
                if(mission.get(6)==0 && (me.getTrailComplited()).contains(mission.get(9))){
                    btn6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+200;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("6").setValue(1);
                            btn6.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);
                        }
                    });
                }

                //월간 미션  7.  등산하기
                if(mission.get(7)==0 && (me.getTrailComplited()).contains(mission.get(10))){
                    btn7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+200;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("7").setValue(1);
                            btn7.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);
                        }
                    });
                }
                //월간 미션  8. 랜덤 등산로 등산하기
                if(mission.get(8)==0 && (me.getTrailComplited()).contains(Midx)){
                    btn8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            score = score+200;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionMonthly").child("8").setValue(1);
                            btn8.setEnabled(false);
                            flag++;
                            database.child("Users").child(uid).child("missionMonthly").child("13").setValue(flag);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}