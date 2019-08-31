package com.lingda.gamble.param;

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
    private static Integer firstSecondSmartDetectRoundNumber = 2;
    private static Integer firstSecondMaxBetCount = 7;
    private static List<Integer> firstSecondExcludeNumbers = new ArrayList<>();
    private static List<Integer> firstSecondLevelAccList = Collections.singletonList(initialChip);
    private static List<String> firstSecondSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean thirdFourthEnabled = false;
    private static Boolean thirdFourthSmartMode = false;
    private static Integer thirdFourthSmartDetectRoundNumber = 2;
    private static Integer thirdFourthMaxBetCount = 7;
    private static List<Integer> thirdFourthExcludeNumbers = new ArrayList<>();
    private static List<Integer> thirdFourthLevelAccList = Collections.singletonList(initialChip);
    private static List<String> thirdFourthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean fifthSixthEnabled = false;
    private static Boolean fifthSixthSmartMode = false;
    private static Integer fifthSixthSmartDetectRoundNumber = 2;
    private static Integer fifthSixthMaxBetCount = 7;
    private static List<Integer> fifthSixthExcludeNumbers = new ArrayList<>();
    private static List<Integer> fifthSixthLevelAccList = Collections.singletonList(initialChip);
    private static List<String> fifthSixthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean seventhEighthEnabled = false;
    private static Boolean seventhEighthSmartMode = false;
    private static Integer seventhEighthSmartDetectRoundNumber = 2;
    private static Integer seventhEighthMaxBetCount = 7;
    private static List<Integer> seventhEighthExcludeNumbers = new ArrayList<>();
    private static List<Integer> seventhEighthLevelAccList = Collections.singletonList(initialChip);
    private static List<String> seventhEighthSmartSwitch = Arrays.asList("1,3,5", "6,8,10");

    private static Boolean ninethTenthEnabled = false;
    private static Boolean ninethTenthSmartMode = false;
    private static Integer ninethTenthSmartDetectRoundNumber = 2;
    private static Integer ninethTenthMaxBetCount = 7;
    private static List<Integer> ninethTenthExcludeNumbers = new ArrayList<>();
    private static List<Integer> ninethTenthLevelAccList = Collections.singletonList(initialChip);
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

    public static void setFirstSecondStrategyMode(StrategyMode  firstSecondStrategyMode) {
        Config.firstSecondStrategyMode = firstSecondStrategyMode;
    }

    public static Boolean getThirdFourthSmartMode() {
        return thirdFourthSmartMode;
    }

    public static void setThirdFourthSmartMode(Boolean thirdFourthSmartMode) {
        Config.thirdFourthSmartMode = thirdFourthSmartMode;
    }

    public static Boolean getFifthSixthSmartMode() {
        return fifthSixthSmartMode;
    }

    public static void setFifthSixthSmartMode(Boolean fifthSixthSmartMode) {
        Config.fifthSixthSmartMode = fifthSixthSmartMode;
    }

    public static Boolean getSeventhEighthSmartMode() {
        return seventhEighthSmartMode;
    }

    public static void setSeventhEighthSmartMode(Boolean seventhEighthSmartMode) {
        Config.seventhEighthSmartMode = seventhEighthSmartMode;
    }

    public static Boolean getNinethTenthSmartMode() {
        return ninethTenthSmartMode;
    }

    public static void setNinethTenthSmartMode(Boolean ninethTenthSmartMode) {
        Config.ninethTenthSmartMode = ninethTenthSmartMode;
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
}
