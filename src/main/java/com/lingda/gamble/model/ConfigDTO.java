package com.lingda.gamble.model;


import com.lingda.gamble.param.Config;
import com.lingda.gamble.param.StrategyMode;

import java.util.List;

public class ConfigDTO {
    private Boolean smpEnabled;
    private Boolean smpDaXiao;
    private Boolean smpDanShuang;
    private List<Integer> smpLevelChips;
    private Boolean firstSecondEnabled;
    private StrategyMode firstSecondStrategyMode;
    private  Integer firstSecondPairModeDetectRoundNumber;
    private  Integer firstSecondGapRoundsForConsecutiveNumbers;
    private List<Integer> firstSecondExcludeNumbers;
    private List<Integer> firstSecondLevelAccList;
    private Boolean thirdFourthEnabled;
    private StrategyMode thirdFourthStrategyMode;
    private  Integer thirdFourthPairModeDetectRoundNumber;
    private  Integer thirdFourthGapRoundsForConsecutiveNumbers;
    private List<Integer> thirdFourthExcludeNumbers;
    private List<Integer> thirdFourthLevelAccList;
    private Boolean fifthSixthEnabled;
    private StrategyMode fifthSixthStrategyMode;
    private  Integer fifthSixthPairModeDetectRoundNumber;
    private  Integer fifthSixthGapRoundsForConsecutiveNumbers;
    private List<Integer> fifthSixthExcludeNumbers;
    private List<Integer> fifthSixthLevelAccList;
    private Boolean seventhEighthEnabled;
    private StrategyMode seventhEighthStrategyMode;
    private  Integer seventhEighthPairModeDetectRoundNumber;
    private  Integer seventhEighthGapRoundsForConsecutiveNumbers;
    private List<Integer> seventhEighthExcludeNumbers;
    private List<Integer> seventhEighthLevelAccList;
    private Boolean ninethTenthEnabled;
    private StrategyMode ninethTenthStrategyMode;
    private  Integer ninethTenthPairModeDetectRoundNumber;
    private  Integer ninethTenthGapRoundsForConsecutiveNumbers;
    private List<Integer> ninethTenthExcludeNumbers;
    private List<Integer> ninethTenthLevelAccList;
    private Integer lostThreshold;

    public ConfigDTO() {
        setSmpEnabled(Config.getSmpEnabled());
        setSmpDaXiao(Config.getSmpDaXiao());
        setSmpDanShuang(Config.getSmpDanShuang());
        setSmpLevelChips(Config.getSmpLevelChips());

        setFirstSecondEnabled(Config.getFirstSecondEnabled());
        setFirstSecondStrategyMode(Config.getFirstSecondStrategyMode());
        setFirstSecondExcludeNumbers(Config.getFirstSecondExcludeNumbers());
        setFirstSecondLevelAccList(Config.getFirstSecondLevelAccList());
        setFirstSecondPairModeDetectRoundNumber(Config.getFirstSecondPairModeDetectRoundNumber());
        setFirstSecondGapRoundsForConsecutiveNumbers(Config.getFirstSecondGapRoundsForConsecutiveNumbers());

        setThirdFourthEnabled(Config.getThirdFourthEnabled());
        setThirdFourthStrategyMode(Config.getThirdFourthStrategyMode());
        setThirdFourthExcludeNumbers(Config.getThirdFourthExcludeNumbers());
        setThirdFourthLevelAccList(Config.getThirdFourthLevelAccList());
        setThirdFourthPairModeDetectRoundNumber(Config.getThirdFourthPairModeDetectRoundNumber());
        setThirdFourthGapRoundsForConsecutiveNumbers(Config.getThirdFourthGapRoundsForConsecutiveNumbers());

        setFifthSixthEnabled(Config.getFifthSixthEnabled());
        setFifthSixthStrategyMode(Config.getFifthSixthStrategyMode());
        setFifthSixthExcludeNumbers(Config.getFifthSixthExcludeNumbers());
        setFifthSixthLevelAccList(Config.getFifthSixthLevelAccList());
        setFifthSixthPairModeDetectRoundNumber(Config.getFifthSixthPairModeDetectRoundNumber());
        setFifthSixthGapRoundsForConsecutiveNumbers(Config.getFifthSixthGapRoundsForConsecutiveNumbers());

        setSeventhEighthEnabled(Config.getSeventhEighthEnabled());
        setSeventhEighthStrategyMode(Config.getSeventhEighthStrategyMode());
        setSeventhEighthExcludeNumbers(Config.getSeventhEighthExcludeNumbers());
        setSeventhEighthLevelAccList(Config.getSeventhEighthLevelAccList());
        setSeventhEighthPairModeDetectRoundNumber(Config.getSeventhEighthPairModeDetectRoundNumber());
        setSeventhEighthGapRoundsForConsecutiveNumbers(Config.getSeventhEighthGapRoundsForConsecutiveNumbers());

        setNinethTenthEnabled(Config.getNinethTenthEnabled());
        setNinethTenthStrategyMode(Config.getNinethTenthStrategyMode());
        setNinethTenthExcludeNumbers(Config.getNinethTenthExcludeNumbers());
        setNinethTenthLevelAccList(Config.getNinethTenthLevelAccList());
        setNinethTenthPairModeDetectRoundNumber(Config.getNinethTenthPairModeDetectRoundNumber());
        setNinethTenthGapRoundsForConsecutiveNumbers(Config.getNinethTenthGapRoundsForConsecutiveNumbers());

        setLostThreshold(Config.getLostThreshold());
    }

    public Integer getFirstSecondPairModeDetectRoundNumber() {
        return firstSecondPairModeDetectRoundNumber;
    }

    public void setFirstSecondPairModeDetectRoundNumber(Integer firstSecondPairModeDetectRoundNumber) {
        this.firstSecondPairModeDetectRoundNumber = firstSecondPairModeDetectRoundNumber;
    }

    public Integer getFirstSecondGapRoundsForConsecutiveNumbers() {
        return firstSecondGapRoundsForConsecutiveNumbers;
    }

    public void setFirstSecondGapRoundsForConsecutiveNumbers(Integer firstSecondGapRoundsForConsecutiveNumbers) {
        this.firstSecondGapRoundsForConsecutiveNumbers = firstSecondGapRoundsForConsecutiveNumbers;
    }

    public StrategyMode getThirdFourthStrategyMode() {
        return thirdFourthStrategyMode;
    }

    public void setThirdFourthStrategyMode(StrategyMode thirdFourthStrategyMode) {
        this.thirdFourthStrategyMode = thirdFourthStrategyMode;
    }

    public Integer getThirdFourthPairModeDetectRoundNumber() {
        return thirdFourthPairModeDetectRoundNumber;
    }

    public void setThirdFourthPairModeDetectRoundNumber(Integer thirdFourthPairModeDetectRoundNumber) {
        this.thirdFourthPairModeDetectRoundNumber = thirdFourthPairModeDetectRoundNumber;
    }

    public Integer getThirdFourthGapRoundsForConsecutiveNumbers() {
        return thirdFourthGapRoundsForConsecutiveNumbers;
    }

    public void setThirdFourthGapRoundsForConsecutiveNumbers(Integer thirdFourthGapRoundsForConsecutiveNumbers) {
        this.thirdFourthGapRoundsForConsecutiveNumbers = thirdFourthGapRoundsForConsecutiveNumbers;
    }

    public StrategyMode getFifthSixthStrategyMode() {
        return fifthSixthStrategyMode;
    }

    public void setFifthSixthStrategyMode(StrategyMode fifthSixthStrategyMode) {
        this.fifthSixthStrategyMode = fifthSixthStrategyMode;
    }

    public Integer getFifthSixthPairModeDetectRoundNumber() {
        return fifthSixthPairModeDetectRoundNumber;
    }

    public void setFifthSixthPairModeDetectRoundNumber(Integer fifthSixthPairModeDetectRoundNumber) {
        this.fifthSixthPairModeDetectRoundNumber = fifthSixthPairModeDetectRoundNumber;
    }

    public Integer getFifthSixthGapRoundsForConsecutiveNumbers() {
        return fifthSixthGapRoundsForConsecutiveNumbers;
    }

    public void setFifthSixthGapRoundsForConsecutiveNumbers(Integer fifthSixthGapRoundsForConsecutiveNumbers) {
        this.fifthSixthGapRoundsForConsecutiveNumbers = fifthSixthGapRoundsForConsecutiveNumbers;
    }

    public StrategyMode getSeventhEighthStrategyMode() {
        return seventhEighthStrategyMode;
    }

    public void setSeventhEighthStrategyMode(StrategyMode seventhEighthStrategyMode) {
        this.seventhEighthStrategyMode = seventhEighthStrategyMode;
    }

    public Integer getSeventhEighthPairModeDetectRoundNumber() {
        return seventhEighthPairModeDetectRoundNumber;
    }

    public void setSeventhEighthPairModeDetectRoundNumber(Integer seventhEighthPairModeDetectRoundNumber) {
        this.seventhEighthPairModeDetectRoundNumber = seventhEighthPairModeDetectRoundNumber;
    }

    public Integer getSeventhEighthGapRoundsForConsecutiveNumbers() {
        return seventhEighthGapRoundsForConsecutiveNumbers;
    }

    public void setSeventhEighthGapRoundsForConsecutiveNumbers(Integer seventhEighthGapRoundsForConsecutiveNumbers) {
        this.seventhEighthGapRoundsForConsecutiveNumbers = seventhEighthGapRoundsForConsecutiveNumbers;
    }

    public StrategyMode getNinethTenthStrategyMode() {
        return ninethTenthStrategyMode;
    }

    public void setNinethTenthStrategyMode(StrategyMode ninethTenthStrategyMode) {
        this.ninethTenthStrategyMode = ninethTenthStrategyMode;
    }

    public Integer getNinethTenthPairModeDetectRoundNumber() {
        return ninethTenthPairModeDetectRoundNumber;
    }

    public void setNinethTenthPairModeDetectRoundNumber(Integer ninethTenthPairModeDetectRoundNumber) {
        this.ninethTenthPairModeDetectRoundNumber = ninethTenthPairModeDetectRoundNumber;
    }

    public Integer getNinethTenthGapRoundsForConsecutiveNumbers() {
        return ninethTenthGapRoundsForConsecutiveNumbers;
    }

    public void setNinethTenthGapRoundsForConsecutiveNumbers(Integer ninethTenthGapRoundsForConsecutiveNumbers) {
        this.ninethTenthGapRoundsForConsecutiveNumbers = ninethTenthGapRoundsForConsecutiveNumbers;
    }

    public StrategyMode getFirstSecondStrategyMode() {
        return firstSecondStrategyMode;
    }

    public void setFirstSecondStrategyMode(StrategyMode firstSecondStrategyMode) {
        this.firstSecondStrategyMode = firstSecondStrategyMode;
    }

    public Boolean getSmpDaXiao() {
        return smpDaXiao;
    }

    public void setSmpDaXiao(Boolean smpDaXiao) {
        this.smpDaXiao = smpDaXiao;
    }

    public Boolean getSmpDanShuang() {
        return smpDanShuang;
    }

    public void setSmpDanShuang(Boolean smpDanShuang) {
        this.smpDanShuang = smpDanShuang;
    }

    public List<Integer> getSmpLevelChips() {
        return smpLevelChips;
    }

    public void setSmpLevelChips(List<Integer> smpLevelChips) {
        this.smpLevelChips = smpLevelChips;
    }

    public List<Integer> getFirstSecondLevelAccList() {
        return firstSecondLevelAccList;
    }

    public void setFirstSecondLevelAccList(List<Integer> firstSecondLevelAccList) {
        this.firstSecondLevelAccList = firstSecondLevelAccList;
    }

    public List<Integer> getThirdFourthLevelAccList() {
        return thirdFourthLevelAccList;
    }

    public void setThirdFourthLevelAccList(List<Integer> thirdFourthLevelAccList) {
        this.thirdFourthLevelAccList = thirdFourthLevelAccList;
    }

    public List<Integer> getFifthSixthLevelAccList() {
        return fifthSixthLevelAccList;
    }

    public void setFifthSixthLevelAccList(List<Integer> fifthSixthLevelAccList) {
        this.fifthSixthLevelAccList = fifthSixthLevelAccList;
    }

    public List<Integer> getSeventhEighthLevelAccList() {
        return seventhEighthLevelAccList;
    }

    public void setSeventhEighthLevelAccList(List<Integer> seventhEighthLevelAccList) {
        this.seventhEighthLevelAccList = seventhEighthLevelAccList;
    }

    public List<Integer> getNinethTenthLevelAccList() {
        return ninethTenthLevelAccList;
    }

    public void setNinethTenthLevelAccList(List<Integer> ninethTenthLevelAccList) {
        this.ninethTenthLevelAccList = ninethTenthLevelAccList;
    }

    public Integer getLostThreshold() {
        return lostThreshold;
    }

    public void setLostThreshold(Integer lostThreshold) {
        this.lostThreshold = lostThreshold;
    }

    public Boolean getSmpEnabled() {
        return smpEnabled;
    }

    public void setSmpEnabled(Boolean smpEnabled) {
        this.smpEnabled = smpEnabled;
    }


    public Boolean getFirstSecondEnabled() {
        return firstSecondEnabled;
    }

    public void setFirstSecondEnabled(Boolean firstSecondEnabled) {
        this.firstSecondEnabled = firstSecondEnabled;
    }

    public List<Integer> getFirstSecondExcludeNumbers() {
        return firstSecondExcludeNumbers;
    }

    public void setFirstSecondExcludeNumbers(List<Integer> firstSecondExcludeNumbers) {
        this.firstSecondExcludeNumbers = firstSecondExcludeNumbers;
    }


    public Boolean getThirdFourthEnabled() {
        return thirdFourthEnabled;
    }

    public void setThirdFourthEnabled(Boolean thirdFourthEnabled) {
        this.thirdFourthEnabled = thirdFourthEnabled;
    }

    public List<Integer> getThirdFourthExcludeNumbers() {
        return thirdFourthExcludeNumbers;
    }

    public void setThirdFourthExcludeNumbers(List<Integer> thirdFourthExcludeNumbers) {
        this.thirdFourthExcludeNumbers = thirdFourthExcludeNumbers;
    }


    public Boolean getFifthSixthEnabled() {
        return fifthSixthEnabled;
    }

    public void setFifthSixthEnabled(Boolean fifthSixthEnabled) {
        this.fifthSixthEnabled = fifthSixthEnabled;
    }

    public List<Integer> getFifthSixthExcludeNumbers() {
        return fifthSixthExcludeNumbers;
    }

    public void setFifthSixthExcludeNumbers(List<Integer> fifthSixthExcludeNumbers) {
        this.fifthSixthExcludeNumbers = fifthSixthExcludeNumbers;
    }


    public Boolean getSeventhEighthEnabled() {
        return seventhEighthEnabled;
    }

    public void setSeventhEighthEnabled(Boolean seventhEighthEnabled) {
        this.seventhEighthEnabled = seventhEighthEnabled;
    }

    public List<Integer> getSeventhEighthExcludeNumbers() {
        return seventhEighthExcludeNumbers;
    }

    public void setSeventhEighthExcludeNumbers(List<Integer> seventhEighthExcludeNumbers) {
        this.seventhEighthExcludeNumbers = seventhEighthExcludeNumbers;
    }


    public Boolean getNinethTenthEnabled() {
        return ninethTenthEnabled;
    }

    public void setNinethTenthEnabled(Boolean ninethTenthEnabled) {
        this.ninethTenthEnabled = ninethTenthEnabled;
    }

    public List<Integer> getNinethTenthExcludeNumbers() {
        return ninethTenthExcludeNumbers;
    }

    public void setNinethTenthExcludeNumbers(List<Integer> ninethTenthExcludeNumbers) {
        this.ninethTenthExcludeNumbers = ninethTenthExcludeNumbers;
    }
}
