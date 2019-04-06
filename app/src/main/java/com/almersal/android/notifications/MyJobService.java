package com.almersal.android.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.almersal.android.R;
import com.almersal.android.activities.MainActivity;
import com.crashlytics.android.Crashlytics;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    private static final int NOTIFY_ID = 0;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        try {

            Bundle bundle = jobParameters.getExtras();
            final String title = bundle.getString("title");
            final String body = bundle.getString("body");
            String url = bundle.getString("url");
//
//        Handler handler = new Handler(getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                sendNotification(title, body);
//            }
//        });

            //sendNotification(title, body);
            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sendNotification(title, body);
                }
            });
        } catch (Exception ex) {
            Crashlytics.log(ex.getLocalizedMessage());
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void sendNotification(String title, final String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,TAG)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        final Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        // notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        // startForeground(0, notificationBuilder.build());
        notificationManager.notify(2 /* ID of notification */, notificationBuilder.build());

    }

    private void sendMessage(String theTitle, String theMessage, String theTicker) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(NOTIFY_ID),
                    theTitle, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(theMessage);
            if (getSystemService(NotificationManager.class) != null) {
                getSystemService(NotificationManager.class).createNotificationChannel(channel);
            }
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, String.valueOf(NOTIFY_ID))
                        .setContentIntent(contentIntent)
                        .setContentTitle(theTitle)
                        .setContentText(theMessage)
                        .setWhen(System.currentTimeMillis())
                        .setTicker(theTicker)
                        .setOngoing(true)
                        .setChannelId(String.valueOf(NOTIFY_ID))
                        .setSmallIcon(R.mipmap.ic_launcher);
//                        .setLargeIcon((((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher, null)).getBitmap()))
//                        .addAction(R.drawable.ic_stop_white_24dp, getBaseContext().getString(R.string.Stop), pendingIntentStop);


        Notification notification = notificationBuilder.build();

        startForeground(NOTIFY_ID, notification);
    }

}
