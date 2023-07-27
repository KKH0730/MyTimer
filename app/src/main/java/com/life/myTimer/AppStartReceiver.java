package com.life.myTimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.life.myTimer.ui.main.MainActivity;

public class AppStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntent);
    }
}

