package ru.minilan.vocabularytrainer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static ru.minilan.vocabularytrainer.MainActivity.MYLOGTAG;

public class VTFirebaseInstance extends FirebaseMessagingService {

    public static final String NOTIFICATION_KEY = "NOTIFICATION_KEY1";
    private String messageFrom, messageTitle, messageBody;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        messageFrom = remoteMessage.getFrom();
        if ((messageTitle = remoteMessage.getNotification().getTitle()) == null) {
            messageTitle = "no title";
        }
        messageBody = remoteMessage.getNotification().getBody();

        Log.i(MYLOGTAG, messageFrom);
        Log.i(MYLOGTAG, messageTitle);
        Log.i(MYLOGTAG, messageBody);

        showNotification();

    }

    private void showNotification() {
        int NOTIFICATION_ID = new Random().nextInt(35);
        Log.i(MYLOGTAG, String.valueOf(NOTIFICATION_ID));


        PendingIntent pi1 = CloseNotification.getCloseIntent(NOTIFICATION_ID,getBaseContext());

        Intent downloadIntent = new Intent();
        PendingIntent pi2 = PendingIntent.getActivity(getBaseContext(), 1, downloadIntent, 0);


        Notification.Action action1 = new Notification.Action(R.drawable.ic_close_black_24dp, "Close", pi1);
        // stub
        Notification.Action action2 = new Notification.Action(R.drawable.ic_cloud_download_black_24dp, "Download", pi2);

        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext());
        notificationBuilder
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .addAction(action1)
                .addAction(action2);
        Notification notification = notificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
