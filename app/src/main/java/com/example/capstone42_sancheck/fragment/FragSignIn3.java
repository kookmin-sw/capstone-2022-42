package com.example.capstone42_sancheck.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.activity.MainActivity;
import com.example.capstone42_sancheck.activity.QuestionActivity;
import com.example.capstone42_sancheck.activity.SignInActivity;
import com.example.capstone42_sancheck.object.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FragSignIn3 extends Fragment {
    private View view;

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton btn_google;

    private DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sign_in3, container, false);

        auth = FirebaseAuth.getInstance();
        btn_google = view.findViewById(R.id.btn_google);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // default_web_client_id ??????: ??? ??? ??? ?????? ?????? ?????????
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // ????????? ????????????
        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        return view;
    }

    private void signIn(){
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCodde, int resultCode, Intent data){
        super.onActivityResult(requestCodde, resultCode, data);

        if(requestCodde == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent((data));
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ // ????????? ??????
                            Toast.makeText(getActivity(), "????????? ??????", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();

                            final String uid = user.getUid();

                            readUser(uid, user);
                        } else{ // ????????? ??????
                            Toast.makeText(getActivity(), "????????? ??????", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    // ?????? user ?????? ?????? ??? ??????
    private void readUser(String uid, FirebaseUser user){
        database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(User.class) != null){
                    Log.d("readUser", "????????????!");
                    updateUI(user);
                } else{
                    Log.d("readUser", "??? ?????????: writeNewUser() ????????? ??????");
                    writeNewUser(uid, user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("readUser", "????????? ?????? ?????? ??????");
            }
        });
    }

    // user data ??????
    private void writeNewUser(String uid, FirebaseUser user){
        List<Integer> trailComplited = new ArrayList<>(); // ????????? ???????????? []??? ?????? - ?????? ??????
        List<Integer> trailPlan = new ArrayList<>(); // ????????? ???????????? []??? ?????? - ?????? ??????

        List<String> trailComplitedDate = new ArrayList<>(); // ????????? ???????????? []??? ?????? - ?????? ??????

        List<Integer> missionDaily = new ArrayList<>();
        List<Integer> missionWeekly = new ArrayList<>();
        List<Integer> missionMonthly = new ArrayList<>();

        List<String> recommendation = new ArrayList<>();

        // ????????? rank ?????? ????????? ????????? ?????? ?????? ??????
        User member = new User(user.getDisplayName(), 0, 0, 10000,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                trailComplited, trailPlan, trailComplitedDate,
                missionDaily, missionWeekly, missionMonthly, recommendation,
                0, 0, 0, "", 0);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child(uid).setValue(member)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ??????
                        Log.d("writeNewUser", "?????? ?????? ?????? ??????!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // ??????
                        Log.d("writeNewUser", "?????? ?????? ?????? ?????????");
                    }
                });
    }
}
