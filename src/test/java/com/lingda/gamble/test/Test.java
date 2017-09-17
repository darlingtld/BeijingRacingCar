package com.lingda.gamble.test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class Test {

    public static void main(String[] args) {
        System.out.println(Double.parseDouble("-0.32"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(TimeZone.getDefault());
    }
}
