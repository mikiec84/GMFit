package com.mcsaatchi.gmfit.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("SERVICE_TAG", "onReceive: Phone is booting up!");
        context.startService(new Intent(context, SensorListener.class));
    }
}
