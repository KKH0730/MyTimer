package com.life.myTimer.ui.intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.life.myTimer.R;
import com.life.myTimer.databinding.ActivityIntroBinding;
import com.life.myTimer.ui.main.MainActivity;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    private final int REQUEST_POST_NOTIFICATIONS = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission();
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(this::navigateToMain, 2000);
        }
    }

    @RequiresApi(api = android.os.Build.VERSION_CODES.TIRAMISU)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.POST_NOTIFICATIONS },
                    REQUEST_POST_NOTIFICATIONS
            );
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(this::navigateToMain, 2000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            navigateToMain();
        }
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (binding != null) {
            binding.unbind();
            binding = null;
        }
        super.onDestroy();
    }
}
