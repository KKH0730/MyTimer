package com.life.myTimer.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.RequiresApi;

import com.life.myTimer.App;

public class DimensionUtils {

    public static int getDeviceWidth() {
        int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            width = getWindowMetrics().getBounds().width();
        } else {
            width = getWindowDisplay().x;
        }
        return width;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public static WindowMetrics getWindowMetrics() {
        return App.getInstance().getSystemService(WindowManager.class).getMaximumWindowMetrics();
    }

    public static Point getWindowDisplay() {
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }
}
