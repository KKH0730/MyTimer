package com.life.myTimer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import timber.log.Timber;

public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static App getInstance() {
        return app;
    }
}
