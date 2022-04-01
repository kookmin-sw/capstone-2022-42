package com.example.capstone42_sancheck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.fragment.FragmentBoard;
import com.example.capstone42_sancheck.fragment.FragmentHome;
import com.example.capstone42_sancheck.fragment.FragmentRank;
import com.example.capstone42_sancheck.fragment.FragmentSearch;
import com.example.capstone42_sancheck.fragment.FragmentWalk;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 메뉴바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentHome fh;
    private FragmentWalk fw;
    private FragmentBoard fb;
    private FragmentRank fr;
    private FragmentSearch fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.board:
                        setFrag(1);
                        break;
                    case R.id.walk:
                        setFrag(2);
                        break;
                    case R.id.rank:
                        setFrag(3);
                        break;
                    case R.id.search:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
        fh = new FragmentHome();
        fw = new FragmentWalk();
        fb = new FragmentBoard();
        fr = new FragmentRank();
        fs = new FragmentSearch();
        setFrag(0); // 첫 화면 지정
    }

    // 프래그먼트 교체
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, fh);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, fb);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, fw);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, fr);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, fs);
                ft.commit();
                break;
        }
    }
}