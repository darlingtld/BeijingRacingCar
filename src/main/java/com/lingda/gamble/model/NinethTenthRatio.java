package com.lingda.gamble.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "nineth_tenth_ratio")
public class NinethTenthRatio {
    @Id
    private String id;
    private Integer round;
    private RankSingleRatio ratioNineth;
    private RankSingleRatio ratioTenth;

    @Override
    public String toString() {
        return "FirstSecondRatio{" +
                "id='" + id + '\'' +
                ", round=" + round +
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

    public RankSingleRatio getRatioNineth() {
        return ratioNineth;
    }

    public void setRatioNineth(RankSingleRatio ratioNineth) {
        this.ratioNineth = ratioNineth;
    }

    public RankSingleRatio getRatioTenth() {
        return ratioTenth;
    }

    public void setRatioTenth(RankSingleRatio ratioTenth) {
        this.ratioTenth = ratioTenth;
    }
}
