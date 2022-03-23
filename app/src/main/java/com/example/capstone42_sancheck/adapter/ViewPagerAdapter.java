package com.example.capstone42_sancheck.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.capstone42_sancheck.fragment.FragSignIn1;
import com.example.capstone42_sancheck.fragment.FragSignIn2;
import com.example.capstone42_sancheck.fragment.FragSignIn3;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public int mCount;

    public ViewPagerAdapter(FragmentActivity fa, int count){
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new FragSignIn1();
        else if(index==1) return new FragSignIn2();
        else return new FragSignIn3();
    }

    @Override
    public int getItemCount() {
        return 3000;
    }

    public int getRealPosition(int position){return position % mCount;}
}
