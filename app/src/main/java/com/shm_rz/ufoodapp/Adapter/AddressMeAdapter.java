package com.shm_rz.ufoodapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Interface.ItemClickListener;
import com.shm_rz.ufoodapp.Model.Address;
import com.shm_rz.ufoodapp.R;

import java.util.ArrayList;

/**
 * Created by Shabnam Moazam on 26/05/2018.
 */

public class AddressMeAdapter extends RecyclerView.Adapter<AddressMeAdapter.MyViewHolder> {

    public ArrayList<Address> addresseList;
    AddressMeAdapter.customAdapterInterface customAdapterInterface;


    private Context context;


    public AddressMeAdapter(ArrayList<Address> addresseList,
                            AddressMeAdapter.customAdapterInterface customAdapterInterface, Context context) {
        this.customAdapterInterface = customAdapterInterface;
        this.addresseList = addresseList;
        this.context = context;
    }


    public interface customAdapterInterface {
        void onCustomListItemClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addressme_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Address address = addresseList.get(position);


        holder.txt_TypeAddress_me.setText(address.getAddressType());
        holder.txt_Address_me.setText(address.getAddress());


        holder.resCartViewAddress.setTag(position);

    }

    @Override
    public int getItemCount() {
        return addresseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private ItemClickListener itemClickListener;

        private TextView
                txt_TypeAddress_me,
                txt_Address_me;

        private CardView
                resCartViewAddress;

        public MyViewHolder(final View itemView) {
            super(itemView);

            txt_Address_me = (TextView) itemView.findViewById(R.id.txt_Address_me);
            txt_TypeAddress_me = (TextView) itemView.findViewById(R.id.txt_TypeAddress_me);


            itemView.setOnCreateContextMenuListener(this);

            resCartViewAddress = (CardView) itemView.findViewById(R.id.AddressCartView);

            resCartViewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    customAdapterInterface.onCustomListItemClick(pos);
                }
            });
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("منو:");

            int possition = (int) v.getTag();
            Common.currentAddressAdapterPossition = possition;
            menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
            menu.add(0, 1, getAdapterPosition(), Common.DELETE);
        }
    }
}