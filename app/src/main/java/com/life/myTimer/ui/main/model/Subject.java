package com.life.myTimer.ui.main.model;

public enum Subject {
    EGG("egg"), STAKE("stake"), TEA("tea");

    private String name;


    Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum EggCookDegree {
        SOFT_BOILED, // 반숙
        MIDDLE_BOILED, // 중숙
        HARD_BOILED; // 완숙
    }

    public enum StakeCookDegree {
        BLUE_RARE, // 블루레어
        RARE, // 레어
        MEDIUM_RARE, // 미디움 레어
        MEDIUM, // 미디움
        MEDIUM_WELDON, // 미디움 웰던
        WELDON; // 웰던
    }

    public enum Tea {
        BLACK_TEA, // 홍차
        GREEN_TEA, // 녹차
        HUB_TEA, // 허브차
        WHITE_TEA; // 백차
    }
}
