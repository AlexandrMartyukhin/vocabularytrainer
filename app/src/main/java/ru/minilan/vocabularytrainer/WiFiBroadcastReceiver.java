package ru.minilan.vocabularytrainer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WiFiBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(MainActivity.MYLOGTAG,"Wifi onreceive");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.i(MainActivity.MYLOGTAG,"Wifi"+wifi.toString());

        if (wifi.isConnected()) {
            Log.i(MainActivity.MYLOGTAG,"Wifi connected");

            // TO DO -- CAN DOWNLOAD NEW WORDS

        }
    }
}
