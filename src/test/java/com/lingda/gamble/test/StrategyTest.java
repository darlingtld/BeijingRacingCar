package com.lingda.gamble.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrategyTest {

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            System.out.println("===== 开奖结果 =====");
//            List<Integer> racingResult = getRacingResult();
//            System.out.println(racingResult);
//            List<Integer> betList = Arrays.asList(1, 1, 1, 1, 1, 1, 1, 0, 0, 0);
//
//        }
        System.out.println(2 << 6);
    }

    private static List<Integer> getRacingResult() {
        List<Integer> result = new ArrayList<>();
        while (true) {
            int i = new Random().nextInt(Integer.MAX_VALUE) % 11;
            if ((i >= 1 && i <= 10) && !result.contains(i)) {
                result.add(i);
                if (result.size() == 10) {
                    return result;
                }
            }
        }
    }
}
