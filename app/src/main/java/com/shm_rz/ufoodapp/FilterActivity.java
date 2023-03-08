package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.topbottomsnackbar.TBSnackbar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shm_rz.ufoodapp.Adapter.FoodFilterAdapter;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.FoodFilter;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.katso.livebutton.LiveButton;

public class FilterActivity extends AppCompatActivity implements FoodFilterAdapter.customAdapterInterface{


    //timer
    private CountDownTimer mCountDownTimer;
    String input = "";

    private boolean mTimerRunning;
    TBSnackbar snackbar;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    Call<String> timerCall;
    boolean timerFlag = true;
    // end timer

    int sortType = 0;
    Button btnSordPrice;
    private boolean ascending = true;


    Button btnFilter;
    ArrayList<FoodFilter> foodFilterList;
    FoodFilterAdapter foodFilterAdapter ;
    RecyclerView recyclerView;
    ApiService service;

    Call<List<FoodFilter>> callResList , callCaffiShopList ,callFastFoodList;

    Database db;
    int OrderCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        db = new Database(getApplicationContext());
        service = ServiceGenerator.createService(ApiService.class);

        btnSordPrice = (Button) findViewById(R.id.btnSordPrice);

        btnSordPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortData(ascending);
                ascending = !ascending;



            }
        });





        btnFilter = (Button) findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog();
            }
        });

        foodFilterList = new ArrayList();
        foodFilterAdapter = new FoodFilterAdapter(foodFilterList, this , this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerFoodFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodFilterAdapter);

    }

    private void sortData(boolean asc) {
        if (asc) {
            Collections.sort(foodFilterList , FoodFilter.BY_COST);
        } else
        {
            Collections.sort(foodFilterList , FoodFilter.BY_COST_SECEND);
        }

        foodFilterAdapter = new FoodFilterAdapter(foodFilterList, this , this);
        recyclerView.setAdapter(foodFilterAdapter);
    }

    @Override
    public void onCustomListItemClick(int position) {

    }

    private void myDialog(){
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.filter_layout))
                .setGravity(Gravity.BOTTOM)
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .setMargin(0,0,0,0)
                .setPadding(0,0,0,0)
                .setOutMostMargin(0,0,0,0)
                .create();
        dialog.show();

        View view = dialog.getHolderView();

        final CheckBox ckbCaffiShop = (CheckBox) view.findViewById(R.id.ckbCaffiShop);
        final CheckBox ckbFastFood = (CheckBox) view.findViewById(R.id.ckbFastFood);
        final CheckBox ckbRes = (CheckBox) view.findViewById(R.id.ckbRes);
        final Button btnAction = (Button) view.findViewById(R.id.btnAction);
        final Button btnClose = (Button) view.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckbCaffiShop.isChecked() && ckbFastFood.isChecked() && ckbRes.isChecked()){
                    foodFilterList.clear();
                    resList();
                    foodFilterList.clear();
                    caffiShopList();
                    foodFilterList.clear();
                    fastFoodList();
                }else if(!ckbCaffiShop.isChecked() && !ckbFastFood.isChecked() && ckbRes.isChecked()){
                    foodFilterList.clear();
                    resList();
                }else if(!ckbCaffiShop.isChecked() && ckbFastFood.isChecked() && !ckbRes.isChecked()){
                    foodFilterList.clear();
                    fastFoodList();
                }else if(ckbCaffiShop.isChecked() && !ckbFastFood.isChecked() && !ckbRes.isChecked()){
                    foodFilterList.clear();
                    caffiShopList();
                }else if(!ckbCaffiShop.isChecked() && ckbFastFood.isChecked() && ckbRes.isChecked()){
                    foodFilterList.clear();
                    resList();
                    foodFilterList.clear();
                    fastFoodList();
                }else if(ckbCaffiShop.isChecked() && !ckbFastFood.isChecked() && ckbRes.isChecked()){
                    foodFilterList.clear();
                    caffiShopList();
                    foodFilterList.clear();
                    resList();
                }else if(ckbCaffiShop.isChecked() && ckbFastFood.isChecked() && !ckbRes.isChecked()){
                    foodFilterList.clear();
                    caffiShopList();
                    foodFilterList.clear();
                    fastFoodList();
                }else{
                    foodFilterList.clear();
                    foodFilterAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });


    }
    private void resList(){
        callResList = service.getResFood();
        callResList.enqueue(new Callback<List<FoodFilter>>() {
            @Override
            public void onResponse(Call<List<FoodFilter>> call, Response<List<FoodFilter>> response) {

                for (FoodFilter foodFilter : response.body()) {
                    foodFilterList.add(foodFilter);
                    foodFilterAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<FoodFilter>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

    }
    private void caffiShopList(){
        callCaffiShopList = service.getCaffiShopFood();
        callCaffiShopList.enqueue(new Callback<List<FoodFilter>>() {
            @Override
            public void onResponse(Call<List<FoodFilter>> call, Response<List<FoodFilter>> response) {

                for (FoodFilter foodFilter : response.body()) {
                    foodFilterList.add(foodFilter);
                    foodFilterAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<FoodFilter>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
    }
    private void fastFoodList(){
        callFastFoodList = service.getFastFoodFood();
        callFastFoodList.enqueue(new Callback<List<FoodFilter>>() {
            @Override
            public void onResponse(Call<List<FoodFilter>> call, Response<List<FoodFilter>> response) {

                for (FoodFilter foodFilter : response.body()) {
                    foodFilterList.add(foodFilter);
                    foodFilterAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<FoodFilter>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
    }
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();

    }

    private void startTimer() {
        timerFlag = false;
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

            }
        }.start();

        mTimerRunning = true;

    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();

    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        if(OrderCount > 0){
            if(timeLeftFormatted.equals("00:00:00") || timeLeftFormatted.equals("00:00") || timeLeftFormatted.equals("۰۰:۰۰") || timeLeftFormatted.equals("۰۰:۰۰:۰۰")){
                snackbar.setText("سبد خرید(تعداد : " + PersianDigitConverter.PerisanNumber(String.valueOf(OrderCount)) + " , مهلت سفارش یارانه ای به پایان رسیده" + ")");
            }else {
                snackbar.setText("سبد خرید(تعداد : " + PersianDigitConverter.PerisanNumber(String.valueOf(OrderCount)) + " , مهلت سفارش یارانه ای :" + timeLeftFormatted + ")");
            }
        }else {
            if(timeLeftFormatted.equals("00:00:00") || timeLeftFormatted.equals("00:00") || timeLeftFormatted.equals("۰۰:۰۰") || timeLeftFormatted.equals("۰۰:۰۰:۰۰")){
                snackbar.setText("مهلت سفارش یارانه ای به پایان رسیده است");
            }else {
                snackbar.setText("زمان باقیمانده برای سفارش یارانه ای :" + timeLeftFormatted);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timerFlag){
            MySnakBar();
        }else {
            mCountDownTimer.cancel();
            MySnakBar();
        }
    }

    private void MySnakBar(){

        timerCall = service.TimeToEndUniversityCredit();
        timerCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                input = response.body();
                long millisInput = Long.parseLong(input) * 60000;

                setTime(millisInput);


                startTimer();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(FilterActivity.this, "خطا", Toast.LENGTH_SHORT).show();

            }
        });

        OrderCount = (db.getCountCart());

        if(OrderCount > 0){
            snackbar = TBSnackbar
                    .make(findViewById(
                            android.R.id.content),
                            "",
                            TBSnackbar.LENGTH_INDEFINITE,
                            TBSnackbar.STYLE_SHOW_BOTTOM
                    );

            snackbar.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCountDownTimer.cancel();
                    Common.firstPage.add(FilterActivity.this);
                    Intent cartIntent = new Intent(FilterActivity.this , Cart.class);
                    startActivity(cartIntent);
                    finish();
                }
            });
            snackbar.setIconRight(R.drawable.basket , 24);
            snackbar.getView().setBackgroundResource(R.color.SnackbarColor);
            snackbar.getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            snackbar.show();
        }else {
            snackbar = TBSnackbar
                    .make(findViewById(
                            android.R.id.content),
                            "",
                            TBSnackbar.LENGTH_INDEFINITE,
                            TBSnackbar.STYLE_SHOW_BOTTOM
                    );
            snackbar.getView().setBackgroundResource(R.color.SnackbarColor);
            snackbar.getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            snackbar.show();

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FilterActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
