package com.life.myTimer.ui.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.life.myTimer.R;
import com.life.myTimer.databinding.ActivityMainBinding;
import com.life.myTimer.databinding.LayoutOneButtonDialogBinding;
import com.life.myTimer.databinding.LayoutTwoButtonDialogBinding;
import com.life.myTimer.ui.main.adapter.KindOfFoodAdapter;
import com.life.myTimer.ui.main.model.KindOfFood;
import com.life.myTimer.ui.main.adapter.FoodSizeAdapter;
import com.life.myTimer.ui.main.model.Subject;
import com.life.myTimer.utils.DimensionUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    int minute = 60;
    int second = 1;
    private ValueAnimator animator;
    private int bottomSheetHeight = 0;
    private SnapHelper snapHelper = new LinearSnapHelper();
    private AlertDialog alertDialog;
    private Ringtone ringtone;

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
        setKindOfFoodRecyclerView();
    }

    @SuppressLint("DefaultLocale")
    private void startObserve() {
        viewModel.selectedSubject.observe(this, subject -> {

            setTimerStartButton(false);
            controlTabIndicator(subject.getName());

            if (subject.getName().equals(Subject.EGG.getName())) {
                binding.ivSettingGuideBubble.setVisibility(View.VISIBLE);
                binding.ivArrow.setVisibility(View.VISIBLE);
                binding.ivFood.setImageResource(R.drawable.img_egg_soft_boiled);
            } else if (subject.getName().equals(Subject.STAKE.getName())) {
                binding.ivSettingGuideBubble.setVisibility(View.VISIBLE);
                binding.ivArrow.setVisibility(View.VISIBLE);
                binding.ivFood.setImageResource(R.drawable.img_stake_blue_rare);
            } else if (subject.getName().equals(Subject.TEA.getName())){
                boolean isShow = binding.clBottomSheet.getHeight() == 0 || binding.clBottomSheet.getVisibility() == View.INVISIBLE || binding.clBottomSheet.getVisibility() == View.GONE;
                if (!isShow) {
                    onClickShowBottomSheet();
                }
                binding.ivSettingGuideBubble.setVisibility(View.INVISIBLE);
                binding.ivArrow.setVisibility(View.INVISIBLE);
                binding.ivFood.setImageResource(R.drawable.img_black_tea);
            }
        });

        viewModel.isColdFood.observe(this, isCold -> {
            binding.tvCold.setBackgroundColor(getColor(R.color.color_CBCBCB));
            binding.tvNotCold.setBackgroundColor(getColor(R.color.color_CBCBCB));

            if (isCold) {
                binding.tvCold.setBackgroundColor(getColor(R.color.color_4466DE));
            } else {
                binding.tvNotCold.setBackgroundColor(getColor(R.color.color_4466DE));
            }
        });

        viewModel.kindOfFoodList.observe(this, kindOfFoods -> {
            RecyclerView.Adapter adapter = binding.rvKindOfFood.getAdapter();
            if (adapter instanceof KindOfFoodAdapter) {
                ((KindOfFoodAdapter) adapter).submitList(kindOfFoods);
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
                Subject subject = viewModel.getSelectedSubject();
                if (subject != null) {
                    playRingtone();

                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    LayoutOneButtonDialogBinding layoutOneButtonDialogBinding = LayoutOneButtonDialogBinding.inflate(LayoutInflater.from(MainActivity.this));
                    alertDialog = showAlertDialog(layoutOneButtonDialogBinding.getRoot());
                    alertDialog.show();

                    layoutOneButtonDialogBinding.tvTitle.setText(getString(R.string.timer_complete_title));
                    layoutOneButtonDialogBinding.tvContent.setText(getString(R.string.timer_complete_content));
                    layoutOneButtonDialogBinding.tvConfirm.setText(getString(R.string.confirm));
                    layoutOneButtonDialogBinding.tvConfirm.setOnClickListener(v -> {
                        stopRingtone();

                        RecyclerView.Adapter adapter = binding.rvKindOfFood.getAdapter();
                        if (adapter instanceof KindOfFoodAdapter) {
                            KindOfFoodAdapter kindOfFoodAdapter = (KindOfFoodAdapter) adapter;
                            int currentPosition = getCurrentKindOfFoodPosition();
                            viewModel.updateSelectedKindOfFood(currentPosition, kindOfFoodAdapter.getCurrentList().get(currentPosition).getFoodImageResource());
                            viewModel.setupTimer();
                            setTimerStartButton(false);
                        }
                        alertDialog.dismiss();
                    });
                }
            } else if (time < 0) {
                return;
            }

            int minute = time / 60;
            int second = time % 60;
            binding.tvRemainTime.setText(String.format("%02d : %02d", minute, second));
        });

        viewModel.foodImage.observe(this, imageRes -> binding.ivFood.setImageResource(imageRes));

        viewModel.ringSongForFlipStake.observe(this, object -> {
            playRingtone();
            viewModel.pauseTimer();

            LayoutOneButtonDialogBinding layoutOneButtonDialogBinding = LayoutOneButtonDialogBinding.inflate(LayoutInflater.from(MainActivity.this));
            alertDialog = showAlertDialog(layoutOneButtonDialogBinding.getRoot());
            alertDialog.show();

            layoutOneButtonDialogBinding.tvTitle.setText(getString(R.string.timer_stake_flip_title));
            layoutOneButtonDialogBinding.tvContent.setText(getString(R.string.timer_stake_flip_content));
            layoutOneButtonDialogBinding.tvConfirm.setText(getString(R.string.confirm));
            layoutOneButtonDialogBinding.tvConfirm.setOnClickListener(v -> {
                stopRingtone();
                viewModel.startTimer(true);
                alertDialog.dismiss();
            });
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

    private void setKindOfFoodRecyclerView() {
        KindOfFoodAdapter kindOfFoodAdapterMiddle = new KindOfFoodAdapter(
                new DiffUtil.ItemCallback<KindOfFood>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull KindOfFood oldItem, @NonNull KindOfFood newItem) {
                        return oldItem.getKindOfFood().getName().equals(newItem.getKindOfFood().getName());
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull KindOfFood oldItem, @NonNull KindOfFood newItem) {
                        return false;
                    }
                },
                DimensionUtils.getDeviceWidth()
        );
        binding.rvKindOfFood.setAdapter(kindOfFoodAdapterMiddle);
        binding.rvKindOfFood.setItemAnimator(null);
        snapHelper.attachToRecyclerView(binding.rvKindOfFood);
        binding.rvKindOfFood.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof KindOfFoodAdapter) {
                    KindOfFoodAdapter kindOfAdapter = ((KindOfFoodAdapter) adapter);

                    int currentPosition = getCurrentKindOfFoodPosition();
                    if (currentPosition == 0 || currentPosition == kindOfAdapter.getItemCount() - 1) {
                        if (currentPosition == 0) {
                            binding.tvLeftArrow.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.color_33FFFFFF)));
                        } else {
                            binding.tvRightArrow.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.color_33FFFFFF)));
                        }
                    } else {
                        binding.tvLeftArrow.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.white)));
                        binding.tvRightArrow.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.white)));
                    }

                    List<KindOfFood> kindOfFoodList = ((KindOfFoodAdapter) adapter).getCurrentList();
                    List<KindOfFood> newKindOfFoodList = new ArrayList<>();
                    for (int i = 0; i < kindOfFoodList.size(); i ++) {
                        if (currentPosition == i) {
                            newKindOfFoodList.add(new KindOfFood(kindOfFoodList.get(i).getKindOfFood(), kindOfFoodList.get(i).getFoodImageResource(), true));
                        } else {
                            newKindOfFoodList.add(new KindOfFood(kindOfFoodList.get(i).getKindOfFood(), kindOfFoodList.get(i).getFoodImageResource(), false));
                        }
                    }

                    kindOfAdapter.submitList(newKindOfFoodList, () -> {
                        viewModel.updateSelectedKindOfFood(currentPosition, newKindOfFoodList.get(currentPosition).getFoodImageResource());
                    });
                }
            }
        });
    }

    private int getCurrentKindOfFoodPosition() {
        int position;
        if (binding.rvKindOfFood.getLayoutManager() != null) {
            View view = snapHelper.findSnapView(binding.rvKindOfFood.getLayoutManager());
            if (view != null) {
                int pos = binding.rvKindOfFood.getLayoutManager().getPosition(view);
                position = RecyclerView.NO_POSITION;

                if (position != pos) {
                    position = pos;
                }
            } else {
                position = 0;
            }
        } else {
            position = 0;
        }
        return position;
    }

    private void controlTabIndicator(String subjectName) {
        binding.ivTabEgg.setImageResource(R.drawable.img_tab_egg_disable);
        binding.llEggIndicatorContainer.setVisibility(View.GONE);

        binding.ivTabStake.setImageResource(R.drawable.img_tab_stake_disable);
        binding.llStakeIndicatorContainer.setVisibility(View.GONE);

        binding.ivTabTea.setImageResource(R.drawable.img_tab_tea_disable);
        binding.llTeaIndicatorContainer.setVisibility(View.GONE);

        if (subjectName.equals(Subject.EGG.getName())) {
            binding.ivTabEgg.setImageResource(R.drawable.img_tab_egg);
            binding.llEggIndicatorContainer.setVisibility(View.VISIBLE);
        } else if (subjectName.equals(Subject.STAKE.getName())) {
            binding.ivTabStake.setImageResource(R.drawable.img_tab_stake);
            binding.llStakeIndicatorContainer.setVisibility(View.VISIBLE);
        } else {
            binding.ivTabTea.setImageResource(R.drawable.img_tab_tea);
            binding.llTeaIndicatorContainer.setVisibility(View.VISIBLE);
        }
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
                    binding.ivSettingGuideBubble.setVisibility(View.INVISIBLE);
                    binding.ivArrow.setVisibility(View.INVISIBLE);
                    binding.clBottomSheet.setVisibility(View.VISIBLE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> binding.flBottomSheetTouchGuard.setVisibility(View.VISIBLE),  100);
                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (!isShow) {
                    binding.ivSettingGuideBubble.setVisibility(View.VISIBLE);
                    binding.ivArrow.setVisibility(View.VISIBLE);
                    binding.clBottomSheet.setVisibility(View.INVISIBLE);
                    binding.flBottomSheetTouchGuard.setVisibility(View.GONE);
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

    public void onClickKindOfFoodArrow(boolean isLeft) {
        int currentPosition = getCurrentKindOfFoodPosition();

        int targetPosition = -1;
        if (isLeft) {
            if (currentPosition - 1 < 0) {
                return;
            }
            targetPosition = currentPosition - 1;
        } else {
            KindOfFoodAdapter adapter = (KindOfFoodAdapter) binding.rvKindOfFood.getAdapter();
            if (currentPosition + 1 >= adapter.getCurrentList().size()) {
                return;
            }
            targetPosition = currentPosition + 1;
        }

        RecyclerView.LayoutManager layoutManager = binding.rvKindOfFood.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            View view = layoutManager.findViewByPosition(targetPosition);

            if (view != null) {
                int[] snapDistance = snapHelper.calculateDistanceToFinalSnap(binding.rvKindOfFood.getLayoutManager(), view);

                if (snapDistance != null) {
                    if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                        binding.rvKindOfFood.smoothScrollBy(snapDistance[0], snapDistance[1], null, 500);
                    }
                }
            }
        }
    }

    public void onClickStartTimer() {
        Subject subject = viewModel.getSelectedSubject();
        if (subject != null) {
            LayoutTwoButtonDialogBinding layoutTwoButtonDialogBinding = LayoutTwoButtonDialogBinding.inflate(LayoutInflater.from(MainActivity.this));
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            alertDialog = showAlertDialog(layoutTwoButtonDialogBinding.getRoot());
            alertDialog.show();

            layoutTwoButtonDialogBinding.tvNegative.setText(getString(R.string.cancel));
            layoutTwoButtonDialogBinding.tvPositive.setText(getString(R.string.continue1));

            if (binding.tvStart.getText().equals(getString(R.string.start))) {
                layoutTwoButtonDialogBinding.tvTitle.setText(getString(R.string.timer_start_title));
                if (subject.getName().equals(Subject.EGG.getName())) {
                    layoutTwoButtonDialogBinding.tvContent.setText(getString(R.string.egg_timer_start_content));
                } else if (subject.getName().equals(Subject.STAKE.getName())) {
                    layoutTwoButtonDialogBinding.tvContent.setText(getString(R.string.stake_timer_start_content));
                } else {
                    layoutTwoButtonDialogBinding.tvContent.setText(getString(R.string.tea_timer_start_content));
                }
                layoutTwoButtonDialogBinding.tvNegative.setOnClickListener(v -> alertDialog.dismiss());
                layoutTwoButtonDialogBinding.tvPositive.setOnClickListener(v -> {
                    setTimerStartButton(true);
                    viewModel.startTimer(false);
                    alertDialog.dismiss();
                });
            } else {
                viewModel.pauseTimer();

                layoutTwoButtonDialogBinding.tvTitle.setText(getString(R.string.timer_cancel_title));
                layoutTwoButtonDialogBinding.tvContent.setText(getString(R.string.timer_cancel_content));
                layoutTwoButtonDialogBinding.tvNegative.setOnClickListener(v -> {
                    RecyclerView.Adapter adapter = binding.rvKindOfFood.getAdapter();
                    if (adapter instanceof KindOfFoodAdapter) {
                        KindOfFoodAdapter kindOfFoodAdapter = (KindOfFoodAdapter) adapter;
                        int currentPosition = getCurrentKindOfFoodPosition();
                        viewModel.updateSelectedKindOfFood(currentPosition, kindOfFoodAdapter.getCurrentList().get(currentPosition).getFoodImageResource());
                        viewModel.stopTimer();
                        setTimerStartButton(false);
                    }
                    alertDialog.dismiss();
                });
                layoutTwoButtonDialogBinding.tvPositive.setOnClickListener(v -> {
                    viewModel.startTimer(true);
                    alertDialog.dismiss();
                });
            }
        }
    }

    private AlertDialog showAlertDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void setTimerStartButton(boolean isTimerStart) {
        if (isTimerStart) {
            binding.tvStart.setText(getString(R.string.stop));
            binding.tvStart.setBackgroundColor(getColor(R.color.color_F23C23));
            binding.tvStart.setTextColor(getColor(R.color.white));
        } else {
            binding.tvStart.setText(getString(R.string.start));
            binding.tvStart.setBackgroundColor(getColor(R.color.color_FFFFDB1E));
            binding.tvStart.setTextColor(getColor(R.color.color_283964));
        }
    }

    private void playRingtone() {
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        if (ringtone != null) {
            ringtone.play();
        }
    }

    private void stopRingtone() {
        // 벨소리 중지
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }

    @Override
    protected void onDestroy() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        if (binding != null) {
            binding.unbind();
            binding = null;
        }
        super.onDestroy();
    }
}
