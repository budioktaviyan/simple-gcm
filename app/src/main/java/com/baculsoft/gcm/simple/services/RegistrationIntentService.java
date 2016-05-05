package com.baculsoft.gcm.simple.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baculsoft.gcm.simple.App;
import com.baculsoft.gcm.simple.R;
import com.baculsoft.gcm.simple.utils.IConstants;
import com.baculsoft.gcm.simple.utils.Preferences;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = RegistrationIntentService.class.getSimpleName();

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(App.getContext());
            String authorizedEntity = App.getContext().getResources().getString(R.string.gcm_defaultSenderId);
            String token = instanceID.getToken(authorizedEntity, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, String.format("GCM Registration Token: %s", token));

            subscribeTopics(token);
            saveRegistrationToken(token, true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            saveRegistrationToken(null, false);
        }

        Intent registrationComplete = new Intent(IConstants.IGcm.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(registrationComplete);
    }

    private void saveRegistrationToken(String token, boolean hasToken) {
        Preferences.setGcmToken(App.getContext(), token, hasToken);
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(App.getContext());

        for (String topic : IConstants.IGcm.TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}