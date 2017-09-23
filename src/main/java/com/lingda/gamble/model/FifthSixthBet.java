package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fifth_sixth_bet")
public class FifthSixthBet {
    @Id
    private String id;
    private Integer round;
    private RankSingleBet betFifth;
    private RankSingleBet betSixth;

    @Override
    public String toString() {
        return "FirstSecondBet{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", betFifth=" + betFifth +
                ", betSixth=" + betSixth +
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

    public RankSingleBet getBetFifth() {
        return betFifth;
    }

    public void setBetFifth(RankSingleBet betFifth) {
        this.betFifth = betFifth;
    }

    public RankSingleBet getBetSixth() {
        return betSixth;
    }

    public void setBetSixth(RankSingleBet betSixth) {
        this.betSixth = betSixth;
    }
}
