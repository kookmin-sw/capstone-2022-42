package com.example.capstone42_sancheck.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.fragment.FragmentBoard;
import com.example.capstone42_sancheck.fragment.FragmentHome;
import com.example.capstone42_sancheck.fragment.FragmentRank;
import com.example.capstone42_sancheck.fragment.FragmentSearch;
import com.example.capstone42_sancheck.fragment.FragmentWalk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class Recommendation extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        textView = (TextView) findViewById(R.id.textViewRecomm);

        if (! Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
//        ArrayList recommendArray = new ArrayList<>();
//        ArrayList sayongja = new ArrayList<>();
//        ArrayList usercod = new ArrayList<>();
//        sayongja.add(0,0.02);
//        sayongja.add(1,2);
//        sayongja.add(2,3);
//        usercod.add(0,126.9);
//        usercod.add(1,37.5);
//        int [] sayongja = {};
//        int [] usercod = {};

        Python py = Python.getInstance();
        //create python object
        PyObject pyobj = py.getModule("myscript"); //give python script name
        //call this function
        PyObject obj = pyobj.callAttr("main",0.02, 2, 3,126.9, 37.5);

        String recommendArray = obj.toString();

        String[] cityArr = recommendArray.split(",");

        int arraySize = cityArr.length;

        for(int i =0; i< arraySize/2; i++){
            textView.append(cityArr[2*i]);
            textView.append("-");
            textView.append(cityArr[2*i+1]);
            textView.append("\n");

        }
//
//
//
//        System.out.println(cityArr);
//
//        textView.setText(recommendArray);
    }

}