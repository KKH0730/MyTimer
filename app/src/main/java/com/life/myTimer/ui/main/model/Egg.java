package com.life.myTimer.ui.main.model;

public class Egg {
    private Subject.EggCookDegree degree;
    private String size;
    private int time;
    private boolean isCold;

    public Egg(Subject.EggCookDegree degree, String size, int time, boolean isCold) {
        this.degree = degree;
        this.size = size;
        this.time = time;
        this.isCold = isCold;
    }

    public Subject.EggCookDegree getDegree() {
        return degree;
    }

    public void setDegree(Subject.EggCookDegree degree) {
        this.degree = degree;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isCold() {
        return isCold;
    }

    public void setCold(boolean cold) {
        isCold = cold;
    }
}