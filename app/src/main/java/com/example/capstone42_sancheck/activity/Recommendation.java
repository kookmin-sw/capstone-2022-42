package com.example.capstone42_sancheck.activity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.capstone42_sancheck.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recommendation extends AppCompatActivity  {
    TextView textView;
    private FusedLocationProviderClient client;
    RecyclerView recyclerView;
    SimpleTextAdapter simpleTextAdapter;

    String level;
    String time;
    String distance;

    double d_time;
    double d_distance;
    double latitude1;
    double longitude1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

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

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();

        databaseReference.child("Users").child(uid).child("recommendation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                level = snapshot.child("0").getValue().toString();
                time = snapshot.child("1").getValue().toString();
                distance = snapshot.child("2").getValue().toString();

                d_time = Character.getNumericValue(time.charAt(0));
                d_distance = Character.getNumericValue(distance.charAt(0));

                Log.d("level = ", String.valueOf(level));
                Log.d("time = ", String.valueOf(d_time));
                Log.d("distance = ", String.valueOf(d_distance));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setContentView(R.layout.activity_recommendation);
        client = LocationServices.getFusedLocationProviderClient(this);
//        spinner = (ProgressBar)findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclller_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false)) ;

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
        if (d_time == 0 || d_distance == 0){
            d_time = 3;
            d_distance = 2;
        }
        PyObject obj = pyobj.callAttr("main",d_distance, d_time*0.4, d_time*0.6,latitude1,longitude1);
        //PyObject obj = pyobj.callAttr("main",0.02, 2, 3,longitude1,latitude1);
        Log.d("유저가 고른 추천리스트", String.valueOf(longitude1));
        Log.d("유저가 고른 추천리스트", String.valueOf(latitude1));

        String recommendArray = obj.toString();

        String[] cityArr = recommendArray.split(",");

        int arraySize = cityArr.length;

        ArrayList<String> list  = new ArrayList<>();

        simpleTextAdapter = new SimpleTextAdapter();

        for(int i =0; i< arraySize/2; i++){
            cityArr[2*i] = cityArr[2*i].replaceAll("[^\uAC00-\uD7A30-9a-zA-Z]","");
            cityArr[2*i+1] = cityArr[2*i+1].replaceAll("[^\uAC00-\uD7A30-9a-zA-Z]","");

            simpleTextAdapter.setArrayData(String.format(cityArr[2*i]));
            simpleTextAdapter.setArrayData2(String.format(cityArr[2*i+1]));




//            list.add(String.format(cityArr[2*i+1]));
//            recyclerView.add(cityArr[2*i]);
//            textView.append("-");
//            textView.append(cityArr[2*i+1]);
//            textView.append("\n");
        }
        recyclerView.setAdapter(simpleTextAdapter);

//        RecyclerView recyclerView = findViewById(R.id.textViewRecomm);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        SimpleTextAdapter adapter = new SimpleTextAdapter(list) ;
//        recyclerView.setAdapter(adapter) ;


        // ProgressDialog 없애기
        dialog.dismiss();
//        System.out.println(cityArr);
//
//        textView.setText(recommendArray);
    }
}