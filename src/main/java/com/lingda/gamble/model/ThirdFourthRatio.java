package com.lingda.gamble.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "third_fourth_ratio")
public class ThirdFourthRatio {
    @Id
    private String id;
    private Integer round;
    private RankSingleRatio ratioThird;
    private RankSingleRatio ratioFourth;

    @Override
    public String toString() {
        return "FirstSecondRatio{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", ratioThird=" + ratioThird +
                ", ratioFourth=" + ratioFourth +
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

    public RankSingleRatio getRatioThird() {
        return ratioThird;
    }

    public void setRatioThird(RankSingleRatio ratioThird) {
        this.ratioThird = ratioThird;
    }

    public RankSingleRatio getRatioFourth() {
        return ratioFourth;
    }

    public void setRatioFourth(RankSingleRatio ratioFourth) {
        this.ratioFourth = ratioFourth;
    }
}
