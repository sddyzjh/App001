package com.example.saint.app001;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by Saint on 10/7/2016.
 */

public class NotificationBuilder {
    public static void setNotification(Context context)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSmallIcon(R.mipmap.mu);

        Intent intent = new Intent(Constants.CHANGE);
        PendingIntent piPlay = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        intent = new Intent(Constants.LAST);
        PendingIntent piLast = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        intent = new Intent(Constants.NEXT);
        PendingIntent piNext = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification);
//        remoteViews.setOnClickPendingIntent(R.id.notification_playandpause, piPlay);
  //      remoteViews.setOnClickPendingIntent(R.id.notification_last, piLast);
    //    remoteViews.setOnClickPendingIntent(R.id.notification_next, piNext);

        Notification notification = builder.setContent(remoteViews).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, notification);
    }
}
