package com.lingda.gamble.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingda.gamble.model.ConfigDTO;
import com.lingda.gamble.param.Config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {
        List<Integer> list = Arrays.asList(1,12,4,0,0);
        System.out.println(list.stream().filter(n->n>0).count());
    }
}
