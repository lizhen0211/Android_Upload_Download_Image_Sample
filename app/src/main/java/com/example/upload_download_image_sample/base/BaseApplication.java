package com.example.upload_download_image_sample.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
