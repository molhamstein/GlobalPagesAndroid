/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.almersal.android.notifications;

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
import com.almersal.android.data.entities.NotificationEntity;
import com.crashlytics.android.Crashlytics;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            sendNotification(remoteMessage);

        } catch (Exception ex) {
            Crashlytics.log(ex.getLocalizedMessage());
        }

    }


    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob(Bundle bundle) {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .setExtras(bundle)
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }


    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        Bundle extras = new Bundle();
        if (remoteMessage.getData() != null) {
            extras.putString("volumeId", remoteMessage.getData().get("volumeId"));
            extras.putString("marketProductId", remoteMessage.getData().get("marketProductId"));
            extras.putString("businessId", remoteMessage.getData().get("businessId"));
            extras.putString("jobId", remoteMessage.getData().get("jobId"));
            extras.putString("adId", remoteMessage.getData().get("adId"));
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String NOTIFY_ID = "1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(NOTIFY_ID),
                    title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(messageBody);
            if (getBaseContext().getSystemService(NotificationManager.class) != null) {
                getBaseContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
            }
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, TAG)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setChannelId(NOTIFY_ID)
                .setExtras(extras)
                .setContentIntent(pendingIntent);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                } catch (Exception ex) {
                    Crashlytics.log(ex.getLocalizedMessage());
                }
            }
        });

    }
}
