package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seventh_eighth_bet")
public class SeventhEighthBet {
    @Id
    private String id;
    private Integer round;
    private RankSingleBet betSeventh;
    private RankSingleBet betEighth;

    @Override
    public String toString() {
        return "FirstSecondBet{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", betSeventh=" + betSeventh +
                ", betEighth=" + betEighth +
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

    public RankSingleBet getBetSeventh() {
        return betSeventh;
    }

    public void setBetSeventh(RankSingleBet betSeventh) {
        this.betSeventh = betSeventh;
    }

    public RankSingleBet getBetEighth() {
        return betEighth;
    }

    public void setBetEighth(RankSingleBet betEighth) {
        this.betEighth = betEighth;
    }
}
