package com.example.capstone42_sancheck.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.capstone42_sancheck.R;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

public class SearchActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    GoogleMap mMap;
    TextView title, sub_title, lt;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getIntent().getExtras();
        String m_name = bundle.getString("m_name");
        String pm_name = bundle.getString("pm_name");
        Double pm_lt = bundle.getDouble("lt");
        title = (TextView) findViewById(R.id.mountain_name);
        sub_title = (TextView) findViewById(R.id.pm_name);
        lt = (TextView) findViewById(R.id.lt);

        title.setText(m_name);
        sub_title.setText(pm_name);
        lt.setText(pm_lt + "km");

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true); // 지도 줌 활성화
        mMap.getUiSettings().setMyLocationButtonEnabled(true); // 현재위치 활성화
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        mMap.setMyLocationEnabled(true);
        oneMarker();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
//            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true)L
              PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                      true);
        }
    }

    public void oneMarker() {
        Bundle bundle = getIntent().getExtras();
        String start = bundle.getString("start");
        String end = bundle.getString("end");
        String[] startArray = start.split(" ");
        String[] endArray = end.split(" ");

        // 초기 위치 설정
        LatLng start_pnt = new LatLng(Double.parseDouble(startArray[1]), Double.parseDouble(startArray[0]));
        LatLng end_pnt = new LatLng(Double.parseDouble(endArray[1]), Double.parseDouble(endArray[0]));

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(start_pnt)
                .title("시작점")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .alpha(0.5f);

        mMap.addMarker(makerOptions);
        mMap.setOnMarkerClickListener(markerClickListener);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_pnt, 16));

        MarkerOptions makerOptions1 = new MarkerOptions();
        makerOptions1
                .position(end_pnt)
                .title("도착점")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f);

        mMap.addMarker(makerOptions1);
        mMap.setOnMarkerClickListener(markerClickListener);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(SearchActivity.this, "마커", Toast.LENGTH_LONG);
                return false;
            }
        });
    }

    //마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            //선택한 타겟위치
            LatLng location = marker.getPosition();
            Toast.makeText(SearchActivity.this, "위치 : "+markerId+"("+location.latitude+" "+location.longitude+")", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            permissionDenied = false;
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    };
}