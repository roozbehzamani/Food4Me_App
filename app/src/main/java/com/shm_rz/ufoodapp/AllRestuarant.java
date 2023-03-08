package com.shm_rz.ufoodapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.Resturants;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;

public class AllRestuarant extends FragmentActivity
        implements OnMapReadyCallback {

    EditText allResSearch;
    ImageButton btnSearch;

    public Bitmap markerIcon;;
    Resturants res;
    ArrayList<Resturants> resturantList;
    private GoogleMap mMap;
    Call<List<Resturants>> call , allResCall;
    ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_restuarant);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        service = ServiceGenerator.createService(ApiService.class);

        resturantList = new ArrayList();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        allResSearch = (EditText) findViewById(R.id.allResSearch);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);

        //SEARCH
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = allResSearch.getText().toString();
                mySearch(s);
            }
        });

        allResSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchField = allResSearch.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(searchField.isEmpty()){
                        Toast.makeText(AllRestuarant.this, "هیچ نامی وارد نشده", Toast.LENGTH_SHORT).show();
                    }else {
                        mySearch(searchField);
                    }
                }
                return false;
            }
        });
    }

    private void mySearch(String s) {
        call = service.resSearch(s);
        call.enqueue(new Callback<List<Resturants>>() {
            @Override
            public void onResponse(Call<List<Resturants>> call, Response<List<Resturants>> response) {

                mMap.clear();
                for (Resturants resturants : response.body()) {
                    resturantList.add(resturants);
                    String[] latlong =  resturants.getResLatLng().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng location = new LatLng(latitude, longitude);
                    markerIcon = BitmapFactory.decodeResource(getResources() , R.drawable.pin);
                    markerIcon = Common.scaleBitmap(markerIcon , 90 , 90);
                    MarkerOptions marker = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))
                            .position(location);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    mMap.addMarker(marker);
                }
            }

            @Override
            public void onFailure(Call<List<Resturants>> call, Throwable t) {
                Toast.makeText(AllRestuarant.this, "خطا", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        setMapStyle();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < resturantList.size(); i++){
                    res = resturantList.get(i);
                    double s = marker.getPosition().latitude ;
                    double s2 = marker.getPosition().longitude ;
                    String[] latlong =  res.getResLatLng().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    if(s == latitude && s2 == longitude){
                        Common.resturants = res;
                        Common.firstPage.add(AllRestuarant.this);
                        Intent intent = new Intent(AllRestuarant.this , MenuFoodListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                return true;
            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng sydney = new LatLng(37.537154, 45.072800);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("اردبیل"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        allResCall = service.getAllRestuarant();
        allResCall.enqueue(new Callback<List<Resturants>>() {
            @Override
            public void onResponse(Call<List<Resturants>> call, Response<List<Resturants>> response) {

                for (Resturants resturants : response.body()) {
                    resturantList.add(resturants);
                    String[] latlong =  resturants.getResLatLng().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng location = new LatLng(latitude, longitude);
                    markerIcon = BitmapFactory.decodeResource(getResources() , R.drawable.pin);
                    markerIcon = Common.scaleBitmap(markerIcon , 70 , 70);
                    MarkerOptions marker = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))
                            .position(location);

                    mMap.addMarker(marker);
                }
            }

            @Override
            public void onFailure(Call<List<Resturants>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void setMapStyle() {
        MapStyleOptions style = new MapStyleOptions("["+
                "{"+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#ebe3cd\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#523735\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"elementType\": \"labels.text.stroke\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#f5f1e6\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"administrative\","+
                "\"elementType\": \"geometry.stroke\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#c9b2a6\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"administrative.land_parcel\","+
                "\"elementType\": \"geometry.stroke\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#dcd2be\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"administrative.land_parcel\","+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#ae9e90\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"landscape.natural\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#dfd2ae\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"poi\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#dfd2ae\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"poi\","+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#93817c\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"poi.park\","+
                "\"elementType\": \"geometry.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#a5b076\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"poi.park\","+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#447530\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#f5f1e6\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road.arterial\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#fdfcf8\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road.highway\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#f8c967\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road.highway\","+
                "\"elementType\": \"geometry.stroke\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#e9bc62\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road.highway.controlled_access\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#e98d58\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road.highway.controlled_access\","+
                "\"elementType\": \"geometry.stroke\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#db8555\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"road.local\","+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#806b63\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"transit.line\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#dfd2ae\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"transit.line\","+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#8f7d77\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"transit.line\","+
                "\"elementType\": \"labels.text.stroke\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#ebe3cd\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"transit.station\","+
                "\"elementType\": \"geometry\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#dfd2ae\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"water\","+
                "\"elementType\": \"geometry.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#b9d3c2\""+
                "}"+
                "]"+
                "},"+
                "{"+
                "\"featureType\": \"water\","+
                "\"elementType\": \"labels.text.fill\","+
                "\"stylers\": ["+
                "{"+
                "\"color\": \"#92998d\""+
                "}"+
                "]"+
                "}"+
                "]");

        mMap.setMapStyle(style);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AllRestuarant.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }

}