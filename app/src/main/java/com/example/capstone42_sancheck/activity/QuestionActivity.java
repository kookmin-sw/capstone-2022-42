package com.example.capstone42_sancheck.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.capstone42_sancheck.R;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private Button btn_ok;
    private RadioGroup rg_level;
    private RadioGroup rg_time;
    private RadioGroup rg_distance;
    private RadioButton rb_easy;
    private RadioButton rb_normal;
    private RadioButton rb_hard;
    private RadioButton rb_oneHour;
    private RadioButton rb_twoHour;
    private RadioButton rb_threeHour;
    private RadioButton rb_fourHour;
    private RadioButton rb_fiveHour;
    private RadioButton rb_sixHour;
    private RadioButton rb_sevenHour;
    private RadioButton rb_eightHour;
    private RadioButton rb_oneKm;
    private RadioButton rb_twoKm;
    private RadioButton rb_threeKm;
    private String level;
    private String time;
    private String distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ArrayList recommendArray = new ArrayList<>();

        btn_ok = (Button) findViewById(R.id.btn_ok);
        rg_level = (RadioGroup) findViewById(R.id.rg_level);
        rg_time = (RadioGroup) findViewById(R.id.rg_time);
        rg_distance = (RadioGroup) findViewById(R.id.rg_distance);
        rg_distance = (RadioGroup) findViewById(R.id.rg_distance);
        rb_easy = (RadioButton) findViewById(R.id.easy_btn);
        rb_normal = (RadioButton) findViewById(R.id.normal_btn);
        rb_hard = (RadioButton) findViewById(R.id.hard_btn);
        rb_oneHour = (RadioButton) findViewById(R.id.oneHour_btn);
        rb_twoHour = (RadioButton) findViewById(R.id.twoHour_btn);
        rb_threeHour = (RadioButton) findViewById(R.id.threeHour_btn);
        rb_fourHour = (RadioButton) findViewById(R.id.fourHour_btn);
        rb_fiveHour = (RadioButton) findViewById(R.id.fiveHour_btn);
        rb_sixHour = (RadioButton) findViewById(R.id.sixHour_btn);
        rb_sevenHour = (RadioButton) findViewById(R.id.sevenHour_btn);
        rb_eightHour = (RadioButton) findViewById(R.id.eightHour_btn);
        rb_oneKm = (RadioButton) findViewById(R.id.oneKm_btn);
        rb_twoKm = (RadioButton) findViewById(R.id.twoKm_btn);
        rb_threeKm = (RadioButton) findViewById(R.id.threeKm_btn);

        rg_level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.easy_btn:
                        level = rb_easy.getText().toString();
                        break;
                    case R.id.normal_btn:
                        level = rb_normal.getText().toString();
                        break;
                    case R.id.hard_btn:
                        level = rb_hard.getText().toString();
                        break;
                }
            }
        });
        rg_time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.oneHour_btn:
                        time = rb_oneHour.getText().toString();
                        break;
                    case R.id.twoHour_btn:
                        time = rb_twoHour.getText().toString();
                        break;
                    case R.id.threeHour_btn:
                        time = rb_threeHour.getText().toString();
                        break;
                    case R.id.fourHour_btn:
                        time = rb_fourHour.getText().toString();
                        break;
                    case R.id.fiveHour_btn:
                        time = rb_fiveHour.getText().toString();
                        break;
                    case R.id.sixHour_btn:
                        time = rb_sixHour.getText().toString();
                        break;
                    case R.id.sevenHour_btn:
                        time = rb_sevenHour.getText().toString();
                        break;
                    case R.id.eightHour_btn:
                        time = rb_eightHour.getText().toString();
                        break;
                }
            }
        });
        rg_distance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.oneKm_btn:
                        distance = rb_oneKm.getText().toString();
                        break;
                    case R.id.twoKm_btn:
                        distance = rb_twoKm.getText().toString();
                        break;
                    case R.id.threeKm_btn:
                        distance = rb_threeKm.getText().toString();
                        break;
                }
            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                recommendArray.add(level);
                recommendArray.add(time);
                recommendArray.add(distance);
                Log.d("유저가 고른 추천리스트", String.valueOf(recommendArray));

                startActivity(intent);
            }
        });
    }
}