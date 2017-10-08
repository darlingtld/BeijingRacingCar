package com.lingda.gamble.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingda.gamble.model.ConfigDTO;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.util.Utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        map.put(1, 3);
        map.put(2, 2);
        map.put(3, 1);
        map.put(4, 3);
        map.put(5, 4);

        map = Utils.sortByValue(map, true);
        map.forEach((k, v) -> System.out.println(k + " " + v));
    }
}
