package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.shm_rz.ufoodapp.Adapter.CommentAdapter;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.Comment;
import com.shm_rz.ufoodapp.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Show_Comment extends AppCompatActivity  implements CommentAdapter.customAdapterInterface {
    String foodID = "";


    int food_ID ;

    ArrayList<Comment> cmList;
    CommentAdapter commentAdapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__comment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }
        foodID = String.valueOf(Common.currentFood.getId());
        food_ID = Common.currentFood.getId();


        cmList = new ArrayList();
        commentAdapter = new CommentAdapter(cmList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://android-application-api.ir/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<List<Comment>> call = service.getCommentList(food_ID);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                for (Comment comment : response.body()) {
                    cmList.add(comment);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onCustomListItemClick(int position) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Show_Comment.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
