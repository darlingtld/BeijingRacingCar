package com.lingda.gamble.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            List<Integer> testList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
//            Collections.shuffle(testList);
            System.out.println(testList.stream().max(Integer::compare).get());

            System.out.println(testList);
        }
    }
}
