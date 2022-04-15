package com.example.capstone42_sancheck.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentHome extends Fragment {
    private View view;

    private FirebaseAuth auth;
    private DatabaseReference database;

    private TextView tv_name;
    private ImageView iv_profile;
    private ImageView iv_edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);

        tv_name = view.findViewById(R.id.tv_name);
        iv_profile = view.findViewById(R.id.iv_profile);
        iv_edit = (ImageView) view.findViewById(R.id.iv_edit);

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("닉네임 변경");
                ad.setMessage("변경할 닉네임을 입력하세요.(1~10자)");

                final EditText et = new EditText(view.getContext());
                ad.setView(et);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = et.getText().toString();
                        tv_name.setText(result);
                        dialogInterface.dismiss();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ad.show();

            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        tv_name.setText(user.getDisplayName());
        Glide.with(getActivity()).load(user.getPhotoUrl()).into(iv_profile);

        database = FirebaseDatabase.getInstance().getReference();

        return view;

    }

    // user data 추가
    private void writeNewUser(String email){
        User member = new User(0);

        database.child("User").child(email).setValue(member)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 성공
                        Log.d("writeNewUser", "유저 정보 저장 성공!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 실패
                        Log.d("writeNewUser", "유저 정보 저장 실패ㅠ");
                    }
                });
    }

    // user data 불러오기
    private void readUser(String email){
        database.child("User").child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class) != null){
                    User post = snapshot.getValue(User.class);
                    Log.d("readUser", "유저 정보 불러오기 성공!");
                } else{
                    Log.d("readUser", "유저 정보 없음...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("readUser", "유저 정보 불러오기 실패ㅠ");
            }
        });
    }
}
