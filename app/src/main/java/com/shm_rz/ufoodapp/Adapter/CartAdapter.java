package com.shm_rz.ufoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shm_rz.ufoodapp.Cart;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.Order;
import com.shm_rz.ufoodapp.R;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Roozbeh Zamani on 02/06/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    int counter = 0 ;
    Retrofit retrofit;
    Order order;
    ApiService service;
    int realFoodCount;
    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";


    String imageUrl =  "";


    Cart cart = new Cart();

    private Context context;
    private ArrayList<Order> orderList;
    CartAdapter.customAdapterInterface customAdapterInterface;

    public CartAdapter(
            ArrayList<Order> orderList,
            CartAdapter.customAdapterInterface customAdapterInterface,
            Context context){
        this.customAdapterInterface = customAdapterInterface;
        this.orderList = orderList;
        this.context = context;
    }



    public interface customAdapterInterface {
        void onCustomListItemClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder , final int position) {

        Locale locale = new Locale("en", "US");
        NumberFormat fmp = NumberFormat.getNumberInstance(locale);

        order = orderList.get(position);
        imageUrl = imageBaseUrl + order.getFoodImage();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://android-application-api.ir/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
        Call<Integer> callFoodCount = service.ChechFoodCountForOrder(Integer.parseInt(order.getFoodID()));
        callFoodCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                realFoodCount = response.body();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show();
            }
        });

        holder.txtCartItemPlaceName.setText(order.getRestuarantName());
        holder.txtCartItemCount.setText(PersianDigitConverter.PerisanNumber(order.getFoodCount()));
        holder.txtCartItemName.setText(order.getFoodName());
        holder.txtCartItemPrice.setText(PersianDigitConverter.PerisanNumber(fmp.format(Integer.parseInt(String.valueOf(Integer.parseInt(order.getFoodPrice()) * Integer.parseInt(order.getFoodCount()))))));
        holder.imgCartItemAddCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order myCounterOrder = orderList.get(position);
                counter = Integer.parseInt(holder.txtCartItemCount.getText().toString());
                Call<Integer> callFoodCount = service.ChechFoodCountForOrder(Integer.parseInt(myCounterOrder.getFoodID()));
                callFoodCount.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        realFoodCount = response.body();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show();
                    }
                });
                if(counter < realFoodCount || realFoodCount == -1){
                    counter++;
                    holder.txtCartItemCount.setText(PersianDigitConverter.PerisanNumber(counter + ""));
                    holder.txtCartItemPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(Integer.parseInt(order.getFoodPrice()) * Integer.parseInt(holder.txtCartItemCount.getText().toString()))));
                    new Database(context).updateCart(String.valueOf(counter),order.getID());
                }else {
                    Toast.makeText(context, "تعداد درخواستی بیشتر از تعداد موجودی میباشد", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgCartItemMinesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = Integer.parseInt(holder.txtCartItemCount.getText().toString());
                if(counter > 1)
                {
                    counter--;
                    holder.txtCartItemCount.setText(PersianDigitConverter.PerisanNumber(counter + ""));
                    holder.txtCartItemPrice.setText(PersianDigitConverter.PerisanNumber(String.valueOf(Integer.parseInt(order.getFoodPrice()) * Integer.parseInt(holder.txtCartItemCount.getText().toString()))));
                    new Database(context).updateCart(String.valueOf(counter),order.getID());
                }
            }
        });

        Picasso
                .with(context)
                .load(imageUrl)
                .into(holder.imgCartItem);

        holder.btnCancel.setTag(position);

    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView
                txtCartItemName,
                txtCartItemPlaceName,
                txtCartItemCount,
                txtCartItemPrice;
        private RoundedImageView
                imgCartItem;
        private ImageButton
                imgCartItemAddCount,
                imgCartItemMinesCount,
                btnCancel;






        public MyViewHolder(final View itemView) {
            super(itemView);

            txtCartItemName = (TextView)itemView.findViewById(R.id.txtCartItemName);
            txtCartItemPlaceName = (TextView)itemView.findViewById(R.id.txtCartItemPlaceName);
            txtCartItemCount = (TextView)itemView.findViewById(R.id.txtCartItemCount);
            txtCartItemPrice = (TextView)itemView.findViewById(R.id.txtCartItemPrice);

            imgCartItem = (RoundedImageView)itemView.findViewById(R.id.imgCartItem);

            imgCartItemAddCount = (ImageButton)itemView.findViewById(R.id.imgCartItemAddCount);
            imgCartItemMinesCount = (ImageButton)itemView.findViewById(R.id.imgCartItemMinesCount);
            btnCancel = (ImageButton)itemView.findViewById(R.id.btnCancel);




            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=(int)v.getTag();
                    customAdapterInterface.onCustomListItemClick(pos);
                }
            });


        }
    }


}
