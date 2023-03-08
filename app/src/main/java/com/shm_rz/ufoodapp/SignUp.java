package com.shm_rz.ufoodapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.shm_rz.ufoodapp.Model.Student;
import com.shm_rz.ufoodapp.Service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    EditText edtPassword , edtName , edtPhone , edtEmail , edtSNumber , edtFamily;
    Spinner spUserAccess;
    Button btnSignUp;
    ArrayList<String> lstAccess = new ArrayList<>();
    RelativeLayout sNumberRelativeLayout;

    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtemail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtName = (EditText) findViewById(R.id.edtName);
        edtFamily = (EditText) findViewById(R.id.edtFamily);
        edtSNumber = (EditText) findViewById(R.id.edtSNumber);
        spUserAccess = (Spinner) findViewById(R.id.spUserAccess);
        sNumberRelativeLayout = (RelativeLayout) findViewById(R.id.sNumberRelativeLayout);

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
                if(selectedItem.equals("نوع ورود را انتخاب نمایید")){
                    btnSignUp.setEnabled(false);
                    sNumberRelativeLayout.setVisibility(View.GONE);
                }else if(selectedItem.equals("دانشجو")){
                    sNumberRelativeLayout.setVisibility(View.VISIBLE);
                    btnSignUp.setEnabled(true);
                }else {
                    sNumberRelativeLayout.setVisibility(View.GONE);
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://android-application-api.ir/")
                        //.client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService service = retrofit.create(ApiService.class);
                final Student student = new Student();
                student.setAccess(selectedItem);
                if(selectedItem.equals("دانشجو")){
                    student.setSnumber(edtSNumber.getText().toString());
                }else {
                    student.setSnumber("");
                }
                student.setName(edtName.getText().toString());
                student.setEmail(edtEmail.getText().toString());
                student.setPassword(edtPassword.getText().toString());
                student.setMob_phone(edtPhone.getText().toString());
                String family = edtFamily.getText().toString();
                Call<Integer> call = service.insertData(student.getName(),student.getEmail(),student.getPassword(),student.getMob_phone() , student.getAccess() , student.getSnumber() , family);

                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {

                        //---------------------------------

                        int result = response.body();
                        if(result == 1 ){
                            Intent verificationIntent = new Intent(SignUp.this , SignIn.class);
                            startActivity(verificationIntent);
                            finish();
                        }else {
                            Toast.makeText(SignUp.this, "لطفا مجددا امتحان فرمایید", Toast.LENGTH_SHORT).show();
                            edtEmail.setText("");
                            edtPhone.setText("");
                            edtName.setText("");
                            edtPassword.setText("");
                            edtFamily.setText("");
                            sNumberRelativeLayout.setVisibility(View.GONE);
                        }





                        //-------------------------------------






                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {


                        Log.i("Hello",""+t);
                        Toast.makeText(getApplicationContext(), "Throwable"+t, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }
}
