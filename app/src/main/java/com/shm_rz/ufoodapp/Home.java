package com.shm_rz.ufoodapp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import com.github.topbottomsnackbar.TBSnackbar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shm_rz.ufoodapp.Adapter.RestuarantAdapter;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.Credit;
import com.shm_rz.ufoodapp.Model.Food;
import com.shm_rz.ufoodapp.Model.Resturants;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import snow.skittles.BaseSkittle;
import snow.skittles.Skittle;
import snow.skittles.SkittleBuilder;
import snow.skittles.SkittleLayout;

public class Home extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener ,
        RestuarantAdapter.customAdapterInterface,
        SkittleBuilder.OnSkittleClickListener{

    //timer
    private CountDownTimer mCountDownTimer;
    String input = "";

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    Call<String> timerCall;
    boolean timerFlag = true;

    // end timer

    ApiService service;

    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";

    String imageUrl =  "";
    String imageBaseUrlDrawer = "http://android-application-api.ir/Content/UserContent/images/";

    String imageUrlDrawer =  "";
    TBSnackbar snackbar;

    ArrayList<Resturants> resList;
    RestuarantAdapter restuarantAdapter;
    RecyclerView recyclerView;

    TextView txtFullName,txtWalletPrice;
    //slider
    SliderLayout mSlider;
    ImageButton
            imgFastFood  ,
            imgResturant ,
            imgCaffiShop ;


    RoundedImageView imageDrawer;
    Database db;
    int OrderCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new Database(getApplicationContext());
        service = ServiceGenerator.createService(ApiService.class);

        resList = new ArrayList();
        restuarantAdapter = new RestuarantAdapter(resList, this , this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_resturant);

        recyclerView.setLayoutManager(new GridLayoutManager(this , 2));
        recyclerView.setAdapter(restuarantAdapter);

        Call<List<Resturants>> call = service.getRestuarant();
        call.enqueue(new Callback<List<Resturants>>() {
            @Override
            public void onResponse(Call<List<Resturants>> call, Response<List<Resturants>> response) {

                for (Resturants res : response.body()) {
                    if(res.isResEnable()){
                        resList.add(res);
                        restuarantAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Resturants>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });




        ////////////////////////////////////////////////////////////////////////////////


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  ");
        setSupportActionBar(toolbar);


        imgFastFood = (ImageButton) findViewById(R.id.imgFastFood);
        imgResturant = (ImageButton) findViewById(R.id.imgResturant);
        imgCaffiShop = (ImageButton) findViewById(R.id.imgCaffiShop);

        imgFastFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resList.clear();
                restuarantAdapter.notifyDataSetChanged();
                Call<List<Resturants>> call = service.getFastFood();
                call.enqueue(new Callback<List<Resturants>>() {
                    @Override
                    public void onResponse(Call<List<Resturants>> call, Response<List<Resturants>> response) {

                        for (Resturants res : response.body()) {
                            if(res.isResEnable()){
                                resList.add(res);
                                restuarantAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Resturants>> call, Throwable t) {

                        Log.i("Hello",""+t);
                        Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

                    }
                });
                imgCaffiShop.setBackgroundResource(R.color.startblue);
                imgFastFood.setBackgroundResource(R.color.imageButtonSelected);
                imgResturant.setBackgroundResource(R.color.startblue);
            }
        });

        imgResturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resList.clear();
                Call<List<Resturants>> call = service.getRestuarant();
                call.enqueue(new Callback<List<Resturants>>() {
                    @Override
                    public void onResponse(Call<List<Resturants>> call, Response<List<Resturants>> response) {

                        for (Resturants res : response.body()) {
                            if(res.isResEnable()){
                                resList.add(res);
                                restuarantAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Resturants>> call, Throwable t) {

                        Log.i("Hello",""+t);
                        Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

                    }
                });
                imgCaffiShop.setBackgroundResource(R.color.startblue);
                imgFastFood.setBackgroundResource(R.color.startblue);
                imgResturant.setBackgroundResource(R.color.imageButtonSelected);
            }
        });

        imgCaffiShop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resList.clear();
                restuarantAdapter.notifyDataSetChanged();
                Call<List<Resturants>> call = service.getCaffiShop();
                call.enqueue(new Callback<List<Resturants>>() {
                    @Override
                    public void onResponse(Call<List<Resturants>> call, Response<List<Resturants>> response) {

                        for (Resturants res : response.body()) {
                            resList.add(res);
                            restuarantAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Resturants>> call, Throwable t) {

                        Log.i("Hello",""+t);
                        Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

                    }
                });
                imgCaffiShop.setBackgroundResource(R.color.imageButtonSelected);
                imgFastFood.setBackgroundResource(R.color.startblue);
                imgResturant.setBackgroundResource(R.color.startblue);
            }
        });

        Paper.init(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtWalletPrice = (TextView)headerView.findViewById(R.id.txtWalletPrice);
        String s = Common.currentUser.getName() + " " + Common.currentUser.getFamily();
        txtFullName.setText(s);
        imageUrlDrawer = imageBaseUrlDrawer + Common.currentUser.getImage();
        imageDrawer=(RoundedImageView)headerView.findViewById(R.id.imageDrawer);
        Picasso.with(getApplicationContext()).load(imageUrlDrawer)
                .into(imageDrawer);

        Call<Credit> CreditCall = service.ShowCredit(Common.currentUser.getEmail());
        CreditCall.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {

                Credit credit = response.body();
                if(credit.getID() == 0){
                    txtWalletPrice.setText("۰");
                }else {
                    Locale locale = new Locale("en", "US");
                    NumberFormat fmp = NumberFormat.getNumberInstance(locale);
                    txtWalletPrice.setText(PersianDigitConverter.PerisanNumber(fmp.format(credit.getCredit())));
                }
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        txtWalletPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                Common.firstPage.add(Home.this);
                Intent walletIntent = new Intent(Home.this, WalletActivity.class);
                startActivity(walletIntent);
                finish();
            }
        });
        //fab Button========================================================================

        final SkittleBuilder skittleBuilder =
                SkittleBuilder.newInstance((SkittleLayout) findViewById(R.id.skittleLayout), false);

        skittleBuilder.addSkittle(Skittle.newInstance(ContextCompat.getColor(this, R.color.whitebtn),
                ContextCompat.getDrawable(this, R.drawable.ic_dehaze_black_24dp)));

        skittleBuilder.addSkittle(Skittle.newInstance(ContextCompat.getColor(this, R.color.whitebtn),
                ContextCompat.getDrawable(this, R.drawable.ic_funnel)));

        skittleBuilder.addSkittle(Skittle.newInstance(ContextCompat.getColor(this, R.color.whitebtn),
                ContextCompat.getDrawable(this, R.drawable.ic_chat)));
        skittleBuilder.setSkittleClickListener(this);


        //end of fab button====================================================================


        //setup slider
        setupslider();




    }

    private void setupslider() {
        mSlider = (SliderLayout) findViewById(R.id.slider);

        Call<List<Food>> call = service.getBannerImage();
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {

                for (final Food food : response.body()) {
                    imageUrl = imageBaseUrl + food.getFoodImage();
                    final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(food.getName())
                            .image(imageUrl)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    mCountDownTimer.cancel();
                                    Common.firstPage.add(Home.this);
                                    Common.currentFood = food;
                                    Intent intent = new Intent(Home.this, FoodDetail.class);
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    //add extera
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("foodID", String.valueOf(food.getId()));

                    mSlider.addSlider(textSliderView);
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        //create slider

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
                Toast.makeText(Home.this, "خطا", Toast.LENGTH_SHORT).show();
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
                    Common.firstPage.add(Home.this);
                    Intent cartIntent = new Intent(Home.this , Cart.class);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            mCountDownTimer.cancel();
            Paper.book().destroy();
            Intent singin = new Intent(Home.this,SignIn.class);
            singin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(singin);
            finish();
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_home, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent profileIntent = new Intent(Home.this, ProfileCollectionAvtivity.class);
            startActivity(profileIntent);
            finish();
        }else if (id == R.id.nav_cart) {
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
            finish();
        } else if (id == R.id.nav_orders) {
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent newIntent = new Intent(Home.this , TransactionReportActivity.class);
            startActivity(newIntent);
            finish();
        } else if (id == R.id.nav_log_out) {
            mCountDownTimer.cancel();
            Paper.book().destroy();
            Intent singin = new Intent(Home.this,SignIn.class);
            singin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(singin);
            finish();
        }else if (id == R.id.nav_fav) {
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent favIntent = new Intent(Home.this , FavoritesActivity.class);
            startActivity(favIntent);
            finish();
        }else if (id==R.id.navShowAllRestuarants) {
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent showAllRestuarantsIntent = new Intent(Home.this , AllRestuarant.class);
            startActivity(showAllRestuarantsIntent);
            finish();
        }else if (id==R.id.navAbout_me) {
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent AboutmeIntent = new Intent(Home.this , AboutUsActivity.class);
            startActivity(AboutmeIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onCustomListItemClick(int position) {
        Resturants res = resList.get(position);
        Common.resturants = res;
        String ID = res.getID() + "";
        mCountDownTimer.cancel();
        Common.firstPage.add(Home.this);
        Intent intent = new Intent(Home.this , MenuFoodListActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onSkittleClick(BaseSkittle skittle, int position) {
        if(position == 0){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else if(position == 1){
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent filterIntent = new Intent(Home.this , FilterActivity.class);
            startActivity(filterIntent);
            finish();
        }else if(position == 2){
            mCountDownTimer.cancel();
            Common.firstPage.add(Home.this);
            Intent filterIntent = new Intent(Home.this , CrispActivity.class);
            startActivity(filterIntent);
            finish();
        }
    }

    @Override
    public void onMainSkittleClick() {

    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();

    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        timerFlag = false;

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

}
