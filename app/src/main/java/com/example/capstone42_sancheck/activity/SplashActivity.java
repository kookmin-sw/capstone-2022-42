package com.example.capstone42_sancheck.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(2000); // 로딩화면 대기시간
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, SignInActivity.class)); // 대기후 화면전환
        finish();
    }
}
