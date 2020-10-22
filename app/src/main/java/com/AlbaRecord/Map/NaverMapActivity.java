package com.AlbaRecord.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.AlbaRecord.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class NaverMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    double Latitude=0.0;
    double Longtitude=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_map);
        Latitude=getIntent().getDoubleExtra("위도",0.0);
        Longtitude=getIntent().getDoubleExtra("경도",0.0);
        Log.d("위도",String.valueOf(Latitude));
        Log.d("경도",String.valueOf(Longtitude));

        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("990du68u3n"));


        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(Latitude, Longtitude), 4))
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

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
//        locationSource =
//                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
//        Log.d("맵레디", "성공");
//        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        Marker marker = new Marker();


        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(Latitude, Longtitude));
        naverMap.moveCamera(cameraUpdate);
        marker.setPosition(new LatLng(Latitude, Longtitude));
        marker.setWidth(200);
        marker.setHeight(250);
        marker.setIconTintColor(Color.RED);
        marker.setMap(naverMap);


        //Log.d("네이버지도",naverMap.getCameraPosition().toString());
//        naverMap.setCameraPosition(new CameraPosition(,14.0));
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
}