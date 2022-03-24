package com.example.capstone42_sancheck.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.adapter.ViewPagerAdapter;

import me.relex.circleindicator.CircleIndicator3;

public class SignInActivity extends AppCompatActivity {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /**
         * 가로 슬라이드 뷰 Fragment
         */
//ViewPager2
        mPager = findViewById(R.id.viewPager);
//Adapter
        pagerAdapter = new ViewPagerAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);
//Indicator
        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);
//ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
/**
 * 이 부분 조정하여 처음 시작하는 이미지 설정.
 * 2000장 생성하였으니 현재위치 1002로 설정하여
 * 좌 우로 슬라이딩 할 수 있게 함. 거의 무한대로
 */
        mPager.setCurrentItem(1002); //시작 지점
        mPager.setOffscreenPageLimit(3); //최대 이미지 수
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }
        });

    }
}