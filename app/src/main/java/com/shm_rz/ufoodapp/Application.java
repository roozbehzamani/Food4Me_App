package com.shm_rz.ufoodapp;

/**
 * Created by Shabnam Moazam on 05/03/2018.
 */

import im.crisp.sdk.Crisp;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Alireza Afkar on 2/11/16 AD.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Crisp.initialize(this);
        Crisp.getInstance().setWebsiteId("7598bf86-9ebb-46bc-8c61-be8929bbf93d");
    }
}