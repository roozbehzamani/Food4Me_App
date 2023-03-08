package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingPageActivity extends AppCompatActivity {

    ApiService service;
    Call<Integer> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // starting app in full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_page);

        service = ServiceGenerator.createService(ApiService.class);

        Starter();
    }

    private void Starter(){
        call = service.Loading();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Intent intent = new Intent(LoadingPageActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Starter();
            }
        });
    }
}
