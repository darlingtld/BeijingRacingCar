package com.lingda.gamble.model;


import com.lingda.gamble.param.Config;

import java.util.List;

public class ConfigDTO {
    private Integer smpChip;
    private Integer smpLevels;
    private Boolean smpEnabled;
    private List<Integer> smpLevelChips;

    private Integer firstSecondChip;
    private Boolean firstSecondEnabled;
    private List<Integer> firstSecondExcludeNumbers;
    private List<Integer> firstSecondLevelAccList;

    private Integer thirdFourthChip;
    private Boolean thirdFourthEnabled;
    private List<Integer> thirdFourthExcludeNumbers;
    private List<Integer> thirdFourthLevelAccList;

    private Integer fifthSixthChip;
    private Boolean fifthSixthEnabled;
    private List<Integer> fifthSixthExcludeNumbers;
    private List<Integer> fifthSixthLevelAccList;

    private Integer seventhEighthChip;
    private Boolean seventhEighthEnabled;
    private List<Integer> seventhEighthExcludeNumbers;
    private List<Integer> seventhEighthLevelAccList;

    private Integer ninethTenthChip;
    private Boolean ninethTenthEnabled;
    private List<Integer> ninethTenthExcludeNumbers;
    private List<Integer> ninethTenthLevelAccList;

    private Integer lostThreshold;

    public ConfigDTO() {
        setSmpChip(Config.getSmpChip());
        setSmpLevels(Config.getSmpLevels());
        setSmpEnabled(Config.getSmpEnabled());
        setSmpLevelChips(Config.getSmpLevelChips());

        setFirstSecondChip(Config.getFirstSecondChip());
        setFirstSecondEnabled(Config.getFirstSecondEnabled());
        setFirstSecondExcludeNumbers(Config.getFirstSecondExcludeNumbers());
        setFirstSecondLevelAccList(Config.getFirstSecondLevelAccList());

        setThirdFourthChip(Config.getThirdFourthChip());
        setThirdFourthEnabled(Config.getThirdFourthEnabled());
        setThirdFourthExcludeNumbers(Config.getThirdFourthExcludeNumbers());
        setThirdFourthLevelAccList(Config.getThirdFourthLevelAccList());

        setFifthSixthChip(Config.getFifthSixthChip());
        setFifthSixthEnabled(Config.getFifthSixthEnabled());
        setFifthSixthExcludeNumbers(Config.getFifthSixthExcludeNumbers());
        setFifthSixthLevelAccList(Config.getFifthSixthLevelAccList());

        setSeventhEighthChip(Config.getSeventhEighthChip());
        setSeventhEighthEnabled(Config.getSeventhEighthEnabled());
        setSeventhEighthExcludeNumbers(Config.getSeventhEighthExcludeNumbers());
        setSeventhEighthLevelAccList(Config.getSeventhEighthLevelAccList());

        setNinethTenthChip(Config.getNinethTenthChip());
        setNinethTenthEnabled(Config.getNinethTenthEnabled());
        setNinethTenthExcludeNumbers(Config.getNinethTenthExcludeNumbers());
        setNinethTenthLevelAccList(Config.getNinethTenthLevelAccList());

        setLostThreshold(Config.getLostThreshold());
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

    public Integer getSmpChip() {
        return smpChip;
    }

    public void setSmpChip(Integer smpChip) {
        this.smpChip = smpChip;
    }

    public Integer getSmpLevels() {
        return smpLevels;
    }

    public void setSmpLevels(Integer smpLevels) {
        this.smpLevels = smpLevels;
    }

    public Boolean getSmpEnabled() {
        return smpEnabled;
    }

    public void setSmpEnabled(Boolean smpEnabled) {
        this.smpEnabled = smpEnabled;
    }

    public Integer getFirstSecondChip() {
        return firstSecondChip;
    }

    public void setFirstSecondChip(Integer firstSecondChip) {
        this.firstSecondChip = firstSecondChip;
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

    public Integer getThirdFourthChip() {
        return thirdFourthChip;
    }

    public void setThirdFourthChip(Integer thirdFourthChip) {
        this.thirdFourthChip = thirdFourthChip;
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

    public Integer getFifthSixthChip() {
        return fifthSixthChip;
    }

    public void setFifthSixthChip(Integer fifthSixthChip) {
        this.fifthSixthChip = fifthSixthChip;
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

    public Integer getSeventhEighthChip() {
        return seventhEighthChip;
    }

    public void setSeventhEighthChip(Integer seventhEighthChip) {
        this.seventhEighthChip = seventhEighthChip;
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

    public Integer getNinethTenthChip() {
        return ninethTenthChip;
    }

    public void setNinethTenthChip(Integer ninethTenthChip) {
        this.ninethTenthChip = ninethTenthChip;
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
