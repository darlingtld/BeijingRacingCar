package com.lingda.gamble.model;


import com.lingda.gamble.param.Config;

import java.util.List;

public class ConfigDTO {
    private Boolean smpEnabled;
    private Boolean smpDaXiao;
    private Boolean smpDanShuang;
    private List<Integer> smpLevelChips;
    private Boolean firstSecondEnabled;
    private Boolean firstSecondSmartMode;
    private List<Integer> firstSecondExcludeNumbers;
    private List<Integer> firstSecondLevelAccList;
    private Boolean thirdFourthEnabled;
    private Boolean thirdFourthSmartMode;
    private List<Integer> thirdFourthExcludeNumbers;
    private List<Integer> thirdFourthLevelAccList;
    private Boolean fifthSixthEnabled;
    private Boolean fifthSixthSmartMode;
    private List<Integer> fifthSixthExcludeNumbers;
    private List<Integer> fifthSixthLevelAccList;
    private Boolean seventhEighthEnabled;
    private Boolean seventhEighthSmartMode;
    private List<Integer> seventhEighthExcludeNumbers;
    private List<Integer> seventhEighthLevelAccList;
    private Boolean ninethTenthEnabled;
    private Boolean ninethTenthSmartMode;
    private List<Integer> ninethTenthExcludeNumbers;
    private List<Integer> ninethTenthLevelAccList;
    private Integer lostThreshold;

    public ConfigDTO() {
        setSmpEnabled(Config.getSmpEnabled());
        setSmpDaXiao(Config.getSmpDaXiao());
        setSmpDanShuang(Config.getSmpDanShuang());
        setSmpLevelChips(Config.getSmpLevelChips());

        setFirstSecondEnabled(Config.getFirstSecondEnabled());
        setFirstSecondSmartMode(Config.getFirstSecondSmartMode());
        setFirstSecondExcludeNumbers(Config.getFirstSecondExcludeNumbers());
        setFirstSecondLevelAccList(Config.getFirstSecondLevelAccList());

        setThirdFourthEnabled(Config.getThirdFourthEnabled());
        setThirdFourthSmartMode(Config.getThirdFourthSmartMode());
        setThirdFourthExcludeNumbers(Config.getThirdFourthExcludeNumbers());
        setThirdFourthLevelAccList(Config.getThirdFourthLevelAccList());

        setFifthSixthEnabled(Config.getFifthSixthEnabled());
        setFifthSixthEnabled(Config.getFifthSixthSmartMode());
        setFifthSixthExcludeNumbers(Config.getFifthSixthExcludeNumbers());
        setFifthSixthLevelAccList(Config.getFifthSixthLevelAccList());

        setSeventhEighthEnabled(Config.getSeventhEighthEnabled());
        setSeventhEighthSmartMode(Config.getSeventhEighthSmartMode());
        setSeventhEighthExcludeNumbers(Config.getSeventhEighthExcludeNumbers());
        setSeventhEighthLevelAccList(Config.getSeventhEighthLevelAccList());

        setNinethTenthEnabled(Config.getNinethTenthEnabled());
        setNinethTenthSmartMode(Config.getNinethTenthSmartMode());
        setNinethTenthExcludeNumbers(Config.getNinethTenthExcludeNumbers());
        setNinethTenthLevelAccList(Config.getNinethTenthLevelAccList());

        setLostThreshold(Config.getLostThreshold());
    }

    public Boolean getFirstSecondSmartMode() {
        return firstSecondSmartMode;
    }

    public void setFirstSecondSmartMode(Boolean firstSecondSmartMode) {
        this.firstSecondSmartMode = firstSecondSmartMode;
    }

    public Boolean getThirdFourthSmartMode() {
        return thirdFourthSmartMode;
    }

    public void setThirdFourthSmartMode(Boolean thirdFourthSmartMode) {
        this.thirdFourthSmartMode = thirdFourthSmartMode;
    }

    public Boolean getFifthSixthSmartMode() {
        return fifthSixthSmartMode;
    }

    public void setFifthSixthSmartMode(Boolean fifthSixthSmartMode) {
        this.fifthSixthSmartMode = fifthSixthSmartMode;
    }

    public Boolean getSeventhEighthSmartMode() {
        return seventhEighthSmartMode;
    }

    public void setSeventhEighthSmartMode(Boolean seventhEighthSmartMode) {
        this.seventhEighthSmartMode = seventhEighthSmartMode;
    }

    public Boolean getNinethTenthSmartMode() {
        return ninethTenthSmartMode;
    }

    public void setNinethTenthSmartMode(Boolean ninethTenthSmartMode) {
        this.ninethTenthSmartMode = ninethTenthSmartMode;
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
