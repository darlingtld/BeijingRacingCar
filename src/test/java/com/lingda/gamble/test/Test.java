package com.lingda.gamble.test;

import java.time.LocalTime;

public class Test {

    public static void main(String[] args) {
        LocalTime endTime = LocalTime.parse("20:40:00");
        LocalTime now= LocalTime.now();
        System.out.println(endTime.isAfter(now));
    }
}
