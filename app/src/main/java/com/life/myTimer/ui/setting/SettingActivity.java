package com.life.myTimer.ui.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.life.myTimer.BuildConfig;
import com.life.myTimer.R;
import com.life.myTimer.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting, null);
        binding.setActivity(this);

        setAppVersion();
    }

    @SuppressLint("SetTextI18n")
    private void setAppVersion() {
        binding.tvAppVersion.setText("v" + BuildConfig.VERSION_NAME);
    }
}
