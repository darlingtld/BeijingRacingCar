package com.lingda.gamble.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "first_second_ratio")
public class FirstSecondRatio {
    @Id
    private String id;
    private Integer round;
    private RankSingleRatio ratioFirst;
    private RankSingleRatio ratioSecond;

    @Override
    public String toString() {
        return "FirstSecondRatio{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", ratioFirst=" + ratioFirst +
                ", ratioSecond=" + ratioSecond +
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

    public RankSingleRatio getRatioFirst() {
        return ratioFirst;
    }

    public void setRatioFirst(RankSingleRatio ratioFirst) {
        this.ratioFirst = ratioFirst;
    }

    public RankSingleRatio getRatioSecond() {
        return ratioSecond;
    }

    public void setRatioSecond(RankSingleRatio ratioSecond) {
        this.ratioSecond = ratioSecond;
    }
}
