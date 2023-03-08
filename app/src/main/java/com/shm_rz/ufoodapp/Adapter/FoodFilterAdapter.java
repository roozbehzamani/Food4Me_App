package com.shm_rz.ufoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.Model.FoodFilter;
import com.shm_rz.ufoodapp.Model.Order;
import com.shm_rz.ufoodapp.R;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Roozbeh Zamani on 16/06/2018.
 */

public class FoodFilterAdapter extends RecyclerView.Adapter<FoodFilterAdapter.MyViewHolder> {

   ArrayList<FoodFilter> foodFilterList;
    FoodFilterAdapter.customAdapterInterface customAdapterInterface;

    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";

    String imageUrl =  "";

    Database db , localDB , localDB2;

    private Context context;
    ApiService service;

    String s= "";
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
    Date today = new Date();

    public FoodFilterAdapter(ArrayList<FoodFilter> foodFilterList,
                             FoodFilterAdapter.customAdapterInterface customAdapterInterface ,
                             Context context){
        this.customAdapterInterface=customAdapterInterface;
        this.foodFilterList = foodFilterList;
        this.context = context;
    }

    public interface customAdapterInterface {
        void onCustomListItemClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_filter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder , final int position) {
        final FoodFilter foodFilter = foodFilterList.get(position);

        Locale locale = new Locale("en", "US");
        NumberFormat fmp = NumberFormat.getNumberInstance(locale);

        service = ServiceGenerator.createService(ApiService.class);

        imageUrl = imageBaseUrl + foodFilter.getFoodImage();
        holder.txtFilterFoodMenuName.setText(foodFilter.getMenuName());
        holder.txtFilterFoodName.setText(foodFilter.getName());
        holder.txtFilterFoodPrice.setText(PersianDigitConverter.PerisanNumber(fmp.format(foodFilter.getCost())));

        holder.txtFilterFoodResName.setText(foodFilter.getRestuarant());

        Picasso
                .with(context)
                .load(imageUrl)
                .into(holder.imgFilterFood);


        localDB = new Database(context);
        if(localDB.isFavorites(String.valueOf(foodFilter.getId()))){
            holder.imgButtonFilterFoodFavorites.setImageResource(R.drawable.ic_favorite_white);
        }else {
            holder.imgButtonFilterFoodFavorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


        holder.imgButtonFilterFoodFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localDB = new Database(context);
                if(!localDB.isFavorites(String.valueOf(foodFilter.getId()))){
                    localDB.addToFavorites(
                            String.valueOf(foodFilter.getId()) ,
                            holder.txtFilterFoodName.getText().toString(),
                            holder.txtFilterFoodPrice.getText().toString() ,
                            imageBaseUrl + foodFilter.getFoodImage() ,
                            "dfsdfsdfsdfsdf" ,
                            Common.currentUser.getMob_phone()
                    );
                    holder.imgButtonFilterFoodFavorites.setImageResource(R.drawable.ic_favorite_white);
                    Toast.makeText(context, "عذا با موفقیت به لیست مورد علاقه ها افزوده شد", Toast.LENGTH_SHORT).show();
                }else {
                    localDB.removeFavorites(String.valueOf(foodFilter.getId()));
                    holder.imgButtonFilterFoodFavorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(context, "غذا از لیست مورد علاقه ها حذف شد", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgButtonFilterFoodShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Integer> timeToEndOrder = service.TimeToOrder();
                timeToEndOrder.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int resu = response.body();
                        if(resu == 1){
                            FoodFilter foood = foodFilterList.get(position);
                            dateFormatter.setLenient(false);
                            today = Calendar.getInstance().getTime();
                            s = dateFormatter.format(today);
                            db = new Database(context);
                            if(db.FoodIsExist(String.valueOf(foood.getId()))){
                                String count;
                                Order myyyyOrder = db.getSinglecart(String.valueOf(foood.getId()));
                                count = String.valueOf(
                                        1 + Integer.parseInt(myyyyOrder.getFoodCount())
                                );
                                if(foodFilter.getFoodCount() >= Integer.parseInt(count) || foodFilter.getFoodCount() == -1)
                                    db.updateCartFoodID(count , String.valueOf(foood.getId()));
                                else
                                    Toast.makeText(context, "تعداد درخواستی در سبد خرید بیشتر از تعداد موجودی میباشد", Toast.LENGTH_SHORT).show();
                            }else {
                                if(foodFilter.getFoodCount() > 0 || foodFilter.getFoodCount() == -1){
                                    db.addToCart(
                                            String.valueOf(foood.getId()),
                                            foood.getName(),
                                            "1",
                                            String.valueOf(foood.getCost()),
                                            foood.getFoodImage(),
                                            s,
                                            foodFilter.getRestuarant(),
                                            foodFilter.resID
                                    );
                                }else {
                                    Toast.makeText(context, "این غذا در رستوران موجود نمیباشد", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {
                            Toast.makeText(context, "رستوران بسته شده", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if(foodFilter.getFoodCount() == 0){
            holder.imgButtonFilterFoodShoppingCart.setVisibility(View.GONE);
        }else {
            holder.imgButtonFilterFoodShoppingCart.setVisibility(View.VISIBLE);
        }

        if(foodFilter.isOrderType())
            holder.txtOrderType.setVisibility(View.VISIBLE);
        else
            holder.txtOrderType.setVisibility(View.GONE);

        if(foodFilter.getFoodCount() == -1)
            holder.txtFoodCount.setText("نامحدود");
        else
            holder.txtFoodCount.setText(PersianDigitConverter.PerisanNumber(String.valueOf(foodFilter.getFoodCount())));



        holder.foodFiterCardView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return foodFilterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView
                txtFilterFoodName,
                txtFilterFoodPrice,
                txtFilterFoodResName,
                txtFoodCount,
                txtOrderType,
                txtFilterFoodMenuName;
        private ImageView
                imgFilterFood;
        private ImageButton
                imgButtonFilterFoodShoppingCart,
                imgButtonFilterFoodFavorites;
        private CardView
                foodFiterCardView;

        public MyViewHolder(final View itemView) {
            super(itemView);

            txtFilterFoodName=(TextView)itemView.findViewById(R.id.txtFilterFoodName);
            txtOrderType=(TextView)itemView.findViewById(R.id.txtOrderType);
            txtFoodCount=(TextView)itemView.findViewById(R.id.txtFoodCount);
            txtFilterFoodPrice=(TextView)itemView.findViewById(R.id.txtFilterFoodPrice);
            txtFilterFoodResName=(TextView)itemView.findViewById(R.id.txtFilterFoodResName);
            txtFilterFoodMenuName=(TextView)itemView.findViewById(R.id.txtFilterFoodMenuName);
            imgFilterFood=(ImageView) itemView.findViewById(R.id.imgFilterFood);
            imgButtonFilterFoodShoppingCart=(ImageButton) itemView.findViewById(R.id.imgButtonFilterFoodShoppingCart);
            imgButtonFilterFoodFavorites=(ImageButton) itemView.findViewById(R.id.imgButtonFilterFoodFavorites);
            foodFiterCardView=(CardView)itemView.findViewById(R.id.foodFiterCardView);

            foodFiterCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=(int)v.getTag();
                    customAdapterInterface.onCustomListItemClick(pos);
                }
            });
        }
    }

}