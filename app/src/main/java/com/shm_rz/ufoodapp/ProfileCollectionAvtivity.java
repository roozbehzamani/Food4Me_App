package com.shm_rz.ufoodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shm_rz.ufoodapp.Common.Common;
import com.shm_rz.ufoodapp.Model.Student;
import com.shm_rz.ufoodapp.Model.User;
import com.shm_rz.ufoodapp.Service.ApiService;
import com.shm_rz.ufoodapp.Utility.PersianDigitConverter;
import com.shm_rz.ufoodapp.Utility.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileCollectionAvtivity extends AppCompatActivity  implements
        View.OnClickListener, DateSetListener {
    Button btsSaveChanges;
    EditText edtName , edtFamily;
    String bDate;
    RoundedImageView imgProfileImage;

    private TextView mStart;
    private Date mStartDate;
    TextView txtPhone , btnSelectSex , edtEmail;

    public ApiService service;
    Call<User>  callShow;
    Call<String>  callEdit;
    String LastImage;

    String imageUrlDrawer =  "";
    String imageBaseUrlDrawer = "http://android-application-api.ir/Content/UserContent/images/imgpic/";
    public java.util.Date date;
    int flag = 0;

    String imageBaseUrl = "http://android-application-api.ir/Content/UserContent/images/";


    String imageUrl =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_collection_avtivity);

        service = ServiceGenerator.createService(ApiService.class);

        Button changerPassword = (Button) findViewById(R.id.nav_change_password);
        changerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        Button AddressMe = (Button) findViewById(R.id.nav_AddressMe);
        AddressMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.firstPage.add(ProfileCollectionAvtivity.this);
                Intent AddressIntent = new Intent(ProfileCollectionAvtivity.this , AddressMeActivity.class);
                startActivity(AddressIntent);
                finish();
            }
        });

        imgProfileImage = (RoundedImageView) findViewById(R.id.imgProfileImage);

        Button btnMyCredit = (Button) findViewById(R.id.btnMyCredit);
        btnMyCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.firstPage.add(ProfileCollectionAvtivity.this);
                Intent MyCreditIntent = new Intent(ProfileCollectionAvtivity.this , WalletActivity.class);
                startActivity(MyCreditIntent);
                finish();
            }
        });
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        mStart = (TextView) findViewById(R.id.startDate);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        edtName = (EditText) findViewById(R.id.edtName);
        edtFamily = (EditText) findViewById(R.id.edtFamily);
        edtEmail = (TextView) findViewById(R.id.edtEmail);
        btsSaveChanges = (Button) findViewById(R.id.btsSaveChanges);
        btsSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = edtName.getText().toString();
                String f = edtFamily.getText().toString();
                String bd = mStart.getText().toString();
                String s = btnSelectSex.getText().toString();

                callEdit = service.editUserProfile(
                        Common.currentUser.getEmail()   ,
                        n       ,
                        s  ,
                        bd,
                        f
                );
                callEdit.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if(result.equals("Success")){
                            Toast.makeText(getApplicationContext(), "مشخصات با موفقیت ویرایش شد", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileCollectionAvtivity.this , ProfileCollectionAvtivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "خطا در عملیات . لطفا محظاتی بعد امتحان نمایید", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(ProfileCollectionAvtivity.this, "خطا", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnSelectSex = (TextView) findViewById(R.id.btnSelectSex);

        btnSelectSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullAddressDialog();
            }
        });

        mStartDate = new Date();
        mStart.setOnClickListener(this);
        setLocale("fa");



        callShow = service.getprofile(Common.currentUser.getEmail());
        callShow.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                txtPhone.setText(PersianDigitConverter.PerisanNumber(user.getMob_phone()));
                edtName.setText(user.getName());
                edtFamily.setText(user.getFamily());
                edtEmail.setText(user.getEmail());
                if(user.getSex() == null)
                    btnSelectSex.setText(Common.sex);
                else
                    btnSelectSex.setText(user.getSex());
                if(user.getBirth_date() == null)
                    mStart.setText("تاریخ تولد را وارد کنید");
                else
                    mStart.setText(PersianDigitConverter.PerisanNumber(String.valueOf(user.getBirth_date())));

                imageUrl = imageBaseUrl + user.getImage();
                Picasso
                        .with(getApplicationContext())
                        .load(imageUrl)
                        .into(imgProfileImage);
                Common.currentUser.setImage(user.getImage());
                Common.currentUser.setName(user.getName());
                Common.currentUser.setFamily(user.getFamily());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileCollectionAvtivity.this, "خطا", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileCollectionAvtivity.this);
        alertDialog.setTitle("تغییر رمز");
        alertDialog.setMessage("لطفا تمام اطلاعات را پر کنید");

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_pwd = inflater.inflate(R.layout.change_password_layout , null);

        final MaterialEditText edtPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtNewPassword);
        final MaterialEditText edtRepeatPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtRepeatPassword);

        alertDialog.setView(layout_pwd);

        //Button
        alertDialog.setPositiveButton("ذخیره", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //change password here


                //check old password

                if(edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString())){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://android-application-api.ir/")
                            //.client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ApiService service = retrofit.create(ApiService.class);

                    Call<Student> call = service.getChangePassword(edtNewPassword.getText().toString(),Common.currentUser.getEmail());

                    call.enqueue(new Callback<Student>() {
                        @Override
                        public void onResponse(Call<Student> call, Response<Student> response) {

                            Toast.makeText(getApplicationContext(), "response" + response, Toast.LENGTH_LONG).show();

                            edtPassword.setText("");
                            edtNewPassword.setText("");
                            edtRepeatPassword.setText("");
                        }
                        @Override
                        public void onFailure(Call<Student> call, Throwable t) {

                        }
                    });
                    Toast.makeText(ProfileCollectionAvtivity.this, "رمز با موفقیت تفییر کرد", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProfileCollectionAvtivity.this, "رمز عبور تکراری مطابقت ندارد", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId() == R.id.startDate ? 1 : 2;

        DatePicker.Builder builder = new DatePicker
                .Builder()
                .id(id)
                .maxDate(1400,12,31);


        if (v.getId() == R.id.startDate)
            builder.date(mStartDate.getDay(), mStartDate.getMonth(), mStartDate.getYear());


        builder.build(ProfileCollectionAvtivity.this)
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (id == 1) {
            mStartDate.setDate(day, month, year);
            mStart.setText(PersianDigitConverter.PerisanNumber(mStartDate.getDate()));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("fa");
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                    "%d/%d/%d",
                    getYear(), getMonth(), getDay(),
                    calendar.get(Calendar.YEAR),
                    +calendar.get(Calendar.MONTH) + 1,
                    +calendar.get(Calendar.DAY_OF_MONTH));
        }
    }



    private void fullAddressDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileCollectionAvtivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout_pwd = inflater.inflate(R.layout.layout_dialog , null);

        final Button btnWoman = (Button) layout_pwd.findViewById(R.id.edit_username);
        final Button btnMan = (Button) layout_pwd.findViewById(R.id.edit_man);

        alertDialog.setView(layout_pwd);


        btnWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.sex = "خانوم";
                btnSelectSex.setText(Common.sex);
            }
        });
        btnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.sex = "آقا";
                btnSelectSex.setText(Common.sex);
            }
        });

        alertDialog.setNegativeButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileCollectionAvtivity.this , Common.firstPage.get(Common.firstPage.size() - 1).getClass());
        Common.firstPage.remove(Common.firstPage.size() - 1);
        startActivity(intent);
        finish();
    }
}
