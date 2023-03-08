package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.Favorites;
import com.shm_rz.ufoodapp.ViewHolder.FavoritesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity implements FavoritesAdapter.customAdapterInterface{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FavoritesAdapter adapter;

    ArrayList<Favorites> favorites = new ArrayList<>();

    //favorites
    Database localDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }




        recyclerView = (RecyclerView) findViewById(R.id.recycle_fav);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadFavorites();
    }

    private void loadFavorites() {
        favorites = new Database(this).getFavorite();
        adapter = new FavoritesAdapter(this , new Database(this).getFavorite() ,this);
        recyclerView.setAdapter(adapter);
    }

    private void deleteFavorites(int order) {
        //we will remove item at list <order> by position
        favorites.remove(order);
        //after that  , we will delete all old data from sqlite
        new Database(this).cleanFavorites();
        //and final we will update new data from list<order> to sqlite

        for (Favorites item : favorites)
            new Database(this).addToFavorites(
                    String.valueOf(item.getFoodId()) ,
                    item.getFoodName() ,
                    item.getFoodPrice() ,
                    item.getFoodImage() ,
                    item.getFoodDiscount() ,
                    Common.currentUser.getMob_phone()
            );
        loadFavorites();

    }

    @Override
    public void onCustomListItemClick(int position) {
        deleteFavorites(position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavoritesActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
