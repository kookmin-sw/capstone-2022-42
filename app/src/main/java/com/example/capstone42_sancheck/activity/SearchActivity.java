package com.example.capstone42_sancheck.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.object.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.GeoApiContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    GoogleMap mMap;
    TextView title, sub_title, lt;
    ImageView mountain_heart, mountain_search;
    ArrayList<com.google.android.gms.maps.model.LatLng> mapList;

    private FirebaseAuth auth;
    private FirebaseDatabase database, mountainDB;
    private DatabaseReference databaseReference, mountaindatabaseReference;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    private static final String TAG = "RoadTracker";
    private GeoApiContext mContext;
    private ArrayList<LatLng> mCapturedLocations = new ArrayList<LatLng>();        //지나간 좌표 들을 저장하는 List
    private static final int PAGINATION_OVERLAP = 5;
    private static final int PAGE_SIZE_LIMIT = 100;
    private ArrayList<com.google.android.gms.maps.model.LatLng> mapPoints;

    int totalDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getIntent().getExtras();
        String m_name = bundle.getString("m_name");
        String pm_name = bundle.getString("pm_name");
        Double pm_lt = bundle.getDouble("lt");
        int index = bundle.getInt("index");
        String start = bundle.getString("start");
        String end = bundle.getString("end");

        title = (TextView) findViewById(R.id.mountain_name);
        sub_title = (TextView) findViewById(R.id.pm_name);
        lt = (TextView) findViewById(R.id.lt);
        mountain_heart = (ImageView) findViewById(R.id.search_mountain_heart);
        mountain_search = (ImageView) findViewById(R.id.search_check);

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

        database = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Users");
        mountainDB = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817.firebaseio.com/");

        mountain_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();
                databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Mountain mountainInfo = new Mountain();
                        if (snapshot.getValue(User.class) != null) {
                            if (snapshot.child("trailPlan").exists()) {
                                Toast.makeText(view.getContext(), "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                User user = snapshot.getValue(User.class);
                                user.setTrailPlanAdd(index, user.trailPlan);
                                databaseReference.child(uid).setValue(user);
                            } else {
                                List<Integer> trailPlan = new ArrayList<>(Arrays.asList(index));
                                databaseReference.child(uid).child("trailPlan").setValue(trailPlan);
                                Toast.makeText(view.getContext(), "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        mountain_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();
                databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(User.class) != null) {
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String getTime = sdf.format(date);

                            // 등산로 인덱스 추가
                            if (snapshot.child("trailComplited").exists()) {
                                Toast.makeText(view.getContext(), "등산로를 완주하였습니다!", Toast.LENGTH_SHORT).show();
                                User user = snapshot.getValue(User.class);
                                user.setTrailComplitedAdd(index, user.trailComplited);
                                databaseReference.child(uid).setValue(user);
                            } else {
                                List<Integer> trailComplited = new ArrayList<>(Arrays.asList(index));
                                databaseReference.child(uid).child("trailComplited").setValue(trailComplited);
                                Toast.makeText(view.getContext(), "등산로를 완주하였습니다!", Toast.LENGTH_SHORT).show();
                            }
                            // 등산로 완주 날짜 추가
                            if (snapshot.child("trailComplitedDate").exists()) {
                                User user = snapshot.getValue(User.class);
                                user.setTrailComplitedDate(getTime, user.trailComplitedDate);
                                databaseReference.child(uid).setValue(user);
                            } else {
                                List<String> trailComplitedDate = new ArrayList<>(Arrays.asList(getTime));
                                databaseReference.child(uid).child("trailComplitedDate").setValue(trailComplitedDate);
                            }
                        }
                        mountaindatabaseReference = mountainDB.getReference(String.valueOf(index - 1));
                        mountaindatabaseReference.child("PEOPLE").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("SearchActivity", "Error getting data", task.getException());
                                }
                                else {
                                    mountaindatabaseReference.child("PEOPLE").setValue(task.getResult().getValue(Integer.class) + 1);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public ArrayList<com.google.android.gms.maps.model.LatLng> getJsonData(){
        Thread thread = new Thread(){

            @Override
            public void run(){
                Bundle bundle = getIntent().getExtras();
                String start = bundle.getString("start");
                String end = bundle.getString("end");
                String[] startArray = start.split(" ");
                String[] endArray = end.split(" ");
                HttpClient httpClient = new DefaultHttpClient();

                String urlString = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&appKey=l7xx773d8ebf02ad45dea284564492514c01";
                try{
                    URI uri = new URI(urlString);

                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(uri);

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("startX", Double.toString(Double.parseDouble(startArray[0]))));
                    nameValuePairs.add(new BasicNameValuePair("startY", Double.toString(Double.parseDouble(startArray[1]))));

                    nameValuePairs.add(new BasicNameValuePair("endX", Double.toString(Double.parseDouble(endArray[0]))));
                    nameValuePairs.add(new BasicNameValuePair("endY", Double.toString(Double.parseDouble(endArray[1]))));

                    nameValuePairs.add(new BasicNameValuePair("startName", "출발점"));
                    nameValuePairs.add(new BasicNameValuePair("endName", "도착점"));

                    nameValuePairs.add(new BasicNameValuePair("searchOption", "10"));

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    int code = response.getStatusLine().getStatusCode();
                    String message = response.getStatusLine().getReasonPhrase();
                    Log.d(TAG, "run: " + message);
                    String responseString;
                    if(response.getEntity() != null)
                        responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    else
                        return;
                    String strData = "";

                    Log.d(TAG, "0\n");
                    JSONObject jAr = new JSONObject(responseString);

                    Log.d(TAG, "1\n");

                    JSONArray features = jAr.getJSONArray("features");
                    mapPoints = new ArrayList<>();

                    mapPoints.add(new com.google.android.gms.maps.model.LatLng(Double.parseDouble(startArray[1]), Double.parseDouble(startArray[0])));
                    for(int i=0; i<features.length(); i++)
                    {
                        JSONObject test2 = features.getJSONObject(i);
                        if(i == 0){
                            JSONObject properties = test2.getJSONObject("properties");
                            totalDistance += properties.getInt("totalDistance");
                        }
                        JSONObject geometry = test2.getJSONObject("geometry");
                        JSONArray coordinates = geometry.getJSONArray("coordinates");


                        String geoType = geometry.getString("type");
                        if(geoType.equals("Point"))
                        {
                            double lonJson = coordinates.getDouble(0);
                            double latJson = coordinates.getDouble(1);

                            Log.d(TAG, "-");
                            Log.d(TAG, lonJson+","+latJson+"\n");
                            com.google.android.gms.maps.model.LatLng point = new com.google.android.gms.maps.model.LatLng(latJson, lonJson);
                            mapPoints.add(point);

                        }
                        if(geoType.equals("LineString"))
                        {
                            for(int j=0; j<coordinates.length(); j++)
                            {
                                JSONArray JLinePoint = coordinates.getJSONArray(j);
                                double lonJson = JLinePoint.getDouble(0);
                                double latJson = JLinePoint.getDouble(1);

                                Log.d(TAG, "-");
                                Log.d(TAG, lonJson+","+latJson+"\n");
                                com.google.android.gms.maps.model.LatLng point = new com.google.android.gms.maps.model.LatLng(latJson, lonJson);

                                mapPoints.add(point);

                            }
                        }
                    }
                    mapPoints.add(new com.google.android.gms.maps.model.LatLng(Double.parseDouble(endArray[1]), Double.parseDouble(endArray[0])));

                } catch (URISyntaxException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        thread.start();

        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return mapPoints;
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
        BitmapDrawable startbitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_startmarker);
        Bitmap b = startbitmapdraw.getBitmap();
        Bitmap startsmallMarker = Bitmap.createScaledBitmap(b, 120, 120, false);
        makerOptions
                .position(start_pnt)
                .title("시작점")
                .icon(BitmapDescriptorFactory.fromBitmap(startsmallMarker));

        mMap.addMarker(makerOptions);
        mMap.setOnMarkerClickListener(markerClickListener);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_pnt, 16));

        MarkerOptions makerOptions1 = new MarkerOptions();
        BitmapDrawable finishbitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_finishmarker);
        Bitmap b1 = finishbitmapdraw.getBitmap();
        Bitmap finishsmallMarker = Bitmap.createScaledBitmap(b1, 120, 120, false);
        makerOptions1
                .position(end_pnt)
                .title("도착점")
                .icon(BitmapDescriptorFactory.fromBitmap(finishsmallMarker));

        mMap.addMarker(makerOptions1);
        mMap.setOnMarkerClickListener(markerClickListener);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(SearchActivity.this, "마커", Toast.LENGTH_LONG);
                return false;
            }
        });

        mapList = getJsonData();
        if (mapList != null) {
            for ( int i = 0 ; i < mapList.size() - 1; i++){
                Polyline polyline = mMap.addPolyline(new PolylineOptions()
                        .add(mapList.get(i), mapList.get(i + 1))
                        .width(5)
                        .color(Color.RED));
            }
        }
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