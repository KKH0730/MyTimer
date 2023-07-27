package com.life.myTimer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppStateDetector implements Application.ActivityLifecycleCallbacks {
    private App application;
    private Listener listener;
    private AppState appState = AppState.None;
    private boolean isChangingConfigurations = false;
    private int running = 0;

    AppStateDetector(App application, Listener listener) {
        this.application = application;
        this.listener = listener;

        application.registerActivityLifecycleCallbacks(this);
    }

    public void unregisterCallbacks() {
        application.unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++running == 1 && !isChangingConfigurations) {
            appState = AppState.Foreground;
            listener.onBecameForeground();
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {}

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        isChangingConfigurations = activity.isChangingConfigurations();
        if (--running == 0 && !isChangingConfigurations) {
            appState = AppState.Background;
            listener.onBecameBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}

    interface Listener {
        void onBecameForeground();
        void onBecameBackground();
    }

    enum AppState {
        None, Foreground, Background
    }
}
