package com.baculsoft.gcm.simple;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    private static volatile Context CONTEXT;

    public static synchronized Context getContext() {
        return CONTEXT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        Log.i(TAG, "onCreate");
    }
}