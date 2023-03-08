package com.shm_rz.ufoodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.Credit;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;

import java.text.NumberFormat;
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
public class WalletFragment extends Fragment {

    View view;
    Context context;

    TextView txtCredit;
    EditText edtCreditAmount;
    Button btnDefaultFifty , btnDefaultTwentyFiveّ , btnDefaultTen , btnSave;

    Retrofit retrofit;
    ApiService service;
    Call<Integer> AddCreditCall;

    @SuppressLint("ValidFragment")
    public WalletFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wallet, container, false);

        Locale locale = new Locale("en", "US");
        final NumberFormat fmp = NumberFormat.getNumberInstance(locale);

        txtCredit = (TextView) view.findViewById(R.id.txtCredit);
        edtCreditAmount = (EditText) view.findViewById(R.id.edtCreditAmount);
        btnDefaultFifty = (Button) view.findViewById(R.id.btnDefaultFifty);
        btnDefaultTwentyFiveّ = (Button) view.findViewById(R.id.btnDefaultTwentyFiveّ);
        btnDefaultTen = (Button) view.findViewById(R.id.btnDefaultTen);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://android-application-api.ir/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        btnDefaultFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCreditAmount.setText(PersianDigitConverter.PerisanNumber("50000"));
            }
        });

        btnDefaultTwentyFiveّ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCreditAmount.setText(PersianDigitConverter.PerisanNumber("25000"));
            }
        });

        btnDefaultTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCreditAmount.setText(PersianDigitConverter.PerisanNumber("10000"));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String credit = edtCreditAmount.getText().toString();

                AddCreditCall = service.AddNewCredite(
                        Common.currentUser.getEmail() ,
                        Integer.parseInt(credit) ,
                        "123456789",
                        "درلحظه"
                );
                AddCreditCall.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int result = response.body();

                        if(result == 0){
                            Toast.makeText(getContext(), "مشکلی پیش آمده.مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                            edtCreditAmount.setText(null);
                        }else {
                            Toast.makeText(getContext(), "افزایش اغتبار با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity() , Home.class);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                        Log.i("Hello",""+t);
                        Toast.makeText(getContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        Call<Credit> CreditCall = service.ShowCredit(Common.currentUser.getEmail());
        CreditCall.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {

                Credit credit = response.body();
                if(credit.getID() == 0){
                    txtCredit.setText("۰");
                }else {
                    txtCredit.setText(PersianDigitConverter.PerisanNumber(fmp.format(credit.getCredit())));
                }
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {

                Log.i("Hello",""+t);
                Toast.makeText(getContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }
}