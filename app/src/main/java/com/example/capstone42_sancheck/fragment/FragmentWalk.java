package com.example.capstone42_sancheck.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.object.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentWalk extends Fragment implements SensorEventListener {
    private View view;
    private TextView tv_sensor;
    private SensorManager sm;
    private Sensor stepCounterSensor;
    private TextView btn_reset;
    private ProgressBar progressBar;

    int steps = 0;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_walk, container, false);

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        tv_sensor = (TextView) view.findViewById(R.id.sensor);
        btn_reset = (TextView) view.findViewById(R.id.btn_reset);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(stepCounterSensor == null){
            Toast.makeText(getActivity().getApplicationContext(), "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        final String uid = user.getUid();

        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(User.class) != null){
                    User post = snapshot.getValue(User.class);

                    if (steps != post.getWalkDaily()){
                        steps = post.getWalkDaily();
                    }
                    tv_sensor.setText(String.valueOf(steps));
                    progressBar.setProgress(80); //user input steps
                    progressBar.setMax(1000); //user daily max stepcount

                    btn_reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            steps = 0;
                            post.setWalkDaily(0);
                            database.child("Users").child(uid).child("walkDaily").setValue(steps)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("FragmentWalk: walkReset", "걷기 정보 리셋");
                                        }
                                    });
                            tv_sensor.setText(String.valueOf(steps));
                        }
                    });
                } else{
                    Log.d("FragmentWalk", "유저 정보 없음...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FragmentWalk", "유저 정보 불러오기 실패ㅠ");
            }
        });

        return view;
    }
    public void onStart(){
        super.onStart();
        // 센서 속도 설정
        // * 옵션
        // - SENSOR_DELAY_NORMAL: 200,000ms 딜레이
        // - SENSOR_DELAY_UI: 60,000ms 딜레이
        // - SENSOR_DELAY_GAME: 20,000ms 딜레이
        // - SENSOR_DELAY_FASTEST: 0ms
        //
        if(stepCounterSensor != null){
            sm.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            if(sensorEvent.values[0]==1.0f){
                steps++;
                tv_sensor.setText(String.valueOf(steps));

                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                final String uid = user.getUid();

                database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(User.class) != null) {
                            User post = snapshot.getValue(User.class);

                            if (steps > post.getWalkDaily()) {
                                post.setWalkDaily(steps);
                                database.child("Users").child(uid).child("walkDaily").setValue(steps)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("onSensorChanged", "유저 정보 수정 확인!");
                                            }
                                        });
                            }
                        }
                        else{
                            Log.d("onSensorChanged", "유저 정보 없음...");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("onSensorChanged", "유저 정보 불러오기 실패ㅠ");
                    }
                });
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
