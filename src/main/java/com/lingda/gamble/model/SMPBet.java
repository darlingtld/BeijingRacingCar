package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "smp_bet")
public class SMPBet {
    @Id
    private String id;
    private Integer round;
    private SMPSingleBet betFirst;
    private SMPSingleBet betSecond;
    private SMPSingleBet betThird;
    private SMPSingleBet betFourth;
    private SMPSingleBet betFifth;
    private SMPSingleBet betSixth;
    private SMPSingleBet betSeventh;
    private SMPSingleBet betEighth;
    private SMPSingleBet betNinth;
    private SMPSingleBet betTenth;

    @Override
    public String toString() {
        return "SMPBet{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", betFirst=" + betFirst +
                ", betSecond=" + betSecond +
                ", betThird=" + betThird +
                ", betFourth=" + betFourth +
                ", betFifth=" + betFifth +
                ", betSixth=" + betSixth +
                ", betSeventh=" + betSeventh +
                ", betEighth=" + betEighth +
                ", betNinth=" + betNinth +
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

    public SMPSingleBet getBetFirst() {
        return betFirst;
    }

    public void setBetFirst(SMPSingleBet betFirst) {
        this.betFirst = betFirst;
    }

    public SMPSingleBet getBetSecond() {
        return betSecond;
    }

    public void setBetSecond(SMPSingleBet betSecond) {
        this.betSecond = betSecond;
    }

    public SMPSingleBet getBetThird() {
        return betThird;
    }

    public void setBetThird(SMPSingleBet betThird) {
        this.betThird = betThird;
    }

    public SMPSingleBet getBetFourth() {
        return betFourth;
    }

    public void setBetFourth(SMPSingleBet betFourth) {
        this.betFourth = betFourth;
    }

    public SMPSingleBet getBetFifth() {
        return betFifth;
    }

    public void setBetFifth(SMPSingleBet betFifth) {
        this.betFifth = betFifth;
    }

    public SMPSingleBet getBetSixth() {
        return betSixth;
    }

    public void setBetSixth(SMPSingleBet betSixth) {
        this.betSixth = betSixth;
    }

    public SMPSingleBet getBetSeventh() {
        return betSeventh;
    }

    public void setBetSeventh(SMPSingleBet betSeventh) {
        this.betSeventh = betSeventh;
    }

    public SMPSingleBet getBetEighth() {
        return betEighth;
    }

    public void setBetEighth(SMPSingleBet betEighth) {
        this.betEighth = betEighth;
    }

    public SMPSingleBet getBetNinth() {
        return betNinth;
    }

    public void setBetNinth(SMPSingleBet betNinth) {
        this.betNinth = betNinth;
    }

    public SMPSingleBet getBetTenth() {
        return betTenth;
    }

    public void setBetTenth(SMPSingleBet betTenth) {
        this.betTenth = betTenth;
    }
}
