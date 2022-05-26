package com.example.capstone42_sancheck.activity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recommendation extends AppCompatActivity  {
    TextView textView;
    private FusedLocationProviderClient client;
    RecyclerView recyclerView;
    SimpleTextAdapter simpleTextAdapter;


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
        PyObject obj = pyobj.callAttr("main",0.02, 2, 3,latitude1,longitude1);
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
//        dialog.dismiss();
//        System.out.println(cityArr);
//
//        textView.setText(recommendArray);
    }
}