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
import java.util.TimeZone;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(Double.parseDouble("-0.32"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(TimeZone.getDefault());

        System.out.println(new ObjectMapper().writeValueAsString(new ConfigDTO()));
    }
}
