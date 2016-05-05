package com.baculsoft.gcm.simple.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.baculsoft.gcm.simple.App;
import com.baculsoft.gcm.simple.R;
import com.baculsoft.gcm.simple.view.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public class SimpleGcmListenerService extends GcmListenerService {
    private static final String TAG = SimpleGcmListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.w(TAG, "onMessageReceived");
        String message = data.getString("message");
        sendNotification(message);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(App.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(App.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(App.getContext());
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("New Message");
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}