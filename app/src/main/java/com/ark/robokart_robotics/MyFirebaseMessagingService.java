package com.ark.robokart_robotics;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.NotifyAct;
import com.ark.robokart_robotics.Activities.Quiz.DailyQuizActivity;
import com.ark.robokart_robotics.Activities.Quiz.QuizContestActivity;
import com.ark.robokart_robotics.Activities.Splash.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    DBHelper dbHelper;
    SQLiteDatabase database;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
         dbHelper = new DBHelper(this);
         database = dbHelper.getWritableDatabase();
        Log.i("notification recieved",""+remoteMessage.getData());

        if (remoteMessage.getData().get("title").equalsIgnoreCase("Quiz Contest"))
            popupDialog();
        else
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));

    }

    private void popupDialog() {
        Intent intent=new Intent(getApplicationContext(), QuizContestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void insert(String title1, String body1, String time) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.TITLE, title1);
        contentValue.put(DBHelper.BODY, body1);
        contentValue.put(DBHelper.TIME, time);
        database.insert(DBHelper.TABLE_NAME, null, contentValue);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("new token",s);
    }

    private void sendNotification(String title, String
            message) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm aaa", Locale.getDefault());
        String time = sdf.format(new Date());
        insert(title,message,time);
        Intent intent;
        PendingIntent pendingIntent;

            if (title.equals("New Story on Robokart App.")){
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("story","go");

                pendingIntent= PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        0);
            }else if (title.equals("New Doubt on Robokart App.")){
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("post","ok");
                pendingIntent= PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        0);
            }else if (title.equals("Quiz of the Day")){
                intent = new Intent(this, DailyQuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("post","ok");
                pendingIntent= PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        0);
            }
            else {
                intent = new Intent(this, NotifyAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        0);
            }

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = getString(R.string.default_notification_channel_id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.robokart_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.robokart_logo))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(NOTIFICATION_CHANNEL_ID);


        notificationManager.notify(1, notificationBuilder.build());


    }

}
