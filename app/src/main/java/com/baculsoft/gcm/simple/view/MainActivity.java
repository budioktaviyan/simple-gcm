package com.baculsoft.gcm.simple.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baculsoft.gcm.simple.App;
import com.baculsoft.gcm.simple.R;
import com.baculsoft.gcm.simple.services.RegistrationIntentService;
import com.baculsoft.gcm.simple.utils.IConstants;
import com.baculsoft.gcm.simple.utils.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public TextView mTvMessage;
    public Button mBtnSend;

    private boolean isReceiverRegistered;
    private String mSenderId;
    private AtomicInteger mMessageId;
    private BroadcastReceiver mBroadcastReceiver;
    private GoogleCloudMessaging mGoogleCloudMessaging;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageId = new AtomicInteger();
        mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(this);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mBtnSend = (Button) findViewById(R.id.btn_send);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String message;

                        try {
                            Bundle bundle = new Bundle();
                            bundle.putString("title", "New Client Message");
                            bundle.putString("message", "Halo GCM Client!");
                            String id = Integer.toString(mMessageId.incrementAndGet());
                            mSenderId = App.getContext().getResources().getString(R.string.gcm_defaultSenderId);
                            mGoogleCloudMessaging.send(mSenderId.concat("@gcm.googleapis.com"), id, bundle);
                            message = "Sent Message";
                        } catch (IOException e) {
                            message = "Error Sent Message";
                            Log.e(TAG, e.getMessage(), e);
                        }

                        return message;
                    }

                    @Override
                    protected void onPostExecute(String message) {
                        mTvMessage.append(message.concat("\n"));
                    }
                }.execute(null, null, null);
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean hasToken = Preferences.getGcmTokenStatus(App.getContext());
                String message = Preferences.getGcmToken(App.getContext());
                Log.i(TAG, "onReceive");

                if (hasToken && !TextUtils.isEmpty(message)) {
                    Log.i(TAG, message);
                } else {
                    Log.e(TAG, "Token failed to save!");
                }
            }
        };
        registerReceiver();

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        unregisterReceiver();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(IConstants.IGcm.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9000).show();
            } else {
                Log.e(TAG, "This device is not supported.");
                finish();
            }

            return false;
        }

        return true;
    }
}