package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.topbottomsnackbar.TBSnackbar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.Food;
import com.shm_rz.ufoodapp.Model.Menu;
import com.shm_rz.ufoodapp.Model.Order;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MenuFoodListActivity extends AppCompatActivity {

    //timer
    private CountDownTimer mCountDownTimer;
    String input = "";

    private boolean mTimerRunning;
    TBSnackbar snackbar;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    Call<String> timerCall;
    ApiService service;
    int OrderCount;
    boolean timerFlag = true;
    // end timer

    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";
    String imageUrl = "";

    String count = "";
    int quantity;
    Database db , localDB;
    String s= "";
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
    Date today = new Date();

    String imageBaseUrl1 = "http://android-application-api.ir/Content/UserContent/images/";
    String imageUrl1 = "";
    String imageUrl2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_food_list);

        db = new Database(getApplicationContext());
        service = ServiceGenerator.createService(ApiService.class);

        final ExpandableLayout layout = (ExpandableLayout) findViewById(R.id.menu_ExpandableLayout);


        layout.setRenderer(new ExpandableLayout.Renderer<Menu, Food>() {
            @Override
            public void renderParent(View view, Menu phoneCategory, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.menuFoodList_name)).setText(phoneCategory.getMenuTitle());
                ImageView menuFoodList_image = (ImageView) view.findViewById(R.id.menuFoodList_image);
                imageUrl = imageBaseUrl + phoneCategory.getMenuImage();
                Picasso
                        .with(MenuFoodListActivity.this)
                        .load(imageUrl)
                        .into(menuFoodList_image);



            }

            @Override
            public void renderChild(final View view, final Food phone, int parentPosition, int childPosition) {
                final TextView txtCount = (TextView) view.findViewById(R.id.txtCount);
                TextView txtFoodCount = (TextView) view.findViewById(R.id.txtFoodCount);
                final TextView txtFoodName = (TextView) view.findViewById(R.id.food_name);
                TextView txtSendTomorrow = (TextView) view.findViewById(R.id.txtSendTomorrow);
                final TextView txtFoodPrice = (TextView) view.findViewById(R.id.food_price);
                RoundedImageView food_image = (RoundedImageView) view.findViewById(R.id.food_image);
                ImageView btn_share = (ImageView) view.findViewById(R.id.btn_share);
                final ImageView fav = (ImageView) view.findViewById(R.id.fav);
                Button btn_quick_cart = (Button) view.findViewById(R.id.btn_quick_cart);
                Button btnAddCount = (Button) view.findViewById(R.id.btnAddCount);
                RelativeLayout foodCardView = (RelativeLayout) view.findViewById(R.id.foodCardView);

                foodCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.currentFood = phone;
                        mCountDownTimer.cancel();
                        Common.firstPage.add(MenuFoodListActivity.this);
                        Intent intent = new Intent(MenuFoodListActivity.this , FoodDetail.class);
                        startActivity(intent);
                        finish();
                    }
                });

                txtFoodName.setText(phone.getName());
                Locale locale = new Locale("en", "US");
                NumberFormat fmp = NumberFormat.getNumberInstance(locale);
                int myFoodPricer = Integer.parseInt(phone.getCost());
                txtFoodPrice.setText(PersianDigitConverter.PerisanNumber(fmp.format(myFoodPricer)));

                if(phone.getFoodCount() == -1)
                    txtFoodCount.setText("نامحدود");
                else
                    txtFoodCount.setText(PersianDigitConverter.PerisanNumber(String.valueOf(phone.getFoodCount())));

                if(phone.getFoodCount() == 0){
                    btn_quick_cart.setText("ناموجود");
                    btn_quick_cart.setEnabled(false);
                    btn_quick_cart.setClickable(false);
                }else {
                    btn_quick_cart.setText("سفارش");
                    btn_quick_cart.setEnabled(true);
                    btn_quick_cart.setClickable(true);
                }


                if(phone.isOrderType())
                    txtSendTomorrow.setVisibility(View.VISIBLE);
                else
                    txtSendTomorrow.setVisibility(View.INVISIBLE);

                Button btnMinesCart = (Button) view.findViewById(R.id.btnMinesCart);

                btnAddCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = txtCount.getText().toString();
                        quantity = Integer.parseInt(count);
                        if(quantity < phone.getFoodCount() || phone.getFoodCount() == -1)
                            quantity++;
                        else
                            Toast.makeText(MenuFoodListActivity.this, "تعداد درخواستی بیشتر از تعداد موجودی میباشد", Toast.LENGTH_LONG).show();
                        txtCount.setText(PersianDigitConverter.PerisanNumber(String.valueOf(quantity)));
                    }
                });

                btnMinesCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = txtCount.getText().toString();
                        quantity = Integer.parseInt(count);
                        if(quantity > 1)
                            quantity--;
                        else
                            Toast.makeText(MenuFoodListActivity.this, "حداقل تعداد", Toast.LENGTH_SHORT).show();
                        txtCount.setText(PersianDigitConverter.PerisanNumber(String.valueOf(quantity)));
                    }
                });

                btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        shareItem(phone.getFoodImage() , phone);
                    }
                });

                localDB = new Database(getApplicationContext());
                if(localDB.isFavorites(String.valueOf(phone.getId()))){
                    fav.setImageResource(R.drawable.ic_favorite_white);
                }else {
                    fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localDB = new Database(getApplicationContext());
                        if(!localDB.isFavorites(String.valueOf(phone.getId()))){
                            localDB.addToFavorites(
                                    String.valueOf(phone.getId()) ,
                                    txtFoodName.getText().toString(),
                                    txtFoodPrice.getText().toString() ,
                                    imageBaseUrl + phone.getFoodImage() ,
                                    "sddssd",
                                    Common.currentUser.getMob_phone()
                            );
                            fav.setImageResource(R.drawable.ic_favorite_white);
                            Toast.makeText(getApplicationContext(), "عذا با موفقیت به لیست مورد علاقه ها افزوده شد", Toast.LENGTH_SHORT).show();
                        }else {
                            localDB.removeFavorites(String.valueOf(phone.getId()));
                            fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(getApplicationContext(), "غذا از لیست مورد علاقه ها حذف شد", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Call<Integer> timeToEndOrder = service.TimeToOrder();
                        timeToEndOrder.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                int resu = response.body();
                                if(resu == 1){
                                    Food foood = phone;
                                    count = txtCount.getText().toString();
                                    dateFormatter.setLenient(false);
                                    today = Calendar.getInstance().getTime();
                                    s = dateFormatter.format(today);
                                    if(db.FoodIsExist(String.valueOf(foood.getId()))){
                                        Order myyyyOrder = db.getSinglecart(String.valueOf(foood.getId()));
                                        count = String.valueOf(
                                                Integer.parseInt(count) + Integer.parseInt(myyyyOrder.getFoodCount())
                                        );
                                        if(foood.getFoodCount() >= Integer.parseInt(count) || foood.getFoodCount() == -1)
                                            db.updateCartFoodID(count , String.valueOf(foood.getId()));
                                        else
                                            Toast.makeText(MenuFoodListActivity.this, "تعداد درخواستی در سبد خرید بیشتر از تعداد موجودی میباشد", Toast.LENGTH_SHORT).show();
                                    }else {
                                        db.addToCart(
                                                String.valueOf(foood.getId()),
                                                foood.getName(),
                                                count,
                                                foood.getCost(),
                                                foood.getFoodImage(),
                                                s,
                                                Common.resturants.getResName(),
                                                String.valueOf(Common.resturants.getID())
                                        );

                                        MySnakBar();
                                    }
                                }else {
                                    Toast.makeText(MenuFoodListActivity.this, "رستوران بسته شده", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(MenuFoodListActivity.this, "خطا", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                imageUrl1 = imageBaseUrl1 + phone.getFoodImage();
                Picasso
                        .with(MenuFoodListActivity.this)
                        .load(imageUrl1)
                        .into(food_image);

            }
        });


        Call<List<Menu>> call = service.getResMenu();
        call.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {

                for (Menu menu : response.body()) {
                    layout.addSection(getSection(
                            menu.getMenuTitle(),
                            menu.getMenuImage(),
                            menu.getMenuID()
                    ));
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {

                Log.i("Hello", "" + t);
                Toast.makeText(MenuFoodListActivity.this, "Throwable" + t, Toast.LENGTH_LONG).show();

            }
        });
    }


    private Section<Menu, Food> getSection(String title, String image , int menuID) {

        final Section<Menu, Food> section = new Section<>();

        Menu phoneCategory = new Menu(
                title,
                image
        );

        final List<Food> listPhone = new ArrayList<>();

        Call<List<Food>> callItems = service.getFoodList(Common.resturants.getID() , menuID);
        callItems.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {

                for (Food food : response.body()) {
                    listPhone.add(food);
                }
                section.children.addAll(listPhone);
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(MenuFoodListActivity.this, "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        section.parent = phoneCategory;


        return section;
    }

    public void shareItem(String url, final Food f) {
        url = imageBaseUrl + url;
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.putExtra(Intent.EXTRA_TEXT,f.getName() + " - " + String.valueOf(f.getCost()) + " - " + "لینک دانلود نرم افزار :" + "http://android-application-api.ir/Home/App");
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            }
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
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
                Toast.makeText(MenuFoodListActivity.this, "خطا", Toast.LENGTH_SHORT).show();
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
                    Common.firstPage.add(MenuFoodListActivity.this);
                    Intent cartIntent = new Intent(MenuFoodListActivity.this , Cart.class);
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
        Intent intent = new Intent(MenuFoodListActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
