package com.lingda.gamble.model;

public enum SingleBetCategory {
    DA("大"), XIAO("小"), DAN("单"), SHUANG("双"), LON("龙"), HU("虎");

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    SingleBetCategory(String category) {
        this.category = category;

    }
}
