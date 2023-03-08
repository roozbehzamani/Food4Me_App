package com.shm_rz.ufoodapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shm_rz.ufoodapp.Database.Database;
import com.shm_rz.ufoodapp.FavoritesActivity;
import com.shm_rz.ufoodapp.FoodDetail;
import com.shm_rz.ufoodapp.Model.Favorites;
import com.shm_rz.ufoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Shabnam Moazam on 30/03/2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder> {

    private Context context;
    private List<Favorites> favoriteList;
    String imageBaseUrl = "http://android-application-api.ir/Content/images/";

    FavoritesActivity favoritesActivity;
    FavoritesAdapter.customAdapterInterface customAdapterInterface;

    String imageUrl =  "";
    Database db ;

    public FavoritesAdapter(
            Context context,
            List<Favorites> favoriteList,
            FavoritesAdapter.customAdapterInterface customAdapterInterface
    ) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.customAdapterInterface = customAdapterInterface;
    }

    public interface customAdapterInterface {
        void onCustomListItemClick(int position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favorites_item , parent , false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        imageUrl =  favoriteList.get(position).getFoodImage();
        holder.food_name.setText(favoriteList.get(position).getFoodName());
        holder.food_price.setText(String.format("$ %s" , favoriteList.get(position).getFoodPrice().toString()));
        Picasso.with(favoritesActivity).load(imageUrl)
                .into(holder.food_image);
        holder.menuCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetail=new Intent(context,FoodDetail.class);
                int x = favoriteList.get(position).getFoodId();
                foodDetail.putExtra("foodID" , String.valueOf(x));
                context.startActivity(foodDetail);
            }
        });
        holder.btnCancel.setTag(position);
    }


    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public Favorites getItem(int position){
        return favoriteList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView food_name , food_price;
        public ImageView food_image , btn_share;
        public CardView menuCardView ;
        private ImageButton
                btnCancel;

        public MyViewHolder(View itemView) {
            super(itemView);
            food_name=(TextView) itemView.findViewById(R.id.food_name);
            food_image=(ImageView)itemView.findViewById(R.id.food_image);
            btn_share=(ImageView)itemView.findViewById(R.id.btn_share);
            food_price = (TextView) itemView.findViewById(R.id.food_price);
            menuCardView = (CardView) itemView.findViewById(R.id.menuCardView);
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
