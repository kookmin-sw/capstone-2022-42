package com.example.capstone42_sancheck.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;

public class FragSignIn1 extends Fragment {
    private View view;

    public static FragSignIn1 newInstance(){
        FragSignIn1 fragSignIn1 = new FragSignIn1();
        return fragSignIn1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sign_in1, container, false);

        return view;
    }
}
