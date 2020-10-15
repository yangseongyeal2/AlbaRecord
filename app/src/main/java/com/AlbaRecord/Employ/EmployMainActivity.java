package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.AlbaRecord.Board.BoardActivity;
import com.AlbaRecord.Board.SearchActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Notification.AlramActivity;
import com.AlbaRecord.R;
import com.AlbaRecord.login.LoginWayActivity;
import com.AlbaRecord.Boss.SearchEmployeeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class EmployMainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private final String TAG="메인액티비티";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button myhome,myshop,myboss,Q_A,searchBoss,logout,Alram;
    private MapView map_view;
    private NaverMap naverMap;
    List<BossModel> bosslist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employ_main);
        initToolBar();
        initViewId();
        //String adress=getIntent().getStringExtra("주소");
        Retreive_bosslist();
        myhome.setOnClickListener(this);
        myshop.setOnClickListener(this);
        myboss.setOnClickListener(this);
        Q_A.setOnClickListener(this);
        searchBoss.setOnClickListener(this);
        logout.setOnClickListener(this);
        Alram.setOnClickListener(this);







//        try{      //해쉬키 얻는 코드
//
//            PackageInfo info = getPackageManager().getPackageInfo("com.AlbaRecord",
//                    PackageManager.GET_SIGNATURES);revert
//            for(Signature signature : info.signatures){
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("990du68u3n"));


        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(35.1798159, 100.0750222), 8))
                .mapType(NaverMap.MapType.Hybrid)
                .enabledLayerGroups(NaverMap.LAYER_GROUP_BUILDING)
                .enabledLayerGroups(NaverMap.LAYER_GROUP_TRANSIT)
                .liteModeEnabled(true)
                .lightness(0.3f);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);//Onmapready메소드 호출

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                       // String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
//                    }
//                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


       // refreshLatLon();



    }

//    private void refreshLatLon() {
//        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                EmployeeModel employeeModel=documentSnapshot.toObject(EmployeeModel.class);
//                List<Address> list = null;
//                final Geocoder geocoder = new Geocoder(getApplicationContext());
//                double lat, lon;
//                try {
//                    list = geocoder.getFromLocationName(employeeModel.getAdress(), 10);
//                    Address addr = list.get(0);
//                    lat = addr.getLatitude();
//                    lon = addr.getLongitude();
//                    Log.d("위도", String.valueOf(lat));
//                    Log.d("경도", String.valueOf(lon));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("실패", "list.get(0).toString()");
//                    return;
//                }
//                db.collection("employee").document(mAuth.getCurrentUser().getUid()).update("latitude",lat);
//                db.collection("employee").document(mAuth.getCurrentUser().getUid()).update("logtitude",lon);
//            }
//        });
//    }


    private void initViewId() {
        myhome=(Button)findViewById(R.id.myhome);
        myshop=(Button)findViewById(R.id.myshop);
        myboss=(Button)findViewById(R.id.myboss);
        Q_A=(Button)findViewById(R.id.Q_A);
        searchBoss=(Button)findViewById(R.id.searchBoss);
        Alram=(Button)findViewById(R.id.Alram);


    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        // myhome,myshop,myboss,Q_A,searchBoss,logout;
        switch (v.getId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginWayActivity.class));
                finish();
                break;
            case R.id.myhome:
                startActivity(new Intent(getApplicationContext(), EmployeeMyhomeActivity.class));
                break;
            case R.id.myshop:
                startActivity(new Intent(getApplicationContext(), EmployeeMyshop.class));
                break;
            case R.id.myboss:
                startActivity(new Intent(getApplicationContext(), SearchEmployeeActivity.class));
                break;
            case R.id.Q_A:
                startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                break;
            case R.id.searchBoss:
                startActivity(new Intent(getApplicationContext(), EmployeeBossSearchActivity.class));
                break;
            case R.id.Alram:
                startActivity(new Intent(getApplicationContext(), AlramActivity.class));
                break;

        }

    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        Log.d("맵레디", "성공");
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

//        Marker marker = new Marker();
//        marker.setPosition(new LatLng(37.5670135, 126.9783740));
//        marker.setWidth(50);
//        marker.setHeight(80);
//        marker.setIconTintColor(Color.RED);
//        marker.setMap(naverMap);
    }

    private void Retreive_bosslist() {
        db.collection("boss").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BossModel bossModel = document.toObject(BossModel.class);
                        bosslist.add(bossModel);
                        Log.d("TAG", document.getId() + " => " + document.getData());

                        Marker marker = new Marker();
                        Log.d("위도경도설정", String.valueOf(bossModel.getLatitude()));
                        marker.setPosition(new LatLng(bossModel.getLatitude(), bossModel.getLongtitude()));
                        marker.setWidth(50);
                        marker.setHeight(80);
                        marker.setIconTintColor(Color.RED);
                        marker.setMap(naverMap);

                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}