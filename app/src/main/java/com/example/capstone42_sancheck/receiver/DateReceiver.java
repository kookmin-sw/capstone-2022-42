package com.example.capstone42_sancheck.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.capstone42_sancheck.object.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateReceiver extends BroadcastReceiver {
    public DateReceiver(){

    }

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = auth.getCurrentUser();

    final String uid = user.getUid();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(Intent.ACTION_DATE_CHANGED, intent.getAction())){
            database.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue(User.class) != null){
                        User post = snapshot.getValue(User.class);

                        switch (getCurrentWeek()){
                            case 2:
                                Log.d("DateReceiver", "일요일 걷기 정보 업데이트!!");
                                post.setWalkSun(post.getWalkDaily());
                                break;
                            case 3:
                                Log.d("DateReceiver", "월요일 걷기 정보 업데이트!!");
                                post.setWalkMon(post.getWalkDaily());
                                break;
                            case 4:
                                Log.d("DateReceiver", "화요일 걷기 정보 업데이트!!");
                                post.setWalkTue(post.getWalkDaily());
                                break;
                            case 5:
                                Log.d("DateReceiver", "수요일 걷기 정보 업데이트!!");
                                post.setWalkWen(post.getWalkDaily());
                                break;
                            case 6:
                                Log.d("DateReceiver", "목요일 걷기 정보 업데이트!!");
                                post.setWalkThu(post.getWalkDaily());
                                break;
                            case 7:
                                Log.d("DateReceiver", "금요일 걷기 정보 업데이트!!");
                                post.setWalkFri(post.getWalkDaily());
                                break;
                            case 1:
                                Log.d("DateReceiver", "토요일 걷기 정보 업데이트!!");
                                post.setWalkSat(post.getWalkDaily());
                                break;
                        } // end of switch

                        post.setWalkTotal(post.getWalkTotal() + post.getWalkDaily());
                        post.setWalkDaily(-1);

                        database.child(uid).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("DateReceiver", "유저 걷기 정보 저장 완료:)");
                            }
                        });
                    } else{
                        Log.d("DateReceiver", "유저 정보 없음...");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("DateReceiver", "유저 정보 불러오기 실패ㅠ");
                }
            });
        }
    }

    public static int getCurrentWeek(){
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeekNumber;
    }
}
