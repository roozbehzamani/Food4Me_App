package com.shm_rz.ufoodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.shm_rz.ufoodapp.Adapter.WalletTransactionAdapter;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.CreditTransaction;
import com.shm_rz.ufoodapp.Service.ApiService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shabnam on 06/12/2018.
 */

@SuppressLint("ValidFragment")
public class TransactionFragment extends Fragment {

    View view;
    Context context;

    ArrayList<CreditTransaction> cmList;
    WalletTransactionAdapter commentAdapter;
    RecyclerView recyclerView;

    @SuppressLint("ValidFragment")
    public TransactionFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction, container, false);

        cmList = new ArrayList();
        commentAdapter = new WalletTransactionAdapter(cmList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(commentAdapter);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://android-application-api.ir/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<List<CreditTransaction>> call = service.CreditReportList(Common.currentUser.getEmail());
        call.enqueue(new Callback<List<CreditTransaction>>() {
            @Override
            public void onResponse(Call<List<CreditTransaction>> call, Response<List<CreditTransaction>> response) {

                for (CreditTransaction comment : response.body()) {
                    cmList.add(comment);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CreditTransaction>> call, Throwable t) {
                Toast.makeText(context, "خطا", Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }
}