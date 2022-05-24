package com.example.capstone42_sancheck.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.object.Rank;
import com.example.capstone42_sancheck.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.lang.String;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class FragmentMission1 extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference database;
    int flag;
    int walkToday;
    View view;
    int temp;
    int score;
    int montlyC;

    //현재시간
    String day = new SimpleDateFormat("yyyyMMdd(E)").format(new Date());
    int today =  Integer.parseInt(day.substring(0,8));
    String dow = Character.toString(day.charAt(9));

    int dailyCount;


    List<Integer> mission ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mission1, container, false);
        Button btn1 = (Button) view.findViewById(R.id.button1) ;
        Button btn2 = (Button) view.findViewById(R.id.button2) ;
        Button btn3 = (Button) view.findViewById(R.id.button3) ;
        ProgressBar progress0 = (ProgressBar) view.findViewById(R.id.progress0);
        ProgressBar progress1 = (ProgressBar) view.findViewById(R.id.progress1);
        ProgressBar progress2 = (ProgressBar) view.findViewById(R.id.progress2);
        ProgressBar progress3 = (ProgressBar) view.findViewById(R.id.progress3);


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();

        database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User me = snapshot.getValue(User.class);
                int dateCmp = me.getDailyCheck();

                if(me.getMissionDaily()==null){
                    mission= new ArrayList(Collections.nCopies(9, 0));
                    me.setMissionDaily(mission);
                    database.child("Users").child(uid).setValue(me);
                }

                if(me.getMissionMonthly()==null){
                    mission= new ArrayList(Collections.nCopies(14, 0));
                    me.setMissionMonthly(mission);
                    database.child("Users").child(uid).setValue(me);
                }

                if(me.getMissionWeekly()==null){
                    mission= new ArrayList(Collections.nCopies(8, 0));
                    me.setMissionWeekly(mission);
                    database.child("Users").child(uid).setValue(me);
                }

                montlyC = me.getMissionMonthly().get(12);
                score = me.getScore();
                walkToday = me.getWalkDaily();
                mission = me.getMissionDaily();
                temp = me.getMissionDailyCount();



                if(dateCmp!=today) {

                    if(dateCmp!=0&&(Integer.toString(dateCmp)).charAt(5)!=(Integer.toString(today)).charAt(5)){
                        database.child("Users").child(uid).child("missionMontly").child("8").setValue(-1);

                    }
                    me.setDailyCheck(today);
                    mission= new ArrayList(Collections.nCopies(7, 0));
                    flag=0;
                    me.setMissionDaily(mission);
                    me.setWeeklyCheck(dow);
                    database.child("Users").child(uid).setValue(me);
                }
                flag= mission.get(0);

                progress0.setProgress(flag);
                progress1.setProgress(mission.get(1));
                progress2.setProgress(me.getWalkDaily());
                progress3.setProgress(me.getWalkDaily());

               // btn0.setEnabled(mission.get(0)<3);
                btn1.setEnabled(mission.get(1)!=1);
                btn2.setEnabled(mission.get(2)!=1);
                btn3.setEnabled(mission.get(3)!=1);

               /* if(flag==3){
                    btn0.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            score = score+30;
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionDaily").child("0").setValue(0);
                            btn0.setEnabled(false);
                        }
                    });
                }*/
                //일일미션-1  출석하기
                if(mission.get(1)!=1) {
                    btn1.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            score = score+10;
                            flag++;
                            montlyC++;
                            database.child("Users").child(uid).child("missionMonthly").child("12").setValue(montlyC);
                            database.child("Users").child(uid).child("missionDailyCount").setValue(temp+1);
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionDaily").child("1").setValue(1);
                            database.child("Users").child(uid).child("missionDaily").child("0").setValue(flag);
                            btn1.setEnabled(false);
                        }
                    });
                }


                //일일미션-2
                if(walkToday>=3000 &&mission.get(2)!=1) {
                    btn2.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            score = score + 20;
                            flag++;
                            database.child("Users").child(uid).child("missionDailyCount").setValue(temp+1);
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionDaily").child("2").setValue(1);
                            database.child("Users").child(uid).child("missionDaily").child("0").setValue(flag);
                            btn2.setEnabled(false);


                        }
                    });
                }

                //일일미션-3
                if(walkToday>=5000&&mission.get(3)!=1) {
                    btn3.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            score = score + 30;
                            flag++;
                            database.child("Users").child(uid).child("missionDailyCount").setValue(temp+1);
                            database.child("Users").child(uid).child("score").setValue(score);
                            database.child("Users").child(uid).child("missionDaily").child("3").setValue(1);
                            database.child("Users").child(uid).child("missionDaily").child("0").setValue(flag);
                            btn3.setEnabled(false);

                        }
                    });
                }

                //database.child("Users").child(uid).child("missionDaily").setValue(mission);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });





        return view;
    }
}