package com.example.homeautomationsystem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class ParsePushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("alert");
        String imageUrl = intent.getStringExtra("imageUrl");
//        Toast.makeText(context, imageUrl, Toast.LENGTH_SHORT).show();
        // Handle the notification
        if (title != null && message != null) {
            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                    
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Optionally, load and display the image
            // You can use libraries like Picasso or Glide to load the image from the URL
            // For simplicity, this example does not load the image
            Intent resultIntent = new Intent(context, ImageActivity.class);
            resultIntent.putExtra("imageUrl", imageUrl);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            Toast.makeText(context, "Notification Manipulated", Toast.LENGTH_SHORT).show();
            // Show the notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
    }
}
