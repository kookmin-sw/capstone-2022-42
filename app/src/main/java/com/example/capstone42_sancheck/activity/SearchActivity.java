package com.example.capstone42_sancheck.activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.capstone42_sancheck.R;

import org.w3c.dom.Text;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    TextView title;

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
        title = (TextView) findViewById(R.id.mountain_name);
        title.setText(m_name);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        oneMarker();
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
//    다수 마커 찍기 ( 경로 )
//    public void manyMarker() {
//        for (int idx = 0; idx < 2; idx++) {
//            MarkerOptions makerOptions = new MarkerOptions();
//            makerOptions
//                    .position(new LatLng(37.52487, 126.92723))
//                    .title("마커" + idx);
//
//            mMap.addMarker(makerOptions);
//        }
//        mMap.setOnMarkerClickListener(markerClickListener);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.52487, 126.92723)));
//    }

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

}