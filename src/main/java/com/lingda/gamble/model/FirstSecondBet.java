package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "first_second_bet")
public class FirstSecondBet {
    @Id
    private String id;
    private Integer round;
    private RankSingleBet betFirst;
    private RankSingleBet betSecond;

    @Override
    public String toString() {
        return "FirstSecondBet{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", betFirst=" + betFirst +
                ", betSecond=" + betSecond +
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

    public RankSingleBet getBetFirst() {
        return betFirst;
    }

    public void setBetFirst(RankSingleBet betFirst) {
        this.betFirst = betFirst;
    }

    public RankSingleBet getBetSecond() {
        return betSecond;
    }

    public void setBetSecond(RankSingleBet betSecond) {
        this.betSecond = betSecond;
    }
}
