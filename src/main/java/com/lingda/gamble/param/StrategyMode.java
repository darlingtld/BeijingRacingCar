package com.lingda.gamble.param;

public enum StrategyMode {

    SMART("SMART"),
    BASIC("BASIC"),
    /**
     * 開了對子後，過20期以後才又開相同對子。讓5把後進場連打15關
     * (每關注碼為上一把的1.1倍，例如第一把100第二把就110)。
     *
     * 例：第36期開了號碼9的對子，如果一直到56期以上才又開9對，開了之後之後第6把進場。
     */
    PAIR("PAIR"),
    DISABLED("DISABLED");

    private String mode;

    StrategyMode(String mode) {
        this.mode = mode;
    }
}
