package com.example.homeautomationsystem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.server.response.FastParser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // Duration in milliseconds
            private void alertDisplayer(String title,String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this)
              .setTitle(title)
              .setMessage(message)
              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.cancel();
                              }
            });
     AlertDialog ok = builder.create();
     ok.show();
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         // Calling the cloud code function
        String userId = "XJD1LGWmk4";
        String message = "Hello, World!";

// Set Cloud Code function parameters
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("message", message);

// Call Cloud Code function asynchronously
        ParseCloud.callFunctionInBackground("sendPushNotification", params, (result, e) -> {
            if (e == null) {
                // Cloud Code function executed successfully
                Log.d("Cloud Code", "Push notification sent successfully!");
                Toast.makeText(this, "Push notification Success", Toast.LENGTH_SHORT).show();
            } else {
                // Cloud Code function execution failed
                Log.e("Cloud Code", "Failed to send push notification: " + e.getMessage());
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, SetupActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
