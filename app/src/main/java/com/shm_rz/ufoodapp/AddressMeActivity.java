package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.shm_rz.ufoodapp.Adapter.AddressMeAdapter;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.Address;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressMeActivity extends AppCompatActivity implements AddressMeAdapter.customAdapterInterface{

    Button btn_Address_me;

    ArrayList<Address> addressesList;
    AddressMeAdapter addressMeAdapter ;
    RecyclerView recyclerView;

    ApiService service;
    public Call<List<Address>> call;
    public Call<String> DeleteCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_me);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        service = ServiceGenerator.createService(ApiService.class);

        addressesList = new ArrayList();
        addressMeAdapter = new AddressMeAdapter(addressesList, this , this);
        recyclerView = (RecyclerView) findViewById(R.id.listAdrress_Me);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(addressMeAdapter);




        btn_Address_me = (Button) findViewById(R.id.btn_Address_me);
        btn_Address_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.firstPage.add(AddressMeActivity.this);
                Intent intent = new Intent(AddressMeActivity.this , MapsAddressMEActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showAllAddress() {
        call = service.getAddressMe(Common.currentUser.getMob_phone() , Common.currentUser.getEmail());
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {

                for (Address address : response.body()) {
                    addressesList.add(address);
                    addressMeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onCustomListItemClick(int position) {
        Address address = addressesList.get(position);
        if(Common.comeFromCart.equals("yes")){
            Common.selectedAddress = address;
            Common.comeFromCart = "no";
            Intent intent = new Intent(AddressMeActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
            Common.firstPage.remove(Common.firstPage.size() - 1);
            Common.fromAddressMe = "yes";
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            Address myAddress = addressesList.get(Common.currentAddressAdapterPossition);
            Common.firstPage.add(AddressMeActivity.this);
            Intent intent = new Intent(AddressMeActivity.this , MapsAddressMEActivity.class);
            intent.putExtra("Address", myAddress.getAddress());
            intent.putExtra("Type", myAddress.getAddressType());
            intent.putExtra("latLong", myAddress.getAddressLatLng());
            intent.putExtra("apartmentName", myAddress.getApartmentName());
            intent.putExtra("floor", myAddress.getFloor());
            intent.putExtra("unit", myAddress.getUnit());
            intent.putExtra("ID", String.valueOf(myAddress.getUserAddressID()));
            startActivity(intent);
            finish();
        }else if(item.getTitle().equals(Common.DELETE))
        {
            Address myAddress = addressesList.get(Common.currentAddressAdapterPossition);
            DeleteCall = service.DeleteAddress(myAddress.getUserAddressID());
            DeleteCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String result = response.body();


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
            addressesList.clear();
            showAllAddress();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressesList.clear();
        addressMeAdapter.notifyDataSetChanged();
        showAllAddress();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddressMeActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
