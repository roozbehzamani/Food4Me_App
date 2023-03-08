package com.shm_rz.ufoodapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.github.topbottomsnackbar.TBSnackbar;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.Comment;
import com.shm_rz.ufoodapp.Model.Food;
import com.shm_rz.ufoodapp.Model.Order;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.objenesis.instantiator.perc.PercInstantiator;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {
    TextView food_name,food_price,food_description,backing_time , txtFoodCount;
    CardView cvOrderType;
    ImageView food_image;
    String recipe;
    int FoodTotalCount;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnRating;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;
    Button btnRecipe ;

    //timer
    private CountDownTimer mCountDownTimer;
    String input = "";
    ApiService service;
    private boolean mTimerRunning;
    TBSnackbar snackbar;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    Call<String> timerCall;
    boolean timerFlag = true;
    // end timer

    int OrderCount;

    String foodImage = "";
    String foodID="";
    int food_ID;

    Database db;
    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";

    String imageUrl =  "";

    Button btnShowComment;

    String s= "";
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
    Date today = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        Locale locale = new Locale("en", "US");
        final NumberFormat fmp = NumberFormat.getNumberInstance(locale);

        db = new Database(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        foodID = String.valueOf(Common.currentFood.getId());

        food_ID = Common.currentFood.getId();

        service = ServiceGenerator.createService(ApiService.class);
        btnShowComment=(Button)findViewById(R.id.btnShowComment);

        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                Common.firstPage.add(FoodDetail.this);
                Intent intent=new Intent(FoodDetail.this,Show_Comment.class);
                startActivity(intent);
                finish();
            }
        });


        //init view
        numberButton= (ElegantNumberButton) findViewById(R.id.number_button);
        btnRating = (FloatingActionButton) findViewById(R.id.btn_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnRecipe = (Button) findViewById(R.id.btnRecipe);

        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, final int oldValue, final int newValue) {
                Call<Integer> timeToEndOrder = service.TimeToOrder();
                timeToEndOrder.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int resu = response.body();
                        if(resu == 1){
                            int singleFoodCount = FoodTotalCount;
                            dateFormatter.setLenient(false);
                            today = Calendar.getInstance().getTime();
                            s = dateFormatter.format(today);
                            Food food = Common.currentFood;
                            if(newValue == 1 && oldValue == 0){
                                db.addToCart(
                                        foodID,
                                        food.getName(),
                                        "1",
                                        food.getCost(),
                                        foodImage,
                                        s,
                                        Common.resturants.getResName(),
                                        String.valueOf(Common.resturants.getID())
                                );
                                MySnakBar();
                            }else if(newValue == 0 && oldValue == 1){
                                Order myyyyOrder = db.getSinglecart(String.valueOf(food.getId()));
                                db.DeleteOrder(myyyyOrder.getID());
                            } else {
                                if(newValue > singleFoodCount && singleFoodCount != -1){
                                    //newValue = oldValue;
                                    numberButton.setNumber(String.valueOf(singleFoodCount));
                                    Toast.makeText(FoodDetail.this, "تعداد درخواستی بیشتر از تعداد موجودی میباشد", Toast.LENGTH_SHORT).show();
                                }else {
                                    db.updateCartFoodID(String.valueOf(newValue) , foodID);
                                }
                            }
                        }else {
                            numberButton.setNumber(String.valueOf(oldValue));
                            Toast.makeText(FoodDetail.this, "رستوران بسته شده", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(FoodDetail.this, "خطا", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        food_description=(TextView)findViewById(R.id.food_descrption);
        txtFoodCount = (TextView)findViewById(R.id.txtFoodCount);
        cvOrderType = (CardView) findViewById(R.id.cvOrderType) ;
        food_name=(TextView)findViewById(R.id.food_name);
        backing_time=(TextView)findViewById(R.id.backing_time);
        food_price=(TextView)findViewById(R.id.food_price);
        food_image=(ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);
        //get food id from intent

        btnRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe();
            }
        });

        Call<List<Food>> call = service.getFoodDeatail(food_ID);
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {

                for (Food food : response.body()) {
                    food_description.setText(PersianDigitConverter.PerisanNumber(food.getCreateMaterial()));
                    food_name.setText(food.getName());
                    backing_time.setText(PersianDigitConverter.PerisanNumber(food.getBakingTime()));
                    food_price.setText(PersianDigitConverter.PerisanNumber(fmp.format(Integer.parseInt(food.getCost()))));
                    imageUrl = imageBaseUrl + food.getFoodImage();
                    foodImage = food.getFoodImage();
                    FoodTotalCount = food.getFoodCount();
                    if(food.getFoodCount() == -1)
                        txtFoodCount.setText("نامحدود");
                    else
                        txtFoodCount.setText(PersianDigitConverter.PerisanNumber(String.valueOf(food.getFoodCount())));

                    if(food.isOrderType())
                        cvOrderType.setVisibility(View.VISIBLE);
                    else
                        cvOrderType.setVisibility(View.GONE);

                    if(food.getFoodCount() == 0){
                        numberButton.setEnabled(false);
                        numberButton.setClickable(false);
                    }else {
                        numberButton.setVisibility(View.VISIBLE);
                    }
                    if(db.FoodIsExist(String.valueOf(food.getId()))){
                        Order myyyyOrder = db.getSinglecart(String.valueOf(food.getId()));
                        numberButton.setNumber(myyyyOrder.getFoodCount());
                    }
                    Picasso.with(FoodDetail.this).load(imageUrl)
                            .into(food_image);
                    if(food.getRecipe().equals("ندارد")){
                        btnRecipe.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btnShowComment.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                        btnShowComment.setLayoutParams(layoutParams);
                    }else {
                        recipe = food.getRecipe();
                        btnRecipe.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btnShowComment.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        btnShowComment.setLayoutParams(layoutParams);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        RatingBar();


    }
    public void RatingBar() {
        Call<Float> callRating = service.getFoodRating(food_ID);
        callRating.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if(response.body() != null){
                    ratingBar.setRating(response.body());
                }else {
                    ratingBar.setRating(1);
                }

            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("ارسال")
                .setNegativeButtonText("انصراف")
                .setNoteDescriptions(Arrays.asList("خیلی بد" , "خوب نیست" , "کاملا موافقم" , "خیلی خوب" , "عالی است"))
                .setDefaultRating(1)
                .setTitle("این غذا را ارزیابی کنید")
                .setDescription("لطفا به غذا امتیاز داده و نظر خود را بنویسید")
                .setTitleTextColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.colorPrimary)
                .setHint("لطفا نظر خود را اینجا بنویسید ...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {

        int Food_ID = Integer.parseInt(foodID);
        Call<Comment> call = service.insertComment(Food_ID , value , Common.currentUser.getMob_phone() , comments);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Toast.makeText(FoodDetail.this, "نظر با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {


                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
        Toast.makeText(FoodDetail.this, "نظر با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
        RatingBar();
    }

    @Override
    public void onNeutralButtonClicked() {

    }

    private void recipe(){
        AlertDialog.Builder alertdailog = new AlertDialog.Builder(FoodDetail.this);
        alertdailog.setTitle("طرز پخت");

        final LayoutInflater inflater = this.getLayoutInflater();
        final View order_address_comment = inflater.inflate(R.layout.recipe_layout , null);

        alertdailog.setView(order_address_comment);


        final TextView txtRecipe = (TextView)  order_address_comment.findViewById(R.id.txtRecipe);

        txtRecipe.setText(PersianDigitConverter.PerisanNumber(recipe));

        alertdailog.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdailog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_home , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.FoodHome:{
                mCountDownTimer.cancel();
                Common.firstPage.add(FoodDetail.this);
                Intent homeIntent = new Intent(FoodDetail.this , Home.class);
                startActivity(homeIntent);
                finish();
            }
            case R.id.FoodMenu:{
                mCountDownTimer.cancel();
                Common.firstPage.add(FoodDetail.this);
                Intent homeIntent = new Intent(FoodDetail.this , MenuFoodListActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(FoodDetail.this, "خطا", Toast.LENGTH_SHORT).show();
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
                    Common.firstPage.add(FoodDetail.this);
                    Intent cartIntent = new Intent(FoodDetail.this , Cart.class);
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
        Intent intent = new Intent(FoodDetail.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
