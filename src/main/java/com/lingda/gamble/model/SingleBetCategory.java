package com.lingda.gamble.model;

public enum SingleBetCategory {
    FIRST("一"), SECOND("二"), THIRD("三"), FOURTH("四"), FIFTH("五"), SIXTH("六"), SEVENTH("七"), EIGHTH("八"), NINETH("九"), TENTH("十"), DA("大"), XIAO("小"), DAN("单"), SHUANG("双"), LON("龙"), HU("虎");

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
