package com.lingda.gamble.util;

import com.lingda.gamble.model.RankSingleBet;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isReversed) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort((e1, e2) -> {
            if (isReversed) {
                return (e2.getValue()).compareTo(e1.getValue());
            } else {
                return (e1.getValue()).compareTo(e2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static LinkedHashMap<Integer, Integer> convertMap(LinkedHashMap<Integer, AtomicInteger> map) {
        LinkedHashMap<Integer, Integer> convertedMap = new LinkedHashMap<>();
        map.forEach((key, value) -> convertedMap.put(key, value.intValue()));
        return convertedMap;
    }

    public static boolean detectStepIntegerList(Integer smartDetectRoundNumber, List<Integer> stepIntegerList1, List<Integer> stepIntegerList2, Integer lastLotteryResult, Integer lotteryResult2, Integer lotteryResult3, Integer lotteryResult4) {
        switch (smartDetectRoundNumber) {
            case 2:
                return stepIntegerList1.contains(lastLotteryResult) && stepIntegerList2.contains(lotteryResult2);
            case 3:
                return stepIntegerList1.contains(lastLotteryResult) && stepIntegerList2.contains(lotteryResult2) && stepIntegerList1.contains(lotteryResult3);
            case 4:
                return stepIntegerList1.contains(lastLotteryResult) && stepIntegerList2.contains(lotteryResult2) && stepIntegerList1.contains(lotteryResult3) && stepIntegerList2.contains(lotteryResult4);
        }
        return false;
    }
}
