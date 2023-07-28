package com.life.myTimer.ui.main;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.life.myTimer.App;
import com.life.myTimer.R;
import com.life.myTimer.ui.main.model.KindOfFood;
import com.life.myTimer.ui.main.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainViewModel extends ViewModel {

    private String[] eggSizes = App.getInstance().getResources().getStringArray(R.array.eggSize);

    private int[] softBoiledColdEggTimes = App.getInstance().getResources().getIntArray(R.array.softBoiledColdEggTime);
    private int[] softBoiledEggTimes = App.getInstance().getResources().getIntArray(R.array.softBoiledEggTime);

    private int[] middleBoiledColdEggTimes = App.getInstance().getResources().getIntArray(R.array.middleBoiledColdEggTime);
    private int[] middleBoiledEggTimes = App.getInstance().getResources().getIntArray(R.array.middleBoiledEggTime);

    private int[] hardBoiledColdEggTimes = App.getInstance().getResources().getIntArray(R.array.hardBoiledColdEggTime);
    private int[] hardBoiledEggTimes = App.getInstance().getResources().getIntArray(R.array.hardBoiledEggTime);

    private String[] stakeSizes = App.getInstance().getResources().getStringArray(R.array.stakeSize);


    private int[] rareColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.rareColdStakeTime);
    private int[] rareStakeTimes = App.getInstance().getResources().getIntArray(R.array.rareStakeTime);

    private int[] mediumColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumColdStakeTime);
    private int[] mediumStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumStakeTime);

    private int[] weldonColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.weldonColdStakeTime);
    private int[] weldonStakeTimes = App.getInstance().getResources().getIntArray(R.array.weldonStakeTime);

    private int[] teaTimes = App.getInstance().getResources().getIntArray(R.array.teaTime);

    private MutableLiveData<Subject> _selectedSubject = new MutableLiveData<>(Subject.EGG);
    public LiveData<Subject> selectedSubject = _selectedSubject;

    private MutableLiveData<Boolean> _isColdFood = new MutableLiveData<>(true);
    public LiveData<Boolean> isColdFood = _isColdFood;

    private MutableLiveData<List<String>> _foodSizeList = new MutableLiveData<>(Arrays.asList(eggSizes));
    public LiveData<List<String>> foodSizeList = _foodSizeList;


    private MutableLiveData<Integer> _clickSelectedFoodSize = new MutableLiveData<>(0);
    public LiveData<Integer> clickSelectedFoodSize = _clickSelectedFoodSize;

    private MutableLiveData<Integer> _selectedFoodSizeIndex = new MutableLiveData<>(0);
    public LiveData<Integer> selectedFoodSizeIndex = _selectedFoodSizeIndex;

    private MutableLiveData<List<KindOfFood>> _kindOfFoodList = new MutableLiveData<>();
    public LiveData<List<KindOfFood>> kindOfFoodList = _kindOfFoodList;

    private MutableLiveData<Integer> _foodImage = new MutableLiveData<>();
    public LiveData<Integer> foodImage = _foodImage;

    private int selectedKindOfFoodIndex = 0;

    private MutableLiveData<Integer> _time = new MutableLiveData<>();
    public LiveData<Integer> time = _time;

    private MutableLiveData<Object> _ringSongForFlipStake = new MutableLiveData<>();
    public LiveData<Object> ringSongForFlipStake = _ringSongForFlipStake;

    private Timer timer = new Timer();


    public MainViewModel() {
        updateKindOfFoodList(Subject.EGG);
        setupTimer();
    }

    public void initializeData() {
        _isColdFood.setValue(true);
        _selectedFoodSizeIndex.setValue(0);
        selectedKindOfFoodIndex = 0;
    }

    public Subject getSelectedSubject() {
        return _selectedSubject.getValue();
    }

    public int getTime() {
        return _time.getValue();
    }

    public void updateSelectedSubject(Subject subject) {
        initializeData();

        updateFoodSizes(subject);
        updateKindOfFoodList(subject);

        _selectedSubject.setValue(subject);
        setupTimer();
    }

    public void updateFoodSizes(Subject subject) {
        List<String> foodSizeList;

        if (subject.getName().equals(Subject.EGG.getName())) {
            foodSizeList = Arrays.asList(eggSizes);
        } else if (subject.getName().equals(Subject.STAKE.getName())) {
            foodSizeList = Arrays.asList(stakeSizes);
        } else {
            foodSizeList = new ArrayList<>();
        }
        _foodSizeList.setValue(foodSizeList);
    }


    public void onClickSelectedFoodSizePosition(int index) {
        _clickSelectedFoodSize.setValue(index);
    }
    public void updateSelectedFoodSizePosition(int index) {
        _selectedFoodSizeIndex.setValue(index);
        setupTimer();
    }

    public void updateColdFood(boolean isColdFood) {
        _isColdFood.setValue(isColdFood);
        setupTimer();
    }

    public void updateKindOfFoodList(Subject subject) {
        ArrayList<KindOfFood> kindOfFoodList = new ArrayList<>();

        if (subject.getName().equals(Subject.EGG.getName())) {
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.SOFT_BOILED, R.drawable.img_egg_soft_boiled, 0,true));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.MIDDLE_BOILED, R.drawable.img_egg_middle_boiled, 0,false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.HARD_BOILED,  R.drawable.img_egg_hard_boiled, 0,false));
        } else if (subject.getName().equals(Subject.STAKE.getName())) {
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.RARE, R.drawable.img_stake_blue_rare, R.string.blue_rare_guide, true));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.MEDIUM, R.drawable.img_stake_rare,R.string.rare_guide, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.WELDON, R.drawable.img_stake_medium, R.string.medium_guide, false));
        } else {
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.BLACK_TEA, R.drawable.img_black_tea,0,true));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.GREEN_TEA, R.drawable.img_green_tea,0,false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.HUB_TEA, R.drawable.img_hub_tea,0,false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.WHITE_TEA, R.drawable.img_black_tea,0,false));
        }

        _kindOfFoodList.setValue(kindOfFoodList);
    }

    public void updateSelectedKindOfFood(int index, int imageRes) {
        selectedKindOfFoodIndex = index;
        _foodImage.setValue(imageRes);
        setupTimer();
    }

    public void setupTimer() {
        timer.cancel();

        Subject subject = _selectedSubject.getValue();
        if (subject != null) {
            if (subject.getName().equals(Subject.EGG.getName())) {
                _time.setValue(getEggTime());
            } else if (subject.getName().equals(Subject.STAKE.getName())) {
                _time.setValue(getStakeTime());
            } else if (subject.getName().equals(Subject.TEA.getName())) {
                _time.setValue(getTeaTime());
            }
        }
    }

    private int getEggTime() {
        KindOfFood kindOfFood = _kindOfFoodList.getValue().get(selectedKindOfFoodIndex);
        int currentEggSizeIndex = _selectedFoodSizeIndex.getValue();
        boolean isCold = _isColdFood.getValue();

        int time;
        if (kindOfFood.getKindOfFood() == Subject.KindOfFood.SOFT_BOILED) {
            if (isCold) {
                time = softBoiledColdEggTimes[currentEggSizeIndex];
            } else {
                time = softBoiledEggTimes[currentEggSizeIndex];
            }
        } else if (kindOfFood.getKindOfFood() == Subject.KindOfFood.MIDDLE_BOILED){
            if (isCold) {
                time = middleBoiledColdEggTimes[currentEggSizeIndex];
            } else {
                time = middleBoiledEggTimes[currentEggSizeIndex];
            }
        } else {
            if (isCold) {
                time = hardBoiledColdEggTimes[currentEggSizeIndex];
            } else {
                time = hardBoiledEggTimes[currentEggSizeIndex];
            }
        }
        return time;
    }

    private int getStakeTime() {
        KindOfFood kindOfFood = _kindOfFoodList.getValue().get(selectedKindOfFoodIndex);
        int currentStakeSizeIndex = _selectedFoodSizeIndex.getValue();
        boolean isCold = _isColdFood.getValue();


        int time;
        if (kindOfFood.getKindOfFood() == Subject.KindOfFood.RARE) {
            if (isCold) {
                time = rareColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = rareStakeTimes[currentStakeSizeIndex];
            }
        } else if (kindOfFood.getKindOfFood() == Subject.KindOfFood.MEDIUM){
            if (isCold) {
                time = mediumColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = mediumStakeTimes[currentStakeSizeIndex];
            }
        }  else {
            if (isCold) {
                time = weldonColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = weldonStakeTimes[currentStakeSizeIndex];
            }
        }
        return time;
    }

    private int getTeaTime() {
        return teaTimes[selectedKindOfFoodIndex];
    }

    public int getStakeFlipTime() {
        return getStakeTime() / 2;
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();

            setupTimer();
        }
    }

    public void startTimer(boolean isResume) {
        Subject subject = selectedSubject.getValue();
        boolean isNeedRinging = false;
        int ringingTime = getStakeFlipTime();
        if (subject != null) {
            if (subject.getName().equals(Subject.EGG.getName())) {
                _foodImage.setValue(R.drawable.img_egg_cook);
            } else if (subject.getName().equals(Subject.STAKE.getName())){
                isNeedRinging = true;
                _foodImage.setValue(R.drawable.img_stake_cook);
            } else {
                _foodImage.setValue(R.drawable.img_tea_cook);
            }

            timer.cancel();
            timer = new Timer();
            boolean finalIsNeedRinging = isNeedRinging;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        int time  = _time.getValue();
                        if (time - 1 >= 0) {
                            _time.postValue(time - 1);

                            if (finalIsNeedRinging && time - 1 == ringingTime) {
                                _ringSongForFlipStake.postValue(new Object());
                                pauseTimer();
                            }

                            if (time <= 0) {
                                this.cancel();
                                timer.cancel();
                                timer.purge();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            timer.schedule(timerTask, 1000, 1000);
        }
    }

    @Override
    protected void onCleared() {
        timer.cancel();
        super.onCleared();
    }
}
