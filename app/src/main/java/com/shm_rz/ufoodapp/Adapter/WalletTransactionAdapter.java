package com.shm_rz.ufoodapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shm_rz.ufoodapp.Model.CreditTransaction;
import com.shm_rz.ufoodapp.R;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Asus on 10/02/2019.
 */

public class WalletTransactionAdapter  extends RecyclerView.Adapter<WalletTransactionAdapter.MyViewHolder> {



    private ArrayList<CreditTransaction> cmList;

    public WalletTransactionAdapter(ArrayList<CreditTransaction> cmList){
        this.cmList=cmList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_fragment_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder , final int position) {
        CreditTransaction comment = cmList.get(position);

        Locale locale = new Locale("en", "US");
        NumberFormat fmp = NumberFormat.getNumberInstance(locale);

        holder.txtTransactionCode.setText(PersianDigitConverter.PerisanNumber(comment.getTransactionCode()));
        holder.txtPrice.setText(PersianDigitConverter.PerisanNumber(fmp.format(Integer.parseInt(comment.getPrice()))));
        holder.txtDate.setText(PersianDigitConverter.PerisanNumber(comment.getDate()));
        holder.txtTime.setText(PersianDigitConverter.PerisanNumber(comment.getTime()));

        if(comment.CreditType)
            holder.cmCardView.setBackgroundResource(R.color.AddCredit);
        else
            holder.cmCardView.setBackgroundResource(R.color.MinesCredit);
    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView
                txtTransactionCode , txtPrice , txtDate , txtTime;
        private CardView
                cmCardView;




        public MyViewHolder(final View itemView) {
            super(itemView);

            txtTransactionCode = (TextView)itemView.findViewById(R.id.txtTransactionCode);
            txtPrice = (TextView)itemView.findViewById(R.id.txtPrice);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtTime=(TextView)itemView.findViewById(R.id.txtTime);
            cmCardView=(CardView)itemView.findViewById(R.id.cmCardView);

        }
    }
}
