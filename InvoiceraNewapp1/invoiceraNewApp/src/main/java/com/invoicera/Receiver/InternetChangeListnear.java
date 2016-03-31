package com.invoicera.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.invoicera.BackgroundServices.SendDataToServer;

/**
 * Created by Parvesh on 10/7/15.
 */
public class InternetChangeListnear extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SendDataToServer.class);
        context.startService(i);

    }
}
