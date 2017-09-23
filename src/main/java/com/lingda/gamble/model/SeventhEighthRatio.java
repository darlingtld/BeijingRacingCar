package com.lingda.gamble.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seventh_eighth_ratio")
public class SeventhEighthRatio {
    @Id
    private String id;
    private Integer round;
    private RankSingleRatio ratioSeventh;
    private RankSingleRatio ratioEighth;

    @Override
    public String toString() {
        return "FirstSecondRatio{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", ratioSeventh=" + ratioSeventh +
                ", ratioEighth=" + ratioEighth +
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

    public RankSingleRatio getRatioSeventh() {
        return ratioSeventh;
    }

    public void setRatioSeventh(RankSingleRatio ratioSeventh) {
        this.ratioSeventh = ratioSeventh;
    }

    public RankSingleRatio getRatioEighth() {
        return ratioEighth;
    }

    public void setRatioEighth(RankSingleRatio ratioEighth) {
        this.ratioEighth = ratioEighth;
    }
}
