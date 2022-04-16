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
import com.google.android.gms.tasks.OnSuccessListener;
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

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        final String uid = user.getUid();

        database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class) != null){
                    User post = snapshot.getValue(User.class);
                    tv_name.setText(post.getName());
                    Log.d("FragmentHome", "유저 정보 불러오기 성공!");

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
                                    post.setName(result);
                                    database.child("Users").child(uid).setValue(post)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("iv_edit", "유저 정보 수정 확인!");
                                                }
                                            });
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

                    Glide.with(getActivity()).load(user.getPhotoUrl()).into(iv_profile);
                } else{
                    Log.d("FragmentHome", "유저 정보 없음...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FragmentHome", "유저 정보 불러오기 실패ㅠ");
            }
        });

        return view;

    }
}
