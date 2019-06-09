package ru.minilan.vocabularytrainer;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static ru.minilan.vocabularytrainer.MainActivity.MYLOGTAG;

public class CloseNotification extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.i(MYLOGTAG, String.valueOf(getIntent().getExtras().getInt(VTFirebaseInstance.NOTIFICATION_KEY)));

        notificationManager.cancel(getIntent().getIntExtra(VTFirebaseInstance.NOTIFICATION_KEY,-1));

        finish();
    }

    public static PendingIntent getCloseIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, CloseNotification.class);
        intent.putExtra(VTFirebaseInstance.NOTIFICATION_KEY, notificationId);
        Log.i(MYLOGTAG, "getCloseIntent id = "+ intent.getExtras().getInt(VTFirebaseInstance.NOTIFICATION_KEY));
        // PendingIntent.FLAG_UPDATE_CURRENT - needs to update notificationId in PI
        PendingIntent closeIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return closeIntent;
    }
}
