package com.example.capstone42_sancheck.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentBoard extends Fragment {
    View view;
    TabLayout tabs;

    FragmentMission1 fragment1;
    FragmentMission2 fragment2;
    FragmentMission3 fragment3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board, container, false);
        fragment1 = new FragmentMission1();
        fragment2 = new FragmentMission2();
        fragment3 = new FragmentMission3();

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        tabs = view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("일간 미션"));
        tabs.addTab(tabs.newTab().setText("주간 미션"));
        tabs.addTab(tabs.newTab().setText("월간 미션"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Fragment sel =null;
                if(pos==0)
                    sel =fragment1;
                else if(pos==1)
                    sel=fragment2;
                else if(pos==2)
                    sel=fragment3;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,sel).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}
