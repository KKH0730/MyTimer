package com.life.myTimer.ui.main;


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

    private int[] blueRareColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.blueRareColdStakeTime);
    private int[] blueRareStakeTimes = App.getInstance().getResources().getIntArray(R.array.blueRareStakeTime);

    private int[] rareColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.rareColdStakeTime);
    private int[] rareStakeTimes = App.getInstance().getResources().getIntArray(R.array.rareStakeTime);

    private int[] mediumRareColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumRareColdStakeTime);
    private int[] mediumRareStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumRareStakeTime);

    private int[] mediumColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumColdStakeTime);
    private int[] mediumStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumStakeTime);

    private int[] mediumWeldonColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumWeldonColdStakeTime);
    private int[] mediumWeldonStakeTimes = App.getInstance().getResources().getIntArray(R.array.mediumWeldonStakeTime);

    private int[] weldonColdStakeTimes = App.getInstance().getResources().getIntArray(R.array.weldonColdStakeTime);
    private int[] weldonStakeTimes = App.getInstance().getResources().getIntArray(R.array.weldonStakeTime);

    private int[] teaTimes = App.getInstance().getResources().getIntArray(R.array.teaTime);

    private MutableLiveData<Subject> _selectedSubject = new MutableLiveData<>(Subject.EGG);
    public LiveData<Subject> selectedSubject = _selectedSubject;

    private MutableLiveData<Boolean> _isColdFood = new MutableLiveData<>(true);
    public LiveData<Boolean> isColdFood = _isColdFood;

    private MutableLiveData<List<String>> _foodSizeList = new MutableLiveData<>(Arrays.asList(eggSizes));
    public LiveData<List<String>> foodSizeList = _foodSizeList;

    private MutableLiveData<Integer> _selectedFoodSizeIndex = new MutableLiveData<>(0);
    public LiveData<Integer> selectedFoodSizeIndex = _selectedFoodSizeIndex;

    private MutableLiveData<List<KindOfFood>> _kindOfFoodList = new MutableLiveData<>();
    public LiveData<List<KindOfFood>> kindOfFoodList = _kindOfFoodList;

    private MutableLiveData<Integer> _selectedKindOfFoodIndex = new MutableLiveData<>(0);
    public LiveData<Integer> selectedKindOfFoodIndex = _selectedKindOfFoodIndex;

    private MutableLiveData<Integer> _time = new MutableLiveData<>();
    public LiveData<Integer> time = _time;

    private Timer timer = new Timer();


    public MainViewModel() {
        updateKindOfFoodList(Subject.EGG);
        setupTimer();
    }

    public void initializeData() {
        _isColdFood.setValue(true);
        _selectedFoodSizeIndex.setValue(0);
        _selectedKindOfFoodIndex.setValue(0);
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
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.SOFT_BOILED, true));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.MIDDLE_BOILED, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.HARD_BOILED, false));
        } else if (subject.getName().equals(Subject.STAKE.getName())) {
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.BLUE_RARE, true));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.RARE, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.MEDIUM_RARE, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.MEDIUM, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.MEDIUM_WELDON, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.WELDON, false));
        } else {
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.BLACK_TEA, true));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.GREEN_TEA, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.HUB_TEA, false));
            kindOfFoodList.add(new KindOfFood(Subject.KindOfFood.WHITE_TEA, false));
        }

        _kindOfFoodList.setValue(kindOfFoodList);
    }

    public void updateSelectedKindOfFood(int index) {
        _selectedKindOfFoodIndex.setValue(index);
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
        KindOfFood kindOfFood = _kindOfFoodList.getValue().get(_selectedKindOfFoodIndex.getValue());
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
        KindOfFood kindOfFood = _kindOfFoodList.getValue().get(_selectedKindOfFoodIndex.getValue());
        int currentStakeSizeIndex = _selectedFoodSizeIndex.getValue();
        boolean isCold = _isColdFood.getValue();


        int time;
        if (kindOfFood.getKindOfFood() == Subject.KindOfFood.BLUE_RARE) {
            if (isCold) {
                time = blueRareColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = blueRareStakeTimes[currentStakeSizeIndex];
            }
        } else if (kindOfFood.getKindOfFood() == Subject.KindOfFood.RARE){
            if (isCold) {
                time = rareColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = rareStakeTimes[currentStakeSizeIndex];
            }
        } else if (kindOfFood.getKindOfFood() == Subject.KindOfFood.MEDIUM_RARE){
            if (isCold) {
                time = mediumRareColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = mediumRareStakeTimes[currentStakeSizeIndex];
            }
        } else if (kindOfFood.getKindOfFood() == Subject.KindOfFood.MEDIUM){
            if (isCold) {
                time = mediumColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = mediumStakeTimes[currentStakeSizeIndex];
            }
        } else if (kindOfFood.getKindOfFood() == Subject.KindOfFood.MEDIUM_WELDON){
            if (isCold) {
                time = mediumWeldonColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = mediumWeldonStakeTimes[currentStakeSizeIndex];
            }
        } else {
            if (isCold) {
                time = weldonColdStakeTimes[currentStakeSizeIndex];
            } else {
                time = weldonStakeTimes[currentStakeSizeIndex];
            }
        }
        return time;
    }

    private int getTeaTime() {
        int selectedTeaIndex = _selectedKindOfFoodIndex.getValue();
        return teaTimes[selectedTeaIndex];
    }

    public void startTimer() {
        timer.cancel();
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    int time  = _time.getValue();
                    if (time - 1 >= 0) {
                        _time.postValue(time - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    protected void onCleared() {
        timer.cancel();
        super.onCleared();
    }
}
