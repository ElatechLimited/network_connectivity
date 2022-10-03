package com.wificonnect.wificonnect.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionState extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();

        // Check internet connection and accrding to state change the
        // text of activity by calling method
        if (networkInfo != null && networkInfo.isConnected()) {


        } else {

        }
    }
}
