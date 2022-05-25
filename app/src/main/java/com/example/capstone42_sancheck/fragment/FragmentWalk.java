package com.example.capstone42_sancheck.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.object.User;
import com.example.capstone42_sancheck.receiver.AlarmReceiver;
import com.example.capstone42_sancheck.receiver.DateReceiver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    // 알림 기능 변수
    private AlarmManager alarmManager;

    private NotificationManager notificationManager;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

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

                    if (post.getWalkDaily() == -1){
                        steps = 0;
                        post.setWalkDaily(0);
                        database.child("Users").child(uid).child("walkDaily").setValue(steps)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("FragmentWalk", "하루가 지남 - 걷기 정보 초기화");
                                    }
                                });
                    }
                    else if (steps != post.getWalkDaily()){
                        steps = post.getWalkDaily();
                    }
                    tv_sensor.setText(String.valueOf(steps));
                    progressBar.setProgress(steps); //user input steps
                    progressBar.setMax(post.getGoal()); //user daily max stepcount

                    notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                    pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                    editor = pref.edit();
                    boolean temp = pref.getBoolean("alarm", false);
                    String clearDate = pref.getString("date", getDate());

                    if (steps >= post.getGoal() && temp && !clearDate.equals(getDate())){
                        editor.putString("date", getDate());
                        editor.commit();
                        setAlarm();
                    }

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
                            progressBar.setProgress(steps); //user input steps
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

        BroadcastReceiver br = new DateReceiver();

        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        getActivity().registerReceiver(br, filter);

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
                progressBar.setProgress(steps); //user input steps

                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                final String uid = user.getUid();

                database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(User.class) != null) {
                            User post = snapshot.getValue(User.class);

                            if (post.getWalkDaily() == -1){
                                steps = 0;
                            }

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

    private String getDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return simpleDateFormat.format(date);
    }

    private void setAlarm(){
        // AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, receiverIntent, 0);

        Log.d("setAlarm", "목표 걸음 달성 알림");

        String form = getDate() + " 23:59:59"; // 하루가 끝나기 전 목표치 달성한 경우 알림
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try{
            datetime = dateFormat.parse(form);
        } catch (ParseException e){
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        Log.d("setAlarm", form + "에 알람 울림!");

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
