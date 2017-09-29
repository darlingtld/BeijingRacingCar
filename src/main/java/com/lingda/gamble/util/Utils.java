package com.lingda.gamble.util;

import com.lingda.gamble.model.RankSingleBet;

public class Utils {

    public static boolean isLastBetWin(Integer lastNumber, RankSingleBet lastSingleBet) {
        switch (lastNumber) {
            case 1:
                return lastSingleBet.getFirst() > 0;
            case 2:
                return lastSingleBet.getSecond() > 0;
            case 3:
                return lastSingleBet.getThird() > 0;
            case 4:
                return lastSingleBet.getFourth() > 0;
            case 5:
                return lastSingleBet.getFifth() > 0;
            case 6:
                return lastSingleBet.getSixth() > 0;
            case 7:
                return lastSingleBet.getSeventh() > 0;
            case 8:
                return lastSingleBet.getEighth() > 0;
            case 9:
                return lastSingleBet.getNineth() > 0;
            case 10:
                return lastSingleBet.getTenth() > 0;
        }
        return false;
    }
}
