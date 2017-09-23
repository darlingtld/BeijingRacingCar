package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "third_fourth_bet")
public class ThirdFourthBet {
    @Id
    private String id;
    private Integer round;
    private RankSingleBet betThird;
    private RankSingleBet betFourth;

    @Override
    public String toString() {
        return "FirstSecondBet{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", betThird=" + betThird +
                ", betFourth=" + betFourth +
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

    public RankSingleBet getBetThird() {
        return betThird;
    }

    public void setBetThird(RankSingleBet betThird) {
        this.betThird = betThird;
    }

    public RankSingleBet getBetFourth() {
        return betFourth;
    }

    public void setBetFourth(RankSingleBet betFourth) {
        this.betFourth = betFourth;
    }
}
