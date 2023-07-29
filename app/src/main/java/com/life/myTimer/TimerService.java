package com.life.myTimer;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.life.myTimer.ui.main.MainActivity;
import com.life.myTimer.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("kkhdev"," onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("kkhdev"," onStartCommand");
        if (intent.getAction().equals("start")) {
            doWork(intent);
        } else if (intent.getAction().equals("stop")) {
            stopForeground(true);
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    public void doWork(Intent intent) {
        Log.e("kkhdev"," dowork");
        int time = intent.getIntExtra("time", -1);
        int flipTime = intent.getIntExtra("flipTime", - 1);
        if (time != -1) {
            Log.e("kkhdev","time : " + time);
            createNotificationBuilder(time, flipTime);
        }
    }


    @Override
    public void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    public void stopTimer() {
        notiTimer.cancel();
        notiTimer.purge();
    }

    final String CHANNEL_ID = "noti_timer_channel";

    private Timer notiTimer = new Timer();

    @SuppressLint("NotificationTrampoline")
    private void createNotificationBuilder(int time, int flipTime) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final int[] timerTime = {time};
        int minute = time / 60;
        int second = time % 60;

        NotificationCompat.Builder builder;
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(createNotificationChannel());

            Intent intent = new Intent(this, AppStartReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(ContextCompat.getColor(this, R.color.color_ED8282))
                    .setContentTitle(this.getString(R.string.app_name))
                    .setContentText(String.format("%02d : %02d", minute, second))
                    .setAutoCancel(true)
                    .setSilent(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);

            builder.setContentText(String.format("%02d : %02d", minute, second));

            startForeground(Constants.NOTICIATION_ID, builder.build());
//            notificationManager.notify(Constants.NOTICIATION_ID, builder.build());

            notiTimer.cancel();
            notiTimer = new Timer();
            TimerTask notiTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (MainActivity.blockTimer) {
                        this.cancel();
                        notiTimer.cancel();
                        return;
                    }

//                    Log.e("kkhdev","time : " + String.format("%02d : %02d", timerTime[0] / 60, timerTime[0] % 60));
                    if (timerTime[0] == 0) {
                        builder.setContentText(String.format("%02d : %02d", 0, 0));
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            throw new RuntimeException(e);
                        }

                        this.cancel();
                        notiTimer.cancel();
                        notiTimer.purge();
                        return;
                    } else {
                        timerTime[0] -= 1;

                        if (flipTime != -1 && flipTime == timerTime[0]) {
                            try {
                                pendingIntent.send();
                                pauseTimer();
                            } catch (PendingIntent.CanceledException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        int updatedMinute = timerTime[0] / 60;
                        int updatedSecond = timerTime[0] % 60;
                        builder.setContentText(String.format("%02d : %02d", updatedMinute, updatedSecond));
                    }
//                    notificationManager.notify(Constants.NOTICIATION_ID, builder.build());
                    startForeground(Constants.NOTICIATION_ID, builder.build());
                }
            };
            notiTimer.schedule(notiTimerTask, 0, 1000);
        }
    }

    private void pauseTimer() {
        if (notiTimer != null) {
            notiTimer.cancel();
        }
    }

    private NotificationChannel createNotificationChannel() {
        return new NotificationChannel(CHANNEL_ID, this.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
