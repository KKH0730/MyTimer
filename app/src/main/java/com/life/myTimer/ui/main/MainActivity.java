package com.life.myTimer.ui.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTimer.App;
import com.life.myTimer.R;
import com.life.myTimer.databinding.ActivityMainBinding;
import com.life.myTimer.ui.main.adapter.FoodSizeAdapter;
import com.life.myTimer.ui.main.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;

import timber.log.Timber;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main, null);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.setActivity(this);
        binding.setViewModel(viewModel);

        startObserve();
        setBottomSheetHeight();
        setFoodSizeRecyclerView();
    }

    @SuppressLint("DefaultLocale")
    private void startObserve() {
        viewModel.selectedSubject.observe(this, subject -> {
            binding.tvSubjectEgg.setTextColor(getColor(R.color.black));
            binding.tvSubjectEgg.setBackgroundColor(getColor(R.color.white));
            binding.tvSubjectStake.setTextColor(getColor(R.color.black));
            binding.tvSubjectStake.setBackgroundColor(getColor(R.color.white));
            binding.tvSubjectTea.setTextColor(getColor(R.color.black));
            binding.tvSubjectTea.setBackgroundColor(getColor(R.color.white));

            if (subject.getName().equals(Subject.EGG.getName())) {
                binding.ivArrow.setVisibility(View.VISIBLE);
                binding.tvSubjectEgg.setTextColor(getColor(R.color.white));
                binding.tvSubjectEgg.setBackgroundColor(getColor(R.color.color_ED8282));
            } else if (subject.getName().equals(Subject.STAKE.getName())) {
                binding.ivArrow.setVisibility(View.VISIBLE);
                binding.tvSubjectStake.setTextColor(getColor(R.color.white));
                binding.tvSubjectStake.setBackgroundColor(getColor(R.color.color_ED8282));
            } else if (subject.getName().equals(Subject.TEA.getName())){
                boolean isShow = binding.clBottomSheet.getHeight() == 0 || binding.clBottomSheet.getVisibility() == View.INVISIBLE || binding.clBottomSheet.getVisibility() == View.GONE;
                if (!isShow) {
                    onClickShowBottomSheet();
                }

                binding.ivArrow.setVisibility(View.GONE);
                binding.tvSubjectTea.setTextColor(getColor(R.color.white));
                binding.tvSubjectTea.setBackgroundColor(getColor(R.color.color_ED8282));
            }
        });

        viewModel.isColdFood.observe(this, isCold -> {
            binding.tvCold.setBackgroundColor(Color.parseColor("#E68D50"));
            binding.tvCold.setTextColor(getColor(R.color.black));
            binding.tvNotCold.setBackgroundColor(Color.parseColor("#E68D50"));
            binding.tvNotCold.setTextColor(getColor(R.color.black));

            if (isCold) {
                binding.tvCold.setBackgroundColor(getColor(R.color.color_ED8282));
                binding.tvCold.setTextColor(getColor(R.color.white));
            } else {
                binding.tvNotCold.setBackgroundColor(getColor(R.color.color_ED8282));
                binding.tvNotCold.setTextColor(getColor(R.color.white));
            }
        });

        viewModel.foodSizeList.observe(this, sizes -> {
            if (sizes.size() > 0) {
                binding.rvFoodSize.setVisibility(View.VISIBLE);
                RecyclerView.Adapter adapter = binding.rvFoodSize.getAdapter();
                if (adapter instanceof FoodSizeAdapter) {
                    ((FoodSizeAdapter) adapter).submitList(sizes);
                }
            } else {
                binding.rvFoodSize.setVisibility(View.INVISIBLE);
            }
        });

        viewModel.time.observe(this, time -> {
            if (time == 0) {
                return;
            }
            int minute = time / 60;
            int second = time % 60;
            binding.tvRemainTime.setText(String.format("%02d : %02d", minute, second));
        });
    }

    private void setBottomSheetHeight() {
        binding.clBottomSheet.post(() -> bottomSheetHeight = binding.clBottomSheet.getHeight());
    }

    private void setFoodSizeRecyclerView() {
        binding.rvFoodSize.setAdapter(
                new FoodSizeAdapter(
                        new DiffUtil.ItemCallback<String>() {
                            @Override
                            public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                                return oldItem.equals(newItem);
                            }

                            @Override
                            public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                                return false;
                            }
                        },
                        viewModel,
                        this
                )
        );
        binding.rvFoodSize.setItemAnimator(null);
    }

    private void animateBottomSheetHeight(boolean isShow, int beforeHeight, int afterHeight) {
        animator = ValueAnimator.ofInt(beforeHeight, afterHeight);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            binding.clBottomSheet.getLayoutParams().height = (int) animation.getAnimatedValue();
            binding.clBottomSheet.requestLayout();

            binding.flAnimatedContainer.getLayoutParams().height = (int) animation.getAnimatedValue();
            binding.flAnimatedContainer.requestLayout();
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

    public void onClickShowBottomSheet() {
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
