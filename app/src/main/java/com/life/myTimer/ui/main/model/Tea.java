package com.life.myTimer.ui.main.model;

public class Tea {
    private Subject.Tea tea;
    private int time;

    Tea(Subject.Tea tea, int time) {
        this.time = time;
    }

    public Subject.Tea getTea() {
        return tea;
    }

    public void setTea(Subject.Tea tea) {
        this.tea = tea;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
