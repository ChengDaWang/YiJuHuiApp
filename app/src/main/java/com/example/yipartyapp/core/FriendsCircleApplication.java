package com.example.yipartyapp.core;

import android.app.Application;
import android.content.Context;

public class FriendsCircleApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        DataCenter.init();
    }
}
