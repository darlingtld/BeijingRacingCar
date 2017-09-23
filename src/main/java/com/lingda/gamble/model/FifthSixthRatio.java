package com.lingda.gamble.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fifth_sixth_ratio")
public class FifthSixthRatio {
    @Id
    private String id;
    private Integer round;
    private RankSingleRatio ratioFifth;
    private RankSingleRatio ratioSixth;

    @Override
    public String toString() {
        return "FirstSecondRatio{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", ratioFifth=" + ratioFifth +
                ", ratioSixth=" + ratioSixth +
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

    public RankSingleRatio getRatioFifth() {
        return ratioFifth;
    }

    public void setRatioFifth(RankSingleRatio ratioFifth) {
        this.ratioFifth = ratioFifth;
    }

    public RankSingleRatio getRatioSixth() {
        return ratioSixth;
    }

    public void setRatioSixth(RankSingleRatio ratioSixth) {
        this.ratioSixth = ratioSixth;
    }
}
