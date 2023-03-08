package com.shm_rz.ufoodapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.OrderTransactionItemList;
import com.shm_rz.ufoodapp.Model.OrderTransactionReport;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransactionReportActivity extends AppCompatActivity {

    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";

    String imageUrl =  "";

    Retrofit retrofit;
    ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_report);

        Locale locale = new Locale("en", "US");
        final NumberFormat fmp = NumberFormat.getNumberInstance(locale);

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

        final ExpandableLayout layout = (ExpandableLayout) findViewById(R.id.expandable_layout);

        layout.setRenderer(new ExpandableLayout.Renderer<OrderTransactionReport,OrderTransactionItemList>() {
            @Override
            public void renderParent(View view, OrderTransactionReport phoneCategory, boolean isExpanded, int parentPosition) {
                ((TextView)view.findViewById(R.id.txtResName)).setText(phoneCategory.getResName());
                ((TextView)view.findViewById(R.id.txtOrderDate)).setText(PersianDigitConverter.PerisanNumber(phoneCategory.getOrderDate()));
                ((TextView)view.findViewById(R.id.txtOrderTime)).setText(PersianDigitConverter.PerisanNumber(phoneCategory.getOrderTime()));
                ((TextView)view.findViewById(R.id.txtFactorTotalPrice)).setText(PersianDigitConverter.PerisanNumber(fmp.format(phoneCategory.getOrderPrice())));
                ((TextView)view.findViewById(R.id.txtFactorID)).setText(PersianDigitConverter.PerisanNumber(String.valueOf(phoneCategory.getID())));
                ((TextView)view.findViewById(R.id.txtOrderStatus)).setText(phoneCategory.getStatus());
                view
                        .findViewById(R.id.arrow)
                        .setBackgroundResource(isExpanded?R.drawable.ic_keyboard_arrow_up_white_24dp:R.drawable.ic_keyboard_arrow_down_white_24dp);
                imageUrl = imageBaseUrl + phoneCategory.getResImage();
                Picasso
                        .with(getApplicationContext())
                        .load(imageUrl)
                        .into(((ImageView)view.findViewById(R.id.imgRes)));
            }

            @Override
            public void renderChild(final View view, OrderTransactionItemList phone, int parentPosition, int childPosition) {
                ((TextView)view.findViewById(R.id.txtFoodName)).setText(phone.getOrderName());
                ((TextView)view.findViewById(R.id.txtFoodCount)).setText(PersianDigitConverter.PerisanNumber(String.valueOf(phone.getOrderCount())));
                imageUrl = imageBaseUrl + phone.getOrderImage();
                Picasso
                        .with(getApplicationContext())
                        .load(imageUrl)
                        .into(((ImageView)view.findViewById(R.id.imgFood)));
            }
        });


        Call<List<OrderTransactionReport>> call = service.OrderFactorListAPI(Common.currentUser.getEmail());
        call.enqueue(new Callback<List<OrderTransactionReport>>() {
            @Override
            public void onResponse(Call<List<OrderTransactionReport>> call, Response<List<OrderTransactionReport>> response) {

                for (OrderTransactionReport comment : response.body()) {
                    layout.addSection(getSection(
                            comment.getID() ,
                            comment.getOrderDate(),
                            comment.getOrderTime(),
                            comment.getStatus(),
                            comment.getResName(),
                            comment.getOrderPrice(),
                            comment.getResImage()
                    ));
                }
            }

            @Override
            public void onFailure(Call<List<OrderTransactionReport>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });
    }
    private Section<OrderTransactionReport,OrderTransactionItemList> getSection(int id, String orderDate, String orderTime, String orderType, String resName, int totalPrice, String resImage) {

        final Section<OrderTransactionReport,OrderTransactionItemList> section = new Section<>();

        OrderTransactionReport phoneCategory = new OrderTransactionReport(
                id,
                totalPrice,
                resName,
                orderType,
                orderDate,
                orderTime,
                resImage
        );

        final List<OrderTransactionItemList> listPhone = new ArrayList<>();

        Call<List<OrderTransactionItemList>> callItems = service.OrderItemListAPI(id);
        callItems.enqueue(new Callback<List<OrderTransactionItemList>>() {
            @Override
            public void onResponse(Call<List<OrderTransactionItemList>> call, Response<List<OrderTransactionItemList>> response) {

                for (OrderTransactionItemList comment : response.body()) {
                    listPhone.add(new OrderTransactionItemList(
                            comment.ID,
                            comment.getOrderCount(),
                            comment.getOrderName(),
                            comment.getOrderImage()
                    ));
                }
                section.children.addAll(listPhone);
            }

            @Override
            public void onFailure(Call<List<OrderTransactionItemList>> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        section.parent = phoneCategory;


        return section;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TransactionReportActivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
