package com.example.homeautomationsystem;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.ArrayList;
import java.util.Map;



public class App extends Application {
    public static final String CHANNEL_ID = "my_channel_id";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        // In MainActivity.java or your Application class
        FirebaseApp.initializeApp(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
    );
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
          installation.put("GCMSenderId", getString(R.string.gcm_defaultSenderId));
            installation.saveInBackground();
        ParsePush.subscribeInBackground("your_channel", e -> {
            if (e == null) {
                Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
            } else {
                Log.e("com.parse.push", "failed to subscribe for push", e);
            }
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
