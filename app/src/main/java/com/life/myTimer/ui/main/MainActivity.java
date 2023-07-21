package com.life.myTimer.ui.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.life.myTimer.App;
import com.life.myTimer.R;
import com.life.myTimer.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    int minute = 60;
    int second = 1;
    private ArrayList<String> eggSize = new ArrayList<>();
    private ArrayList<String> eggTime = new ArrayList<>();
    private ArrayList<String> stakeSize = new ArrayList<>();
    private ArrayList<String> stakeTime = new ArrayList<>();
    private ValueAnimator animator;
    private int bottomSheetHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        binding.setViewModel(viewModel);

        startObserve();

        binding.clBottomSheet.post(() -> bottomSheetHeight = binding.clBottomSheet.getHeight());

        binding.ivSetting.setOnClickListener(v -> {
            boolean isShow = binding.clBottomSheet.getHeight() == 0 || binding.clBottomSheet.getVisibility() == View.INVISIBLE || binding.clBottomSheet.getVisibility() == View.GONE;
            int beforeHeight;
            int afterHeight;
            if (isShow) {
                beforeHeight = 0;
                afterHeight = bottomSheetHeight;
            } else {
                beforeHeight = bottomSheetHeight;
                afterHeight = 0;
            }

            animateBottomSheetHeight(isShow, beforeHeight, afterHeight);
        });
    }

    @SuppressLint("DefaultLocale")
    private void startObserve() {
        viewModel.selectedSubject.observe(this, subject -> {
            viewModel.setupTimer();
        });

        viewModel.time.observe(this, time -> {
            int minute = time / 60;
            int second = time % 60;
            binding.tvRemainTime.setText(String.format("%02d : %02d", minute, second));
        });
    }

    private void animateBottomSheetHeight(boolean isShow, int beforeHeight, int afterHeight) {
        animator = ValueAnimator.ofInt(beforeHeight, afterHeight);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            binding.clBottomSheet.getLayoutParams().height = (int) animation.getAnimatedValue();
            binding.clBottomSheet.requestLayout();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(@NonNull Animator animation) {}
            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}

            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                if (isShow) {
                    binding.clBottomSheet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (!isShow) {
                    binding.clBottomSheet.setVisibility(View.INVISIBLE);
                }
            }
        });
        animator.start();
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
