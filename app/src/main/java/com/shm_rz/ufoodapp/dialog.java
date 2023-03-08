package com.shm_rz.ufoodapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Shabnam Moazam on 25/05/2018.
 */

public class dialog extends AppCompatDialogFragment {
    private Button editTextUsername;
    private Button editman;
    public String username;
    private  ExampleDialogListner listner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialog,null);
        editTextUsername=(Button)view.findViewById(R.id.edit_username);
        editTextUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=editTextUsername.getText().toString();
            }
        });
        editman=(Button)view.findViewById(R.id.edit_man);
        editman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=editman.getText().toString();
            }
        });
        builder.setView(view)
                .setTitle("انتخاب کنید")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listner.applytext(username);
                    }
                });

        return  builder.create();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (ExampleDialogListner) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "error");
        }
    }

    public  interface  ExampleDialogListner{
        void applytext(String username);
    }


}
