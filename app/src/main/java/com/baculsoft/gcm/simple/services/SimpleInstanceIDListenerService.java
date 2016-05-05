package com.baculsoft.gcm.simple.services;

import android.content.Intent;
import android.util.Log;

import com.baculsoft.gcm.simple.App;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public class SimpleInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = SimpleInstanceIDListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        Log.w(TAG, "onTokenRefresh");
        Intent intent = new Intent(App.getContext(), RegistrationIntentService.class);
        startService(intent);
    }
}