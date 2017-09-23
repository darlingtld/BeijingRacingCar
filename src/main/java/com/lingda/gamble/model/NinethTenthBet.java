package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "nineth_tenth_bet")
public class NinethTenthBet {
    @Id
    private String id;
    private Integer round;
    private RankSingleBet betNineth;
    private RankSingleBet betTenth;

    @Override
    public String toString() {
        return "FirstSecondBet{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", betNineth=" + betNineth +
                ", betTenth=" + betTenth +
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

    public RankSingleBet getBetNineth() {
        return betNineth;
    }

    public void setBetNineth(RankSingleBet betNineth) {
        this.betNineth = betNineth;
    }

    public RankSingleBet getBetTenth() {
        return betTenth;
    }

    public void setBetTenth(RankSingleBet betTenth) {
        this.betTenth = betTenth;
    }
}
