package com.example.capstone42_sancheck.activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.LocationListener;
import android.widget.Button;
import android.widget.ProgressBar;



import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.fragment.FragmentBoard;
import com.example.capstone42_sancheck.fragment.FragmentHome;
import com.example.capstone42_sancheck.fragment.FragmentRank;
import com.example.capstone42_sancheck.fragment.FragmentSearch;
import com.example.capstone42_sancheck.fragment.FragmentWalk;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import androidx.core.content.ContextCompat;

public class Recommendation extends AppCompatActivity  {
    TextView textView;
    private FusedLocationProviderClient client;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    String level;
    double time;
    double distance;
    double latitude1;
    double longitude1;

    public void startLocationService() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (location == null) {
        double latitude1 = location.getLatitude();
        double longitude1 = location.getLongitude();

//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ProgressDialog 생성
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("processing...");
        dialog.show();
        super.onCreate(savedInstanceState);

        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();

        mDatabase.child("Users").child(uid).child("recommend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                level = snapshot.child("0").getValue(String.class);
                String s_time = snapshot.child("1").getValue(String.class);
                String s_distance = snapshot.child("2").getValue(String.class);

                time = Character.getNumericValue(s_time.charAt(0));
                distance = Character.getNumericValue(s_distance.charAt(0));

                Log.d("time = ", String.valueOf(level));
                Log.d("time = ", String.valueOf(time));
                Log.d("time = ", String.valueOf(distance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setContentView(R.layout.activity_recommendation);
        client = LocationServices.getFusedLocationProviderClient(this);
//        spinner = (ProgressBar)findViewById(R.id.progressBar);

        textView = (TextView) findViewById(R.id.textViewRecomm);

        if (! Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        //create python object
        PyObject pyobj = py.getModule("myscript"); //give python script name

        //call this function
        if (longitude1 == 0 || latitude1 == 0){
            longitude1 = 37.5232323232323;
            latitude1 = 126.94342324234;

        }
        PyObject obj = pyobj.callAttr("main",0.02, 2, 3,latitude1,longitude1);
        //PyObject obj = pyobj.callAttr("main",0.02, 2, 3,longitude1,latitude1);
        Log.d("유저가 고른 추천리스트", String.valueOf(longitude1));
        Log.d("유저가 고른 추천리스트", String.valueOf(latitude1));

        String recommendArray = obj.toString();

        String[] cityArr = recommendArray.split(",");

        int arraySize = cityArr.length;

        for(int i =0; i< arraySize/2; i++){
            textView.append(cityArr[2*i]);
            textView.append("-");
            textView.append(cityArr[2*i+1]);
            textView.append("\n");
        }
        // ProgressDialog 없애기
//        dialog.dismiss();
//        System.out.println(cityArr);
//
//        textView.setText(recommendArray);
    }

}