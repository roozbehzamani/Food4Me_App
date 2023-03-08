package com.shm_rz.ufoodapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.User;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;

import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {

    ProgressDialog pd;

    EditText edtPhone , edtPassword;
    Button btnSignIn;
    CheckBox ckbRemember;
    TextView txt_forgetPassword;
    Spinner spUserAccess;
    ArrayList<String> lstAccess = new ArrayList<>();

    boolean selectTypeFlag = false;

    String selectedItem = "";
    Call<Integer> callForgetPassword;
    ApiService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        service = ServiceGenerator.createService(ApiService.class);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txt_forgetPassword = (TextView) findViewById(R.id.forget_password);
        ckbRemember = (CheckBox) findViewById(R.id.ckbRemember);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        spUserAccess = (Spinner) findViewById(R.id.spUserAccess);

        lstAccess.add("نوع ورود را انتخاب نمایید");
        lstAccess.add("کاربر عادی");
        lstAccess.add("دانشجو");
        lstAccess.add("کارمند");
        lstAccess.add("هیئت علمی");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, lstAccess);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spUserAccess.setAdapter(adapter);

        spUserAccess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = lstAccess.get(position);
                selectTypeFlag = true;
                if(selectedItem.equals("نوع ورود را انتخاب نمایید")){
                    btnSignIn.setEnabled(false);
                    edtPhone.setHint("");
                    edtPhone.setEnabled(false);
                }else if(selectedItem.equals("دانشجو")){
                    edtPhone.setHint("شماره دانشجویی");
                    btnSignIn.setEnabled(true);
                    edtPhone.setEnabled(true);
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_iconfinder_id, 0, 0, 0);
                }else {
                    edtPhone.setHint("شماره تلفن");
                    btnSignIn.setEnabled(true);
                    edtPhone.setEnabled(true);
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_android_black_24dp, 0, 0, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Paper.init(this);

        String userPhone = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        String acs = Paper.book().read(Common.ACS_KEY);

        if (userPhone != null && pwd != null && acs != null)
        {
            if (!userPhone.isEmpty() && !pwd.isEmpty() && !acs.isEmpty()){
                myProgress();
                User user = new User();
                user.setPassword(pwd);
                user.setMob_phone(userPhone);
                Call<User> call = service.insertSingnIn(userPhone , user.getPassword() , acs);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        edtPhone.setText("");
                        edtPassword.setText("");
                            Common.currentUser = response.body();
                            if(response.body() != null){
                                pd.dismiss();
                                Intent signInIntent = new Intent(SignIn.this , Home.class);
                                startActivity(signInIntent);
                            }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        Log.i("Hello",""+t);
                        Toast.makeText(SignIn.this, "مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

            }
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!edtPhone.getText().toString() .isEmpty()){
                        if(!edtPassword.getText().toString() .isEmpty()){
                            myProgress();

                            Call<User> call = service.insertSingnIn(edtPhone.getText().toString() , edtPassword.getText().toString() , selectedItem);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {

                                    //---------------------------------

                                    Common.currentUser = response.body();
                                    if(Common.currentUser != null){
                                        if (ckbRemember.isChecked())
                                        {
                                            Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                                            Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                                        }
                                        pd.dismiss();
                                        Intent signInIntent = new Intent(SignIn.this , Home.class);
                                        startActivity(signInIntent);
                                    }else {
                                        if(selectedItem.equals("دانشجو"))
                                            Toast.makeText(SignIn.this, "شماره دانشجویی یا رمز عبور صحیح نمیباشد", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(SignIn.this, "شماره همراه یا رمز عبور صحیح نمیباشد", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }



                                    //-------------------------------------

                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(SignIn.this, "مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            });
                        }else {
                            Toast.makeText(SignIn.this, "پسورد خالی میباشد", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SignIn.this, "شناسه ی ورود خالی میباشد", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        txt_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });

    }

    private void myProgress() {
        pd = new ProgressDialog(SignIn.this);
        pd.setMessage("لطفا منتظر بمانید...");
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.show();
        pd.setProgress(50);
    }


    private void forgetPassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignIn.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_pwd = inflater.inflate(R.layout.forget_password_popup_layout , null);

        final EditText edtPhone = (EditText) layout_pwd.findViewById(R.id.edtPhone);
        final EditText edtEmail = (EditText) layout_pwd.findViewById(R.id.edtEmail);
        Button btnSendEmail = (Button) layout_pwd.findViewById(R.id.btnSendEmail);

        alertDialog.setView(layout_pwd);


        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();
                callForgetPassword = service.AdminForgetPassword(email , phone);
                callForgetPassword.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int result = response.body();
                        if(result == 1){
                            Toast.makeText(SignIn.this, "رمز با موفقیت به ایمیل شما ارسال گردید", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignIn.this, "شماره همراه یا ایمیل وارد شده صحیح نمیباشد", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(SignIn.this, "خطایی رخ دادئه . لطفا مجددا اقدام نمایید", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });



        alertDialog.show();
    }


}
