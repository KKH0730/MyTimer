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

    public enum KindOfFood {
        SOFT_BOILED("반숙"), // 반숙
        MIDDLE_BOILED("중숙"), // 중숙
        HARD_BOILED("완숙"), // 완숙

        BLUE_RARE("블루레어"), // 블루레어
        RARE("레어"), // 레어
        MEDIUM("미디움"), // 미디움


        BLACK_TEA("홍차"), // 홍차
        GREEN_TEA("녹차"), // 녹차
        HUB_TEA("허브차"), // 허브차
        WHITE_TEA("백차"), // 백차


        BLANK("공백");

        KindOfFood(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
