package com.life.myTimer.ui.main.model;

public class KindOfFood {
    private Subject.KindOfFood kindOfFood;
    private int foodImageResource;
    private boolean isSelect;


    public KindOfFood(Subject.KindOfFood kindOfFood, int foodImageResource, boolean isSelect) {
        this.kindOfFood = kindOfFood;
        this.foodImageResource = foodImageResource;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
