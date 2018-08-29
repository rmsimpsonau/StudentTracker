package com.example.studenttracker.helpers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageButton;

import com.example.studenttracker.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

enum NOTIFICATION_ACTION {
    ADD,
    CANCEL
}

public class NotificationHelper {

    public enum ALERT_STATE {
        ENABLED,
        DISABLED
    }

//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//        System.out.println("Content Title of Notification: " + Objects.requireNonNull(intent.getExtras()).getCharSequence("Content Title"));
//        System.out.println("Content Text of Notification: " + intent.getExtras().getCharSequence("Content Text"));
//        Notification notification  = new Notification.Builder(context)
//                .setCategory(Notification.CATEGORY_MESSAGE)
//                .setContentTitle(Objects.requireNonNull(intent.getExtras()).getCharSequence("Content Title"))
//                .setContentText(Objects.requireNonNull(intent.getExtras()).getCharSequence("Content Text"))
//                .setSmallIcon(R.drawable.ic_add_icon)
//                .setAutoCancel(true)
//                .setVisibility(Notification.VISIBILITY_PUBLIC).build();
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        assert notificationManager != null;
////        generateNotificationId(),
//        notificationManager.notify(1, notification);
//    }

    public static void toggleNotification(Context context, Date dateOfNotification, NOTIFICATION_ACTION action, int requestCode, String contentTitle, String contentText)
    {
        Notification.Builder builder  = new Notification.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_school_icon);

        Intent intent = new Intent(context, NotificationHelper.class);
        PendingIntent activity = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, requestCode);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfNotification);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;

        Calendar currentTime = Calendar.getInstance();
        long delay = calendar.getTimeInMillis() - currentTime.getTimeInMillis();

        if (action == NOTIFICATION_ACTION.ADD)
        {
            System.out.println("delay.getTimeInMillis(): " + delay);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toaster.makeToast("Notification scheduled",context);
        }
        else // Cancel alarm
        {
            alarmManager.cancel(pendingIntent);
            Toaster.makeToast("Notification canceled",context);
        }

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        assert notificationManager != null;
////        generateNotificationId(),
//        notificationManager.
//
//        Intent intent = new Intent(context, NotificationHelper.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context.getApplicationContext(), requestCode, intent, 0);
//
//        intent.putExtra("Content Title", contentTitle);
//        intent.putExtra("Content Text", contentText);
//
//        System.out.println("Here we are...");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dateOfNotification);
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        assert alarmManager != null;
//
//        if (action == NOTIFICATION_ACTION.ADD)
//        {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            Toaster.makeToast("Notification scheduled",context);
//        }
//        else // Cancel alarm
//        {
//            alarmManager.cancel(pendingIntent);
//            Toaster.makeToast("Notification canceled",context);
//        }

    }

//    public static void toggleNotification(Context context, Date dateOfNotification, NOTIFICATION_ACTION action, int requestCode, String contentTitle, String contentText)
//    {
//        Intent intent = new Intent(context, NotificationHelper.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context.getApplicationContext(), requestCode, intent, 0);
//
//        intent.putExtra("Content Title", contentTitle);
//        intent.putExtra("Content Text", contentText);
//
//        System.out.println("Here we are...");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dateOfNotification);
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        assert alarmManager != null;
//
//        if (action == NOTIFICATION_ACTION.ADD)
//        {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            Toaster.makeToast("Notification scheduled",context);
//        }
//        else // Cancel alarm
//        {
//            alarmManager.cancel(pendingIntent);
//            Toaster.makeToast("Notification canceled",context);
//        }
//
//    }

    // Change alert icon to be enabled or disabled
    public static void setAlertIconState(ImageButton btn, ALERT_STATE state)
    {
        switch (state)
        {
            case ENABLED:
                btn.setImageResource(R.drawable.ic_alert_icon);
                btn.setColorFilter(Color.argb(255, 33, 150, 243));
                break;
            case DISABLED:
                btn.setImageResource(R.drawable.ic_alert_disable_icon);
                btn.setColorFilter(Color.argb(255, 170, 170, 170));
                break;
        }
    }

    public static int generateNotificationId()
    {
        Random randomNumber = new Random();
        return randomNumber.nextInt(2147483647);
    }
}
