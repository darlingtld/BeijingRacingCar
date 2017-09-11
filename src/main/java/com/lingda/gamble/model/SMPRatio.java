package com.lingda.gamble.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "smp_ratio")
public class SMPRatio {
    @Id
    private String id;
    private Integer round;
    private SMPSingleRatio ratioFirst;
    private SMPSingleRatio ratioSecond;
    private SMPSingleRatio ratioThird;
    private SMPSingleRatio ratioFourth;
    private SMPSingleRatio ratioFifth;
    private SMPSingleRatio ratioSixth;
    private SMPSingleRatio ratioSeventh;
    private SMPSingleRatio ratioEighth;
    private SMPSingleRatio ratioNineth;
    private SMPSingleRatio ratioTenth;

    @Override
    public String toString() {
        return "SMPRatio{" +
                "id=" + id +
                ", round=" + round +
                ", ratioFirst=" + ratioFirst +
                ", ratioSecond=" + ratioSecond +
                ", ratioThird=" + ratioThird +
                ", ratioFourth=" + ratioFourth +
                ", ratioFifth=" + ratioFifth +
                ", ratioSixth=" + ratioSixth +
                ", ratioSeventh=" + ratioSeventh +
                ", ratioEighth=" + ratioEighth +
                ", ratioNineth=" + ratioNineth +
                ", ratioTenth=" + ratioTenth +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public SMPSingleRatio getRatioFirst() {
        return ratioFirst;
    }

    public void setRatioFirst(SMPSingleRatio ratioFirst) {
        this.ratioFirst = ratioFirst;
    }

    public SMPSingleRatio getRatioSecond() {
        return ratioSecond;
    }

    public void setRatioSecond(SMPSingleRatio ratioSecond) {
        this.ratioSecond = ratioSecond;
    }

    public SMPSingleRatio getRatioThird() {
        return ratioThird;
    }

    public void setRatioThird(SMPSingleRatio ratioThird) {
        this.ratioThird = ratioThird;
    }

    public SMPSingleRatio getRatioFourth() {
        return ratioFourth;
    }

    public void setRatioFourth(SMPSingleRatio ratioFourth) {
        this.ratioFourth = ratioFourth;
    }

    public SMPSingleRatio getRatioFifth() {
        return ratioFifth;
    }

    public void setRatioFifth(SMPSingleRatio ratioFifth) {
        this.ratioFifth = ratioFifth;
    }

    public SMPSingleRatio getRatioSixth() {
        return ratioSixth;
    }

    public void setRatioSixth(SMPSingleRatio ratioSixth) {
        this.ratioSixth = ratioSixth;
    }

    public SMPSingleRatio getRatioSeventh() {
        return ratioSeventh;
    }

    public void setRatioSeventh(SMPSingleRatio ratioSeventh) {
        this.ratioSeventh = ratioSeventh;
    }

    public SMPSingleRatio getRatioEighth() {
        return ratioEighth;
    }

    public void setRatioEighth(SMPSingleRatio ratioEighth) {
        this.ratioEighth = ratioEighth;
    }

    public SMPSingleRatio getRatioNineth() {
        return ratioNineth;
    }

    public void setRatioNineth(SMPSingleRatio ratioNineth) {
        this.ratioNineth = ratioNineth;
    }

    public SMPSingleRatio getRatioTenth() {
        return ratioTenth;
    }

    public void setRatioTenth(SMPSingleRatio ratioTenth) {
        this.ratioTenth = ratioTenth;
    }
}
