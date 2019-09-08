package com.lingda.gamble.param;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {
    private static Integer initialChip = 1;
    private static Boolean smpEnabled = false;
    private static Boolean smpDaXiao = true;
    private static Boolean smpDanShuang = false;
    private static List<Integer> smpLevelChips = Collections.singletonList(initialChip);

    private static Boolean firstSecondEnabled = false;

    private static StrategyMode firstSecondStrategyMode = StrategyMode.PAIR;
    private static Integer firstSecondPairModeDetectRoundNumber = 8;
    private static Integer firstSecondGapRoundsForConsecutiveNumbers = 5;

    private static Integer firstSecondSmartDetectRoundNumber = 2;
    private static Integer firstSecondMaxBetCount = 7;
    private static List<Integer> firstSecondExcludeNumbers = new ArrayList<>();
    private static List<Integer> firstSecondLevelAccList = Lists.newArrayList(initialChip, initialChip + 1, initialChip + 2, initialChip + 3, initialChip + 4, initialChip + 5, initialChip + 6);
    private static List<String> firstSecondSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean thirdFourthEnabled = false;

    private static StrategyMode thirdFourthStrategyMode = StrategyMode.PAIR;
    private static Integer thirdFourthPairModeDetectRoundNumber = 8;
    private static Integer thirdFourthGapRoundsForConsecutiveNumbers = 5;
    
    private static Integer thirdFourthSmartDetectRoundNumber = 2;
    private static Integer thirdFourthMaxBetCount = 7;
    private static List<Integer> thirdFourthExcludeNumbers = new ArrayList<>();
    private static List<Integer> thirdFourthLevelAccList = Lists.newArrayList(initialChip, initialChip + 1, initialChip + 2, initialChip + 3, initialChip + 4, initialChip + 5, initialChip + 6);;
    private static List<String> thirdFourthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean fifthSixthEnabled = false;
    
    private static StrategyMode fifthSixthStrategyMode = StrategyMode.PAIR;
    private static Integer fifthSixthPairModeDetectRoundNumber = 8;
    private static Integer fifthSixthGapRoundsForConsecutiveNumbers = 5;
    
    private static Integer fifthSixthSmartDetectRoundNumber = 2;
    private static Integer fifthSixthMaxBetCount = 7;
    private static List<Integer> fifthSixthExcludeNumbers = new ArrayList<>();
    private static List<Integer> fifthSixthLevelAccList = Lists.newArrayList(initialChip, initialChip + 1, initialChip + 2, initialChip + 3, initialChip + 4, initialChip + 5, initialChip + 6);;
    private static List<String> fifthSixthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean seventhEighthEnabled = false;

    private static StrategyMode seventhEighthStrategyMode = StrategyMode.PAIR;
    private static Integer seventhEighthPairModeDetectRoundNumber = 8;
    private static Integer seventhEighthGapRoundsForConsecutiveNumbers = 5;
    
    private static Integer seventhEighthSmartDetectRoundNumber = 2;
    private static Integer seventhEighthMaxBetCount = 7;
    private static List<Integer> seventhEighthExcludeNumbers = new ArrayList<>();
    private static List<Integer> seventhEighthLevelAccList = Lists.newArrayList(initialChip, initialChip + 1, initialChip + 2, initialChip + 3, initialChip + 4, initialChip + 5, initialChip + 6);;
    private static List<String> seventhEighthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean ninethTenthEnabled = false;

    private static StrategyMode ninethTenthStrategyMode = StrategyMode.PAIR;
    private static Integer ninethTenthPairModeDetectRoundNumber = 8;
    private static Integer ninethTenthGapRoundsForConsecutiveNumbers = 5;
    
    private static Integer ninethTenthSmartDetectRoundNumber = 2;
    private static Integer ninethTenthMaxBetCount = 7;
    private static List<Integer> ninethTenthExcludeNumbers = new ArrayList<>();
    private static List<Integer> ninethTenthLevelAccList = Lists.newArrayList(initialChip, initialChip + 1, initialChip + 2, initialChip + 3, initialChip + 4, initialChip + 5, initialChip + 6);;
    private static List<String> ninethTenthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");
    private static String email = "";
    private static Integer lostThreshold = 10000;
    private static Integer winThreshold = 5000;

    public static Integer getWinThreshold() {
        return winThreshold;
    }

    public static void setWinThreshold(Integer winThreshold) {
        Config.winThreshold = winThreshold;
    }

    public static Integer getFirstSecondMaxBetCount() {
        return firstSecondMaxBetCount;
    }

    public static void setFirstSecondMaxBetCount(Integer firstSecondMaxBetCount) {
        Config.firstSecondMaxBetCount = firstSecondMaxBetCount;
    }

    public static Integer getThirdFourthMaxBetCount() {
        return thirdFourthMaxBetCount;
    }

    public static void setThirdFourthMaxBetCount(Integer thirdFourthMaxBetCount) {
        Config.thirdFourthMaxBetCount = thirdFourthMaxBetCount;
    }

    public static Integer getFifthSixthMaxBetCount() {
        return fifthSixthMaxBetCount;
    }

    public static void setFifthSixthMaxBetCount(Integer fifthSixthMaxBetCount) {
        Config.fifthSixthMaxBetCount = fifthSixthMaxBetCount;
    }

    public static Integer getSeventhEighthMaxBetCount() {
        return seventhEighthMaxBetCount;
    }

    public static void setSeventhEighthMaxBetCount(Integer seventhEighthMaxBetCount) {
        Config.seventhEighthMaxBetCount = seventhEighthMaxBetCount;
    }

    public static Integer getNinethTenthMaxBetCount() {
        return ninethTenthMaxBetCount;
    }

    public static void setNinethTenthMaxBetCount(Integer ninethTenthMaxBetCount) {
        Config.ninethTenthMaxBetCount = ninethTenthMaxBetCount;
    }

    public static Integer getFirstSecondSmartDetectRoundNumber() {
        return firstSecondSmartDetectRoundNumber;
    }

    public static void setFirstSecondSmartDetectRoundNumber(Integer firstSecondSmartDetectRoundNumber) {
        Config.firstSecondSmartDetectRoundNumber = firstSecondSmartDetectRoundNumber;
    }

    public static Integer getThirdFourthSmartDetectRoundNumber() {
        return thirdFourthSmartDetectRoundNumber;
    }

    public static void setThirdFourthSmartDetectRoundNumber(Integer thirdFourthSmartDetectRoundNumber) {
        Config.thirdFourthSmartDetectRoundNumber = thirdFourthSmartDetectRoundNumber;
    }

    public static Integer getFifthSixthSmartDetectRoundNumber() {
        return fifthSixthSmartDetectRoundNumber;
    }

    public static void setFifthSixthSmartDetectRoundNumber(Integer fifthSixthSmartDetectRoundNumber) {
        Config.fifthSixthSmartDetectRoundNumber = fifthSixthSmartDetectRoundNumber;
    }

    public static Integer getSeventhEighthSmartDetectRoundNumber() {
        return seventhEighthSmartDetectRoundNumber;
    }

    public static void setSeventhEighthSmartDetectRoundNumber(Integer seventhEighthSmartDetectRoundNumber) {
        Config.seventhEighthSmartDetectRoundNumber = seventhEighthSmartDetectRoundNumber;
    }

    public static Integer getNinethTenthSmartDetectRoundNumber() {
        return ninethTenthSmartDetectRoundNumber;
    }

    public static void setNinethTenthSmartDetectRoundNumber(Integer ninethTenthSmartDetectRoundNumber) {
        Config.ninethTenthSmartDetectRoundNumber = ninethTenthSmartDetectRoundNumber;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Config.email = email;
    }

    public static List<String> getFirstSecondSmartSwitch() {
        return firstSecondSmartSwitch;
    }

    public static void setFirstSecondSmartSwitch(List<String> firstSecondSmartSwitch) {
        Config.firstSecondSmartSwitch = firstSecondSmartSwitch;
    }

    public static List<String> getThirdFourthSmartSwitch() {
        return thirdFourthSmartSwitch;
    }

    public static void setThirdFourthSmartSwitch(List<String> thirdFourthSmartSwitch) {
        Config.thirdFourthSmartSwitch = thirdFourthSmartSwitch;
    }

    public static List<String> getFifthSixthSmartSwitch() {
        return fifthSixthSmartSwitch;
    }

    public static void setFifthSixthSmartSwitch(List<String> fifthSixthSmartSwitch) {
        Config.fifthSixthSmartSwitch = fifthSixthSmartSwitch;
    }

    public static List<String> getSeventhEighthSmartSwitch() {
        return seventhEighthSmartSwitch;
    }

    public static void setSeventhEighthSmartSwitch(List<String> seventhEighthSmartSwitch) {
        Config.seventhEighthSmartSwitch = seventhEighthSmartSwitch;
    }

    public static List<String> getNinethTenthSmartSwitch() {
        return ninethTenthSmartSwitch;
    }

    public static void setNinethTenthSmartSwitch(List<String> ninethTenthSmartSwitch) {
        Config.ninethTenthSmartSwitch = ninethTenthSmartSwitch;
    }

    public static StrategyMode getFirstSecondStrategyMode() {
        return firstSecondStrategyMode;
    }

    public static void setFirstSecondStrategyMode(StrategyMode firstSecondStrategyMode) {
        Config.firstSecondStrategyMode = firstSecondStrategyMode;
    }
    public static Boolean getSmpDaXiao() {
        return smpDaXiao;
    }

    public static void setSmpDaXiao(Boolean smpDaXiao) {
        Config.smpDaXiao = smpDaXiao;
    }

    public static Boolean getSmpDanShuang() {
        return smpDanShuang;
    }

    public static void setSmpDanShuang(Boolean smpDanShuang) {
        Config.smpDanShuang = smpDanShuang;
    }

    public static List<Integer> getSmpLevelChips() {
        return smpLevelChips;
    }

    public static void setSmpLevelChips(List<Integer> smpLevelChips) {
        Config.smpLevelChips = smpLevelChips;
    }

    public static List<Integer> getFirstSecondLevelAccList() {
        return firstSecondLevelAccList;
    }

    public static void setFirstSecondLevelAccList(List<Integer> firstSecondLevelAccList) {
        Config.firstSecondLevelAccList = firstSecondLevelAccList;
    }

    public static List<Integer> getThirdFourthLevelAccList() {
        return thirdFourthLevelAccList;
    }

    public static void setThirdFourthLevelAccList(List<Integer> thirdFourthLevelAccList) {
        Config.thirdFourthLevelAccList = thirdFourthLevelAccList;
    }

    public static List<Integer> getFifthSixthLevelAccList() {
        return fifthSixthLevelAccList;
    }

    public static void setFifthSixthLevelAccList(List<Integer> fifthSixthLevelAccList) {
        Config.fifthSixthLevelAccList = fifthSixthLevelAccList;
    }

    public static List<Integer> getSeventhEighthLevelAccList() {
        return seventhEighthLevelAccList;
    }

    public static void setSeventhEighthLevelAccList(List<Integer> seventhEighthLevelAccList) {
        Config.seventhEighthLevelAccList = seventhEighthLevelAccList;
    }

    public static List<Integer> getNinethTenthLevelAccList() {
        return ninethTenthLevelAccList;
    }

    public static void setNinethTenthLevelAccList(List<Integer> ninethTenthLevelAccList) {
        Config.ninethTenthLevelAccList = ninethTenthLevelAccList;
    }

    public static Integer getLostThreshold() {
        return lostThreshold;
    }

    public static void setLostThreshold(Integer lostThreshold) {
        Config.lostThreshold = lostThreshold;
    }


    public static Boolean getSmpEnabled() {
        return smpEnabled;
    }

    public static void setSmpEnabled(Boolean smpEnabled) {
        Config.smpEnabled = smpEnabled;
    }


    public static Boolean getFirstSecondEnabled() {
        return firstSecondEnabled;
    }

    public static void setFirstSecondEnabled(Boolean firstSecondEnabled) {
        Config.firstSecondEnabled = firstSecondEnabled;
    }

    public static List<Integer> getFirstSecondExcludeNumbers() {
        return firstSecondExcludeNumbers;
    }

    public static void setFirstSecondExcludeNumbers(List<Integer> firstSecondExcludeNumbers) {
        Config.firstSecondExcludeNumbers = firstSecondExcludeNumbers;
    }


    public static Boolean getThirdFourthEnabled() {
        return thirdFourthEnabled;
    }

    public static void setThirdFourthEnabled(Boolean thirdFourthEnabled) {
        Config.thirdFourthEnabled = thirdFourthEnabled;
    }

    public static List<Integer> getThirdFourthExcludeNumbers() {
        return thirdFourthExcludeNumbers;
    }

    public static void setThirdFourthExcludeNumbers(List<Integer> thirdFourthExcludeNumbers) {
        Config.thirdFourthExcludeNumbers = thirdFourthExcludeNumbers;
    }


    public static Boolean getFifthSixthEnabled() {
        return fifthSixthEnabled;
    }

    public static void setFifthSixthEnabled(Boolean fifthSixthEnabled) {
        Config.fifthSixthEnabled = fifthSixthEnabled;
    }

    public static List<Integer> getFifthSixthExcludeNumbers() {
        return fifthSixthExcludeNumbers;
    }

    public static void setFifthSixthExcludeNumbers(List<Integer> fifthSixthExcludeNumbers) {
        Config.fifthSixthExcludeNumbers = fifthSixthExcludeNumbers;
    }

    public static Boolean getSeventhEighthEnabled() {
        return seventhEighthEnabled;
    }

    public static void setSeventhEighthEnabled(Boolean seventhEighthEnabled) {
        Config.seventhEighthEnabled = seventhEighthEnabled;
    }

    public static List<Integer> getSeventhEighthExcludeNumbers() {
        return seventhEighthExcludeNumbers;
    }

    public static void setSeventhEighthExcludeNumbers(List<Integer> seventhEighthExcludeNumbers) {
        Config.seventhEighthExcludeNumbers = seventhEighthExcludeNumbers;
    }


    public static Boolean getNinethTenthEnabled() {
        return ninethTenthEnabled;
    }

    public static void setNinethTenthEnabled(Boolean ninethTenthEnabled) {
        Config.ninethTenthEnabled = ninethTenthEnabled;
    }

    public static List<Integer> getNinethTenthExcludeNumbers() {
        return ninethTenthExcludeNumbers;
    }

    public static void setNinethTenthExcludeNumbers(List<Integer> ninethTenthExcludeNumbers) {
        Config.ninethTenthExcludeNumbers = ninethTenthExcludeNumbers;
    }


    public static Integer getFirstSecondPairModeDetectRoundNumber() {
        return firstSecondPairModeDetectRoundNumber;
    }

    public static void setFirstSecondPairModeDetectRoundNumber(Integer firstSecondPairModeDetectRoundNumber) {
        Config.firstSecondPairModeDetectRoundNumber = firstSecondPairModeDetectRoundNumber;
    }


    public static Integer getFirstSecondGapRoundsForConsecutiveNumbers() {
        return firstSecondGapRoundsForConsecutiveNumbers;
    }

    public static void setFirstSecondGapRoundsForConsecutiveNumbers(Integer firstSecondGapRoundsForConsecutiveNumbers) {
        Config.firstSecondGapRoundsForConsecutiveNumbers = firstSecondGapRoundsForConsecutiveNumbers;
    }

    public static StrategyMode getThirdFourthStrategyMode() {
        return thirdFourthStrategyMode;
    }

    public static void setThirdFourthStrategyMode(StrategyMode thirdFourthStrategyMode) {
        Config.thirdFourthStrategyMode = thirdFourthStrategyMode;
    }

    public static Integer getThirdFourthPairModeDetectRoundNumber() {
        return thirdFourthPairModeDetectRoundNumber;
    }

    public static void setThirdFourthPairModeDetectRoundNumber(Integer thirdFourthPairModeDetectRoundNumber) {
        Config.thirdFourthPairModeDetectRoundNumber = thirdFourthPairModeDetectRoundNumber;
    }

    public static Integer getThirdFourthGapRoundsForConsecutiveNumbers() {
        return thirdFourthGapRoundsForConsecutiveNumbers;
    }

    public static void setThirdFourthGapRoundsForConsecutiveNumbers(Integer thirdFourthGapRoundsForConsecutiveNumbers) {
        Config.thirdFourthGapRoundsForConsecutiveNumbers = thirdFourthGapRoundsForConsecutiveNumbers;
    }

    public static StrategyMode getFifthSixthStrategyMode() {
        return fifthSixthStrategyMode;
    }

    public static void setFifthSixthStrategyMode(StrategyMode fifthSixthStrategyMode) {
        Config.fifthSixthStrategyMode = fifthSixthStrategyMode;
    }

    public static Integer getFifthSixthPairModeDetectRoundNumber() {
        return fifthSixthPairModeDetectRoundNumber;
    }

    public static void setFifthSixthPairModeDetectRoundNumber(Integer fifthSixthPairModeDetectRoundNumber) {
        Config.fifthSixthPairModeDetectRoundNumber = fifthSixthPairModeDetectRoundNumber;
    }

    public static Integer getFifthSixthGapRoundsForConsecutiveNumbers() {
        return fifthSixthGapRoundsForConsecutiveNumbers;
    }

    public static void setFifthSixthGapRoundsForConsecutiveNumbers(Integer fifthSixthGapRoundsForConsecutiveNumbers) {
        Config.fifthSixthGapRoundsForConsecutiveNumbers = fifthSixthGapRoundsForConsecutiveNumbers;
    }

    public static StrategyMode getSeventhEighthStrategyMode() {
        return seventhEighthStrategyMode;
    }

    public static void setSeventhEighthStrategyMode(StrategyMode seventhEighthStrategyMode) {
        Config.seventhEighthStrategyMode = seventhEighthStrategyMode;
    }

    public static Integer getSeventhEighthPairModeDetectRoundNumber() {
        return seventhEighthPairModeDetectRoundNumber;
    }

    public static void setSeventhEighthPairModeDetectRoundNumber(Integer seventhEighthPairModeDetectRoundNumber) {
        Config.seventhEighthPairModeDetectRoundNumber = seventhEighthPairModeDetectRoundNumber;
    }

    public static Integer getSeventhEighthGapRoundsForConsecutiveNumbers() {
        return seventhEighthGapRoundsForConsecutiveNumbers;
    }

    public static void setSeventhEighthGapRoundsForConsecutiveNumbers(Integer seventhEighthGapRoundsForConsecutiveNumbers) {
        Config.seventhEighthGapRoundsForConsecutiveNumbers = seventhEighthGapRoundsForConsecutiveNumbers;
    }

    public static StrategyMode getNinethTenthStrategyMode() {
        return ninethTenthStrategyMode;
    }

    public static void setNinethTenthStrategyMode(StrategyMode ninethTenthStrategyMode) {
        Config.ninethTenthStrategyMode = ninethTenthStrategyMode;
    }

    public static Integer getNinethTenthPairModeDetectRoundNumber() {
        return ninethTenthPairModeDetectRoundNumber;
    }

    public static void setNinethTenthPairModeDetectRoundNumber(Integer ninethTenthPairModeDetectRoundNumber) {
        Config.ninethTenthPairModeDetectRoundNumber = ninethTenthPairModeDetectRoundNumber;
    }

    public static Integer getNinethTenthGapRoundsForConsecutiveNumbers() {
        return ninethTenthGapRoundsForConsecutiveNumbers;
    }

    public static void setNinethTenthGapRoundsForConsecutiveNumbers(Integer ninethTenthGapRoundsForConsecutiveNumbers) {
        Config.ninethTenthGapRoundsForConsecutiveNumbers = ninethTenthGapRoundsForConsecutiveNumbers;
    }
}
