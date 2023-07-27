package com.life.myTimer;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class App extends Application {
    private static App app;
    private AppStateDetector appStateDetector;
    private AppStateDetectListener appStateDetectListener;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        appStateDetector = new AppStateDetector(
                this,
                new AppStateDetector.Listener() {
                    @Override
                    public void onBecameForeground() {
                        if (appStateDetectListener != null) {
                            appStateDetectListener.onForegroundDetected();
                        }
                    }

                    @Override
                    public void onBecameBackground() {
                        if (appStateDetectListener != null) {
                            appStateDetectListener.onBackgroundDetected();
                        }
                    }
                }
        );

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static App getInstance() {
        return app;
    }

    public void setAppStateDetectListener(AppStateDetectListener appStateDetectListener) {
        this.appStateDetectListener = appStateDetectListener;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        appStateDetectListener = null;
        appStateDetector.unregisterCallbacks();
    }
}
