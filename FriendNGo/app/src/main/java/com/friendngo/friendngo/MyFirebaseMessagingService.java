package com.friendngo.friendngo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by krishna on 2017-04-26.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = "FCM Service";
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // TODO: Handle FCM messages here.
            // If the application is in the foreground handle both data and notification messages here.
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated.
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

            // Create Notification
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this)
//                    .setSound(alarmSound)
                    .setSmallIcon(R.drawable.icon_mapa)
                    .setContentTitle("Message")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
//                    .setLights(Color.RED,300,300)
                    .setDefaults(
                            Notification.DEFAULT_SOUND
                                    | Notification.DEFAULT_VIBRATE
                                    | Notification.DEFAULT_LIGHTS
                    )
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1410, notificationBuilder.build());
        }


}
