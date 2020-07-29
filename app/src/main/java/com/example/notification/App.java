package com.example.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class App extends Application {
    public static final String CHANNEL_ID2="myChannel";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel2 = new NotificationChannel(
                CHANNEL_ID2,
                "Channel 2",
                NotificationManager.IMPORTANCE_LOW
        );
        channel2.setDescription("This is My channel 2");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel2);

    }
}
