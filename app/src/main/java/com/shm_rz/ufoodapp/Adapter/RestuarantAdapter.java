package com.shm_rz.ufoodapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shm_rz.ufoodapp.Home;
import com.shm_rz.ufoodapp.Model.Resturants;
import com.shm_rz.ufoodapp.R;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shabnam Moazam on 09/05/2018.
 */

public class RestuarantAdapter extends RecyclerView.Adapter<RestuarantAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Resturants> resList;
    customAdapterInterface customAdapterInterface;
    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";
    String imageUrl =  "";
    Home home;



    public RestuarantAdapter(ArrayList<Resturants> resList, customAdapterInterface customAdapterInterface , Context context){
        this.customAdapterInterface=customAdapterInterface;
        this.resList=resList;
        this.context = context;
    }

    public interface customAdapterInterface {
        void onCustomListItemClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resturant_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder , final int position) {
        Resturants resturants = resList.get(position);

        imageUrl = imageBaseUrl + resturants.getResImage();
        holder.resturantName.setText(resturants.getResName());
        holder.resturantAddress.setText(PersianDigitConverter.PerisanNumber(resturants.getResAddress()));
        Picasso
                .with(context)
                .load(imageUrl)
                .into(holder.resturant_image);

        holder.resCartView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return resList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView
                resturantName ,
                resturantAddress;
        private ImageView
                resturant_image;
        private CardView
                resCartView;

        public MyViewHolder(final View itemView) {
            super(itemView);

            resturantName=(TextView)itemView.findViewById(R.id.resturantName);
            resturantAddress=(TextView)itemView.findViewById(R.id.resturantAddress);
            resturant_image=(ImageView) itemView.findViewById(R.id.resturant_image);
            resCartView=(CardView)itemView.findViewById(R.id.resCartView);

            resCartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=(int)v.getTag();
                    customAdapterInterface.onCustomListItemClick(pos);
                }
            });
        }
    }
}