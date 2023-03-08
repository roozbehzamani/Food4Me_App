package com.shm_rz.ufoodapp;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shm_rz.ufoodapp.Adapter.CartAdapter;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.Credit;
import com.shm_rz.ufoodapp.Model.Food;
import com.shm_rz.ufoodapp.Model.Order;
import com.shm_rz.ufoodapp.Model.OrderItem;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.katso.livebutton.LiveButton;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Cart extends AppCompatActivity implements
        CartAdapter.customAdapterInterface{

    String comment= "";
    Date twenty;
    Date currentTime;
    String receveTime = "";
    int AllCredit = 0;

    TextView edtAddress;
    Spinner spStudentDefaultAddress;
    TextView txtFinalPrice;
    TextView txtNowTotalCredit;
    TextView txtAlert;

    ArrayList<Food> lstAllFood = new ArrayList<>();

    int pprriiccee = 0;
    int finalpprriiccee = 0;

    boolean flag = false;
    boolean delivery = false;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int UCreditValue = 0;


    public TextView txtTotalPrice;
    LiveButton btnPlace;

    ArrayList<String> addressSpinner = new ArrayList<>();
    int addressID = 0;

    int x = 0;
    int y = 0;

    CheckBox ckbUsedCredit;
    CheckBox ckbReceveOnTime;

    ArrayList<Order> cart = new ArrayList<>();
    ArrayList<Order> foodList = new ArrayList<>();
    ArrayList<Integer> foodCount = new ArrayList<>();
    ArrayList<Integer> foodID = new ArrayList<>();


    CartAdapter adapter;
    int FinalPrice ;
    int recoveryFinalPrice ;
    int recoveryCreditPrice;

    Retrofit retrofit;
    ApiService service;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            this.getWindow().setStatusBarColor(Color.WHITE);
        }



        retrofit = new Retrofit.Builder()
                .baseUrl("http://android-application-api.ir/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);

        //init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice = (TextView) findViewById(R.id.Total);

        if(Common.fromAddressMe.equals("yes")){
            loadListFood();
            showAlertDialog();
            Common.fromAddressMe = "No";
        }

        btnPlace = (LiveButton) findViewById(R.id.btn_place_order);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Integer> timeToEndOrder = service.TimeToOrder();
                timeToEndOrder.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int resu = response.body();
                        if(resu == 1){
                            if (cart.size() > 0) {
                                showAlertDialog();
                            } else
                                Toast.makeText(Cart.this, "سبد خرید خالی ست", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Cart.this, "رستوران بسته شده", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(Cart.this, "خطا", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

                        loadListFood();


    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.order_address_comment , null);

       edtAddress = (TextView) dialogView.findViewById(R.id.edtAddress);
       spStudentDefaultAddress = (Spinner) dialogView.findViewById(R.id.spStudentDefaultAddress);
       txtFinalPrice = (TextView) dialogView.findViewById(R.id.txtFinalPrice);
       txtNowTotalCredit = (TextView) dialogView.findViewById(R.id.txtNowTotalCredit);


       txtAlert = (TextView) dialogView.findViewById(R.id.txtAlert);
       final EditText edtComment = (EditText) dialogView.findViewById(R.id.edtComment);
       Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
       Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
       ckbUsedCredit = (CheckBox) dialogView.findViewById(R.id.ckbUsedCredit);
       ckbReceveOnTime = (CheckBox) dialogView.findViewById(R.id.ckbReceveOnTime);



        alertDialog.setView(dialogView);

        Call<Integer> UniCreditTime = service.TimeToUniCreditOrder();
        UniCreditTime.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int resuu = response.body();

                if(resuu == 1){
                    ckbReceveOnTime.setEnabled(true);
                    ckbReceveOnTime.setClickable(true);
                }else{
                    ckbReceveOnTime.setEnabled(false);
                    ckbReceveOnTime.setClickable(false);
                    receveTime = "درلحظه";
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "خطا", Toast.LENGTH_LONG).show();
            }
        });

        Call<Credit> OnlyUCredit = service.ShowCreditOnlyU(Common.currentUser.getEmail());
        OnlyUCredit.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {

                Credit credit = response.body();
                if(credit.getID() != 0){
                    UCreditValue = credit.getCredit();
                }else {
                    UCreditValue = 0;
                }
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });


       addressSpinner.add("خوابگاه ایثار");
       addressSpinner.add("خوابگاه صدر");
       addressSpinner.add("خوابگاه مدنی");
       addressSpinner.add("خوابگاه خلیلی");
       addressSpinner.add("خوابگاه دانش");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, addressSpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spStudentDefaultAddress.setAdapter(adapter);

        spStudentDefaultAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(addressSpinner.get(position).equals("خوابگاه ایثار")){
                    addressID = 2079;
                }else if(addressSpinner.get(position).equals("خوابگاه صدر")){
                    addressID = 2080;
                }else if(addressSpinner.get(position).equals("خوابگاه مدنی")){
                    addressID = 2081;
                }else if(addressSpinner.get(position).equals("خوابگاه خلیلی")){
                    addressID = 2082;
                }else if(addressSpinner.get(position).equals("خوابگاه دانش")){
                    addressID = 2083;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(Common.currentUser.getAccess().equals("دانشجو")){
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
            String s = mdformat.format(rightNow.getTime());
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            try {
                twenty = parser.parse("20:00");
                currentTime = parser.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(currentTime.before(twenty)){
                delivery = false;
            }else {
                delivery = true;
            }
        }else {
            delivery = true;
        }

        ckbReceveOnTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onTime(isChecked);
            }
        });
        Call<Credit> CreditCall = service.ShowCredit(Common.currentUser.getEmail());
        CreditCall.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {

                Credit credit = response.body();
                if(credit.getID() == 0){
                    txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber("0"));
                }else {
                    txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(credit.getCredit())));
                    recoveryCreditPrice = credit.getCredit();
                }
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        if(delivery){
            txtAlert.setVisibility(View.VISIBLE);
            pprriiccee = FinalPrice + 3000;
            finalpprriiccee = recoveryFinalPrice + 3000;
        }
        else{
            txtAlert.setVisibility(View.GONE);
            pprriiccee = FinalPrice ;
            finalpprriiccee = recoveryFinalPrice;
        }

       txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(pprriiccee)));


       ckbUsedCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               onCreditUse(isChecked);
           }
       });

        edtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.firstPage.add(Cart.this);
                Intent intent = new Intent(Cart.this , AddressMeActivity.class);
                Common.comeFromCart = "yes";
                startActivity(intent);
                finish();
            }
        });

        if(Common.selectedAddress != null){
            edtAddress.setText(Common.selectedAddress.getAddress());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = edtComment.getText().toString();
                /////////////////////////////////////////////////////////
                foodList = new Database(context).getCarts();
                    if(Common.selectedAddress != null) {
                        Call<Integer> call = service.CreateOrderFactor(
                                Integer.parseInt(foodList.get(0).getResID()),
                                Common.currentUser.getEmail(),
                                comment,
                                Common.selectedAddress.getUserAddressID(),
                                flag,
                                delivery,
                                receveTime
                        );
                        call.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                final int result = response.body();
                                if (result != -1) {
                                    Common.selectedAddress = null;
                                    Call<Integer> AddCreditCall = service.AddNewCredite(
                                            Common.currentUser.getEmail(),
                                            y,
                                            "123456789",
                                            receveTime
                                    );
                                    AddCreditCall.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            int creditResult = response.body();

                                            if (creditResult == 0) {
                                                Toast.makeText(context, "مشکلی پیش آمده.مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Call<Integer> editFactorCall = service.UpdateOrderFactor(result, String.valueOf(y));
                                                editFactorCall.enqueue(new Callback<Integer>() {
                                                    @Override
                                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                        int editResult = response.body();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Integer> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {

                                            Log.i("Hello", "" + t);
                                            Toast.makeText(context, "Throwable" + t, Toast.LENGTH_LONG).show();

                                        }
                                    });
                                    for (Order myOrder : foodList) {
                                        foodCount.add(Integer.parseInt(myOrder.getFoodCount()));
                                        foodID.add(Integer.parseInt(myOrder.getFoodID()));
                                    }
                                    OrderItem orderItem = new OrderItem(
                                            result,
                                            foodCount,
                                            foodID
                                    );
                                    Call<Integer> itemCall = service.CreateOrderFactorItem(orderItem);
                                    itemCall.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            int result = response.body();
                                            if (result != -1) {
                                                new Database(getApplicationContext()).cleanCart();
                                                Intent homeIntent = new Intent(Cart.this, Home.class);
                                                startActivity(homeIntent);
                                                finish();
                                                Toast.makeText(context, "پرداخت با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Cart.this, "خطایی رخ داده لطفا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {

                                            Log.i("Hello", "" + t);
                                            Toast.makeText(getApplicationContext(), "Throwable" + t, Toast.LENGTH_LONG).show();

                                        }
                                    });

                                } else {
                                    Toast.makeText(Cart.this, "خطایی رخ داده لطفا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                                Log.i("Hello", "" + t);
                                Toast.makeText(getApplicationContext(), "Throwable" + t, Toast.LENGTH_LONG).show();

                            }
                        });
                    }else{
                        if(addressID == 0){
                            Toast.makeText(Cart.this, "آدرس خالی است", Toast.LENGTH_SHORT).show();
                        }else {
                            Call<Integer> call = service.CreateOrderFactor(
                                    Integer.parseInt(foodList.get(0).getResID()),
                                    Common.currentUser.getEmail(),
                                    comment,
                                    addressID,
                                    flag,
                                    delivery,
                                    receveTime
                            );
                            call.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    int result = response.body();
                                    if(result != -1) {
                                        Common.selectedAddress = null;
                                        Call<Integer> AddCreditCall = service.AddNewCredite(
                                                Common.currentUser.getEmail(),
                                                y,
                                                "123456789",
                                                receveTime
                                        );
                                        AddCreditCall.enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                int result = response.body();

                                                if (result == 0) {
                                                    Toast.makeText(context, "مشکلی پیش آمده.مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                                } else {

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {

                                                Log.i("Hello", "" + t);
                                                Toast.makeText(context, "Throwable" + t, Toast.LENGTH_LONG).show();

                                            }
                                        });
                                        for (Order myOrder : foodList) {
                                            foodCount.add(Integer.parseInt(myOrder.getFoodCount()));
                                            foodID.add(Integer.parseInt(myOrder.getFoodID()));
                                        }
                                        OrderItem orderItem = new OrderItem(
                                                result,
                                                foodCount,
                                                foodID
                                        );
                                        Call<Integer> itemCall = service.CreateOrderFactorItem(orderItem);
                                        itemCall.enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                int result = response.body();
                                                if (result != -1) {
                                                    new Database(getApplicationContext()).cleanCart();
                                                    Intent homeIntent = new Intent(Cart.this, Home.class);
                                                    startActivity(homeIntent);
                                                    finish();
                                                    Toast.makeText(context, "پرداخت با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Cart.this, "خطایی رخ داده لطفا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {

                                                Log.i("Hello", "" + t);
                                                Toast.makeText(getApplicationContext(), "Throwable" + t, Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }else {
                                        Toast.makeText(Cart.this, "خطایی رخ داده لطفا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {

                                    Log.i("Hello",""+t);
                                    Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
            }
        });
        alertDialog.show();
    }
    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart  , this, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculate total price
        int total = 0;
        FinalPrice = 0;
        recoveryFinalPrice = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getFoodPrice())) * (Integer.parseInt(order.getFoodCount()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmp = NumberFormat.getNumberInstance(locale);

        txtTotalPrice.setText(PersianDigitConverter.PerisanNumber(fmp.format(total)));
        FinalPrice = total;
        recoveryFinalPrice = total;
    }
    @Override
    public void onCustomListItemClick(int position) {
        Order order = cart.get(position);
        new Database(this).DeleteOrder(order.getID());
        loadListFood();
    }

    private void onTime(boolean isChecked){
        int TotalPriceValue = Integer.parseInt(txtFinalPrice.getText().toString());
        int CreditValue = Integer.parseInt(txtNowTotalCredit.getText().toString());

        if(isChecked){
            delivery = true;
            receveTime = "درلحظه";
            txtAlert.setVisibility(View.VISIBLE);
            if(ckbUsedCredit.isChecked()){
                if(TotalPriceValue == 0){
                    CreditValue = CreditValue - 3000 - UCreditValue;
                    if(CreditValue >= 0){
                        txtFinalPrice.setText(PersianDigitConverter.PerisanNumber("0"));
                        txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(CreditValue)));
                    }else {
                        txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(0 - CreditValue)));
                        txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber("0"));
                    }
                }else {
                    TotalPriceValue = TotalPriceValue + 3000 + UCreditValue;
                    txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(TotalPriceValue)));
                }
            }else {
                CreditValue = CreditValue - UCreditValue;
                txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(CreditValue)));
                TotalPriceValue = TotalPriceValue + 3000;
                txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(TotalPriceValue)));
            }
        }else {
            delivery = false;
            receveTime = "شام";
            txtAlert.setVisibility(View.GONE);
            if(ckbUsedCredit.isChecked()){
                if(TotalPriceValue == 0){
                    CreditValue = CreditValue + 3000 + UCreditValue;
                    txtFinalPrice.setText(PersianDigitConverter.PerisanNumber("0"));
                    txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(CreditValue)));
                }else {
                    TotalPriceValue = TotalPriceValue - 3000 - UCreditValue;
                    if(TotalPriceValue >= 0){
                        txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(TotalPriceValue)));
                        txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber("0"));
                    }else {
                        txtFinalPrice.setText(PersianDigitConverter.PerisanNumber("0"));
                        txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(0 - TotalPriceValue)));
                    }
                }
            }else {
                CreditValue = CreditValue + UCreditValue;
                TotalPriceValue = TotalPriceValue - 3000;
                txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(CreditValue)));
                txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(TotalPriceValue)));
            }
        }
    }

    private void onCreditUse(boolean isChecked){
        int TotalPriceValue = Integer.parseInt(txtFinalPrice.getText().toString());
        int CreditValue = Integer.parseInt(txtNowTotalCredit.getText().toString());

        if(isChecked){
            flag = true;
            if(Common.currentUser.getAccess().equals("دانشجو")){
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                String s = mdformat.format(rightNow.getTime());
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                try {
                    twenty = parser.parse("20:00");
                    currentTime = parser.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(currentTime.before(twenty) && !ckbUsedCredit.isChecked()){
                    edtAddress.setVisibility(View.GONE);
                    spStudentDefaultAddress.setVisibility(View.VISIBLE);
                }else {
                    edtAddress.setVisibility(View.VISIBLE);
                    spStudentDefaultAddress.setVisibility(View.GONE);
                }
            }
            if(Integer.parseInt(txtNowTotalCredit.getText().toString()) == 0){
                Toast.makeText(context, "شما اعتباری ندارید", Toast.LENGTH_SHORT).show();
                ckbUsedCredit.setChecked(false);
            }else {
                if(Integer.parseInt(txtNowTotalCredit.getText().toString()) <= pprriiccee){
                    x = Integer.parseInt(txtFinalPrice.getText().toString()) - Integer.parseInt(txtNowTotalCredit.getText().toString());
                    y = 0 - Integer.parseInt(txtNowTotalCredit.getText().toString());

                    txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(x)));
                    txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber("0"));
                }else {
                    y = 0 - Integer.parseInt(txtFinalPrice.getText().toString());
                    x = Integer.parseInt(txtNowTotalCredit.getText().toString()) - Integer.parseInt(txtFinalPrice.getText().toString()) ;
                    txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(0)));
                    txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(x)));
                }
            }
        }else {
            flag = false;

            edtAddress.setVisibility(View.VISIBLE);
            spStudentDefaultAddress.setVisibility(View.GONE);
            if(ckbReceveOnTime.isChecked()){
                int niceFinalPrice = finalpprriiccee + 3000;
                int niceCredit = recoveryCreditPrice - UCreditValue;

                txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(niceFinalPrice)));
                txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(niceCredit)));
            }else {
                txtFinalPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(finalpprriiccee)));
                txtNowTotalCredit.setText(PersianDigitConverter.PerisanNumber(String.valueOf(recoveryCreditPrice)));
            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Cart.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
