package com.life.myTimer.ui.main.model;

public class KindOfFood {
    private Subject.KindOfFood kindOfFood;
    private int foodImageResource;

    private int guideResource;
    private boolean isSelect;


    public KindOfFood(Subject.KindOfFood kindOfFood, int foodImageResource, int guideResource, boolean isSelect) {
        this.kindOfFood = kindOfFood;
        this.foodImageResource = foodImageResource;
        this.guideResource = guideResource;
        this.isSelect = isSelect;
    }

    public Subject.KindOfFood getKindOfFood() {
        return kindOfFood;
    }

    public void setKindOfFood(Subject.KindOfFood kindOfFood) {
        this.kindOfFood = kindOfFood;
    }

    public int getFoodImageResource() {
        return foodImageResource;
    }

    public void setFoodImageResource(int foodImageResource) {
        this.foodImageResource = foodImageResource;
    }

    public int getGuideResource() {
        return guideResource;
    }

    public void setGuideResource(int guideResource) {
        this.guideResource = guideResource;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
