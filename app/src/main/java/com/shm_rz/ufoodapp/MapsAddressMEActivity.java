package com.shm_rz.ufoodapp;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Service.ApiService;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.katso.livebutton.LiveButton;

public class MapsAddressMEActivity extends FragmentActivity implements OnMapReadyCallback {

    String myLatitute = "";
    String myLongitude = "";
    String myLatLng = "";
    String Address = "";
    String Type = "";
    String latLong = "";
    String apartmentName = "";
    String floor = "";
    String unit = "";
    String edtType = "";
    String edtApartmentName = "";
    String edtUserFloor = "";
    String edtUserUnit = "";
    String ID = "";
    int myID;
    String edtAddress = "";
    Button btn_Address_me;

    EditText edtAddressType;
    EditText edtCompleteAddress;



    int flag = 0;

    private GoogleMap mMap;
    ApiService service;
    Call<String> call , callEdit;
    int empty = 0;

    View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_address_me);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        service = ServiceGenerator.createService(ApiService.class);

        btn_Address_me = (Button) findViewById(R.id.btn_Address_me);

        if(getIntent() !=null)
        {
            Address = getIntent().getStringExtra("Address");
            Type = getIntent().getStringExtra("Type");
            latLong = getIntent().getStringExtra("latLong");
            apartmentName = getIntent().getStringExtra("apartmentName");
            floor = getIntent().getStringExtra("floor");
            unit = getIntent().getStringExtra("unit");
            ID = getIntent().getStringExtra("ID");
        }
        if(ID != null)
            myID = Integer.parseInt(ID);
        if(ID != null && Address != null && Type != null && latLong != null )
            empty = 1;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();

        mapFragment.getMapAsync(this);


        btn_Address_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myLatLng.equals(""))
                    myDialog();
                else
                    Toast.makeText(MapsAddressMEActivity.this, "لطفا لوکیشن خود را وارد کنید", Toast.LENGTH_SHORT).show();
            }
        });

        if(flag == 1)
            Toast.makeText(getApplicationContext(), "آدرس با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
        else if(flag == 2)
            Toast.makeText(getApplicationContext(), "خطایی رخ داده . لطفا کمی بغد امتحان نمایید", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMapStyle();
        LatLng urmia = new LatLng(37.537154, 45.072800);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(urmia));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        if(empty == 1){
            mMap.clear();
            String[] latlong =  latLong.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            myLatLng = latLong;
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }



        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick (LatLng latLng){
                if(latLng.longitude > 44.983101 && latLng.longitude < 45.12455 && latLng.latitude <37.600506 && latLng.latitude > 37.497246){
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    myLatitute = latLng.latitude + "";
                    myLongitude = latLng.longitude + "";
                    myLatLng = myLatitute + "," + myLongitude;
                }else {
                    Toast.makeText(MapsAddressMEActivity.this, "ادرس انتخابی شما خارج محدوده میباشد", Toast.LENGTH_SHORT).show();
                }

            }




        });



    }

    private void myDialog(){
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.address_layout))
                .setGravity(Gravity.CENTER)
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();

        View view = dialog.getHolderView();

        final EditText edtHomeName = (EditText) view.findViewById(R.id.edtHomeName);
        final EditText edtFloor = (EditText) view.findViewById(R.id.edtFloor);
        final EditText edtUnit = (EditText) view.findViewById(R.id.edtUnit);
        edtCompleteAddress = (EditText) view.findViewById(R.id.edtCompleteAddress);
        edtAddressType = (EditText) view.findViewById(R.id.edtAddressType);
        final LiveButton btnSendInfo = (LiveButton) view.findViewById(R.id.btnSendInfo);

        if(empty == 1){
            edtCompleteAddress.setText(Address);
            edtAddressType.setText(Type);
            edtFloor.setText(floor);
            edtHomeName.setText(apartmentName);
            edtUnit.setText(unit);
        }

        btnSendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtAddressType.getText().toString().equals(""))
                {
                    if (!edtCompleteAddress.getText().toString().equals("") )
                    {
                        edtAddress = edtCompleteAddress.getText().toString();
                        edtType = edtAddressType.getText().toString();
                        edtUserFloor = edtFloor.getText().toString();
                        edtUserUnit = edtUnit.getText().toString();
                        edtApartmentName = edtHomeName.getText().toString();
                        dialog.dismiss();
                        if(empty == 0){
                            call = service.insertUserAddress(
                                    Common.currentUser.getEmail(),
                                    edtAddress,
                                    edtType,
                                    myLatLng ,
                                    edtApartmentName ,
                                    edtUserFloor ,
                                    edtUserUnit
                            );
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if(result.equals("Success")){
                                        Intent intent = new Intent(MapsAddressMEActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
                                        Common.firstPage.remove(Common.firstPage.size() - 1);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                        Toast.makeText(MapsAddressMEActivity.this, "آدرس جدید با خطا مواجه شد", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(MapsAddressMEActivity.this, "خطا", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else {
                            if(myLatLng.isEmpty())
                                myLatLng = latLong;
                            callEdit = service.editUserAddress(
                                    myID,
                                    edtAddress,
                                    edtType,
                                    myLatLng ,
                                    edtApartmentName ,
                                    edtUserFloor ,
                                    edtUserUnit
                            );
                            callEdit.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if(result.equals("Success"))
                                    {
                                        Intent intent = new Intent(MapsAddressMEActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
                                        Common.firstPage.remove(Common.firstPage.size() - 1);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                        Toast.makeText(MapsAddressMEActivity.this, "ویرایش با خطا مواجه شد", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(MapsAddressMEActivity.this, "خطا", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }else {
                        Toast.makeText(MapsAddressMEActivity.this, "آدرس کامل خود را وارد نکردید", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MapsAddressMEActivity.this, "نوع آدرس را وارد نکردید", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
        Intent intent = new Intent(MapsAddressMEActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}