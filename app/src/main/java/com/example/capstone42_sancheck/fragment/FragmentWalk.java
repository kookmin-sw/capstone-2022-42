package com.example.capstone42_sancheck.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;

public class FragmentWalk extends Fragment implements SensorEventListener {
    private View view;
    private TextView tv_sensor;
    private SensorManager sm;
    private Sensor stepCounterSensor;
    private TextView btn_reset;

    int steps = 0;

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
        sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(stepCounterSensor == null){
            Toast.makeText(getActivity().getApplicationContext(), "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steps = 0;
                tv_sensor.setText(String.valueOf(steps));
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
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
