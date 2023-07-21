package com.life.myTimer.ui.main;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.life.myTimer.App;
import com.life.myTimer.R;
import com.life.myTimer.ui.main.model.Egg;
import com.life.myTimer.ui.main.model.Subject;

import java.util.Objects;

import kotlinx.coroutines.flow.StateFlow;

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

    private MutableLiveData<Integer> _selectedEggSizeIndex = new MutableLiveData<>(0);
    public LiveData<Integer> selectedEggSizeIndex = _selectedEggSizeIndex;

    private MutableLiveData<Integer> _selectedStakeSizeIndex = new MutableLiveData<>(0);
    public LiveData<Integer> selectedStakeSizeIndex = _selectedStakeSizeIndex;

    private MutableLiveData<Subject.EggCookDegree> _selectedEggCookDegree = new MutableLiveData(Subject.EggCookDegree.SOFT_BOILED);
    public LiveData<Subject.EggCookDegree> selectedEggCookDegree = _selectedEggCookDegree;

    private MutableLiveData<Subject.StakeCookDegree> _selectedStakeCookDegree = new MutableLiveData(Subject.StakeCookDegree.BLUE_RARE);
    public LiveData<Subject.StakeCookDegree> selectedStakeCookDegree = _selectedStakeCookDegree;

    private MutableLiveData<Integer> _selectedTeaIndex = new MutableLiveData(0);
    public LiveData<Integer> selectedTeaIndex = _selectedTeaIndex;

    private MutableLiveData<Integer> _time = new MutableLiveData<>();
    public LiveData<Integer> time = _time;



    public void initializeData() {
        _isColdFood.setValue(true);
        _selectedEggSizeIndex.setValue(0);
        _selectedStakeSizeIndex.setValue(0);

        _selectedEggCookDegree.setValue(Subject.EggCookDegree.SOFT_BOILED);
        _selectedStakeCookDegree.setValue(Subject.StakeCookDegree.BLUE_RARE);
        _selectedTeaIndex.setValue(0);
    }

    public void updateSelectedSubject(Subject subject) {
        initializeData();

        _selectedSubject.setValue(subject);
        setupTimer();
    }

    public void updateSelectedEggSizePosition(int index) {
        _selectedEggSizeIndex.setValue(index);
        setupTimer();
    }

    public void updateSelectedStakeSizePosition(int index) {
        _selectedStakeSizeIndex.setValue(index);
        setupTimer();
    }

    public void updateColdFood(boolean isColdFood) {
        _isColdFood.setValue(isColdFood);
        setupTimer();
    }

    public void updateSelectedEggDegree(Subject.EggCookDegree eggCookDegree) {
        _selectedEggCookDegree.setValue(eggCookDegree);
        setupTimer();
    }

    public void updateSelectedStateDegree(Subject.StakeCookDegree stakeCookDegree) {
        _selectedStakeCookDegree.setValue(stakeCookDegree);
        setupTimer();
    }

    public void updateSelectedTeaPosition(int index) {
        _selectedTeaIndex.setValue(index);
        setupTimer();
    }

    public void setupTimer() {
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
        Subject.EggCookDegree eggCookDegree = _selectedEggCookDegree.getValue();
        int currentEggSizeIndex = _selectedEggSizeIndex.getValue();
        boolean isCold = _isColdFood.getValue();

        int time;
        if (eggCookDegree == Subject.EggCookDegree.SOFT_BOILED) {
            if (isCold) {
                time = softBoiledColdEggTimes[currentEggSizeIndex];
            } else {
                time = softBoiledEggTimes[currentEggSizeIndex];
            }
        } else if (eggCookDegree == Subject.EggCookDegree.MIDDLE_BOILED){
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
        Subject.StakeCookDegree stakeCookDegree = _selectedStakeCookDegree.getValue();
        int currentEggSizeIndex = _selectedStakeSizeIndex.getValue();
        boolean isCold = _isColdFood.getValue();


        int time;
        if (stakeCookDegree == Subject.StakeCookDegree.BLUE_RARE) {
            if (isCold) {
                time = blueRareColdStakeTimes[currentEggSizeIndex];
            } else {
                time = blueRareStakeTimes[currentEggSizeIndex];
            }
        } else if (stakeCookDegree == Subject.StakeCookDegree.RARE){
            if (isCold) {
                time = rareColdStakeTimes[currentEggSizeIndex];
            } else {
                time = rareStakeTimes[currentEggSizeIndex];
            }
        } else if (stakeCookDegree == Subject.StakeCookDegree.MEDIUM_RARE){
            if (isCold) {
                time = mediumRareColdStakeTimes[currentEggSizeIndex];
            } else {
                time = mediumRareStakeTimes[currentEggSizeIndex];
            }
        } else if (stakeCookDegree == Subject.StakeCookDegree.MEDIUM){
            if (isCold) {
                time = mediumColdStakeTimes[currentEggSizeIndex];
            } else {
                time = mediumStakeTimes[currentEggSizeIndex];
            }
        } else if (stakeCookDegree == Subject.StakeCookDegree.MEDIUM_WELDON){
            if (isCold) {
                time = mediumWeldonColdStakeTimes[currentEggSizeIndex];
            } else {
                time = mediumWeldonStakeTimes[currentEggSizeIndex];
            }
        } else {
            if (isCold) {
                time = weldonColdStakeTimes[currentEggSizeIndex];
            } else {
                time = weldonStakeTimes[currentEggSizeIndex];
            }
        }
        return time;
    }

    private int getTeaTime() {
        int selectedTeaIndex = _selectedTeaIndex.getValue();
        return teaTimes[selectedTeaIndex];
    }
}
