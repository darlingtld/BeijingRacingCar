package com.lingda.gamble.param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static Integer smpChip = 5;
    private static Integer smpLevels = 7;
    private static Boolean smpEnabled = false;
    private static List<Integer> smpLevelChips = Arrays.asList(5, 17, 57, 193, 572);

    public static List<Integer> getSmpLevelChips() {
        return smpLevelChips;
    }

    public static void setSmpLevelChips(List<Integer> smpLevelChips) {
        Config.smpLevelChips = smpLevelChips;
    }

    private static Integer firstSecondChip = 5;
    private static Boolean firstSecondEnabled = false;
    private static List<Integer> firstSecondExcludeNumbers = new ArrayList<>();
    private static List<Integer> firstSecondLevelAccList = Arrays.asList(5, 17, 57, 193, 572);

    private static Integer thirdFourthChip = 5;
    private static Boolean thirdFourthEnabled = false;
    private static List<Integer> thirdFourthExcludeNumbers = new ArrayList<>();
    private static List<Integer> thirdFourthLevelAccList = Arrays.asList(5, 17, 57, 193, 572);

    private static Integer fifthSixthChip = 5;
    private static Boolean fifthSixthEnabled = false;
    private static List<Integer> fifthSixthExcludeNumbers = new ArrayList<>();
    private static List<Integer> fifthSixthLevelAccList = Arrays.asList(5, 17, 57, 193, 572);

    private static Integer seventhEighthChip = 5;
    private static Boolean seventhEighthEnabled = false;
    private static List<Integer> seventhEighthExcludeNumbers = new ArrayList<>();
    private static List<Integer> seventhEighthLevelAccList = Arrays.asList(5, 17, 57, 193, 572);

    private static Integer ninethTenthChip = 5;
    private static Boolean ninethTenthEnabled = false;
    private static List<Integer> ninethTenthExcludeNumbers = new ArrayList<>();
    private static List<Integer> ninethTenthLevelAccList = Arrays.asList(5, 17, 57, 193, 572);

    private static Integer lostThreshold = 10000;

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

    public static Integer getSmpChip() {
        return smpChip;
    }

    public static void setSmpChip(Integer smpChip) {
        Config.smpChip = smpChip;
    }

    public static Integer getSmpLevels() {
        return smpLevels;
    }

    public static void setSmpLevels(Integer smpLevels) {
        Config.smpLevels = smpLevels;
    }

    public static Boolean getSmpEnabled() {
        return smpEnabled;
    }

    public static void setSmpEnabled(Boolean smpEnabled) {
        Config.smpEnabled = smpEnabled;
    }

    public static Integer getFirstSecondChip() {
        return firstSecondChip;
    }

    public static void setFirstSecondChip(Integer firstSecondChip) {
        Config.firstSecondChip = firstSecondChip;
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

    public static Integer getThirdFourthChip() {
        return thirdFourthChip;
    }

    public static void setThirdFourthChip(Integer thirdFourthChip) {
        Config.thirdFourthChip = thirdFourthChip;
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

    public static Integer getFifthSixthChip() {
        return fifthSixthChip;
    }

    public static void setFifthSixthChip(Integer fifthSixthChip) {
        Config.fifthSixthChip = fifthSixthChip;
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

    public static Integer getSeventhEighthChip() {
        return seventhEighthChip;
    }

    public static void setSeventhEighthChip(Integer seventhEighthChip) {
        Config.seventhEighthChip = seventhEighthChip;
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

    public static Integer getNinethTenthChip() {
        return ninethTenthChip;
    }

    public static void setNinethTenthChip(Integer ninethTenthChip) {
        Config.ninethTenthChip = ninethTenthChip;
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
