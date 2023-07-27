package com.life.myTimer.ui;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.life.myTimer.AppStartReceiver;
import com.life.myTimer.R;
import com.life.myTimer.ui.main.MainActivity;
import com.life.myTimer.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class TimerWorkManager extends Worker {
    private Context context;
    public TimerWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        int time = inputData.getInt("time", -1);
        int flipTime = inputData.getInt("flipTime", - 1);
        if (time != -1) {
            createNotificationBuilder(time, flipTime);
        }
        return new Result.Success();
    }

    @Override
    public void onStopped() {
        stopTimer();
        super.onStopped();
    }

    public void stopTimer() {
        notiTimer.cancel();
        notiTimer.purge();
    }

    final String CHANNEL_ID = "noti_timer_channel";

    private Timer notiTimer = new Timer();

    @SuppressLint("NotificationTrampoline")
    private void createNotificationBuilder(int time, int flipTime) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final int[] timerTime = {time};
        int minute = time / 60;
        int second = time % 60;

        NotificationCompat.Builder builder;
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(createNotificationChannel());

            Intent intent = new Intent(context, AppStartReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(ContextCompat.getColor(context, R.color.color_ED8282))
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(String.format("%02d : %02d", minute, second))
                    .setAutoCancel(true)
                    .setSilent(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);

            builder.setContentText(String.format("%02d : %02d", minute, second));

            notificationManager.notify(Constants.NOTICIATION_ID, builder.build());

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
                    if (timerTime[0] == 0) {
                        builder.setContentText(String.format("%02d : %02d", 0, 0));
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            throw new RuntimeException(e);
                        }

                        this.cancel();
                        notiTimer.cancel();
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
                    notificationManager.notify(Constants.NOTICIATION_ID, builder.build());
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
        return new NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
    }
}
