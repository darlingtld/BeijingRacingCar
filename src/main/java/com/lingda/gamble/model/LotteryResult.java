package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//记录开奖结果
@Document(collection = "lottery_result")
public class LotteryResult {

    @Id
    private String id;
    private Integer round;
    private Integer first;
    private Integer second;
    private Integer third;
    private Integer fourth;
    private Integer fifth;
    private Integer sixth;
    private Integer seventh;
    private Integer eighth;
    private Integer nineth;
    private Integer tenth;

    @Override
    public String toString() {
        return "LotteryResult{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", first=" + first +
                ", second=" + second +
                ", third=" + third +
                ", fourth=" + fourth +
                ", fifth=" + fifth +
                ", sixth=" + sixth +
                ", seventh=" + seventh +
                ", eighth=" + eighth +
                ", nineth=" + nineth +
                ", tenth=" + tenth +
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

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getThird() {
        return third;
    }

    public void setThird(Integer third) {
        this.third = third;
    }

    public Integer getFourth() {
        return fourth;
    }

    public void setFourth(Integer fourth) {
        this.fourth = fourth;
    }

    public Integer getFifth() {
        return fifth;
    }

    public void setFifth(Integer fifth) {
        this.fifth = fifth;
    }

    public Integer getSixth() {
        return sixth;
    }

    public void setSixth(Integer sixth) {
        this.sixth = sixth;
    }

    public Integer getSeventh() {
        return seventh;
    }

    public void setSeventh(Integer seventh) {
        this.seventh = seventh;
    }

    public Integer getEighth() {
        return eighth;
    }

    public void setEighth(Integer eighth) {
        this.eighth = eighth;
    }

    public Integer getNineth() {
        return nineth;
    }

    public void setNineth(Integer nineth) {
        this.nineth = nineth;
    }

    public Integer getTenth() {
        return tenth;
    }

    public void setTenth(Integer tenth) {
        this.tenth = tenth;
    }
}
