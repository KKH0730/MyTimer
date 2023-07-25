package com.life.myTimer.ui.main.model;

import com.life.myTimer.ui.main.model.Subject;

public class KindOfFood {
    private Subject.KindOfFood kindOfFood;
    private boolean isSelect;


    public KindOfFood(Subject.KindOfFood kindOfFood, boolean isSelect) {
        this.kindOfFood = kindOfFood;
        this.isSelect = isSelect;
    }

    public Subject.KindOfFood getKindOfFood() {
        return kindOfFood;
    }

    public void setKindOfFood(Subject.KindOfFood kindOfFood) {
        this.kindOfFood = kindOfFood;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
