package com.shm_rz.ufoodapp.Utility;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by aashrai on 21/8/15.
 */
public class SkittleSample extends Application{

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher=LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context){
        SkittleSample application= (SkittleSample) context.getApplicationContext();
        return application.refWatcher;
    }
}
