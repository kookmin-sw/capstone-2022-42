package com.example.capstone42_sancheck.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.activity.SignInActivity;

public class FragSignIn3 extends Fragment {
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sign_in3, container, false);

        Button googleBtn = view.findViewById(R.id.btn_google);

        /** SignInActivity ==> MainActivity 화면전환 **/
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
