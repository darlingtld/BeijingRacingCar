package com.lingda.gamble.model;

public class SMPSingleRatio {
    private double da;
    private double xiao;
    private double dan;
    private double shuang;
    private double lon;
    private double hu;

    public SMPSingleRatio() {
    }

    public SMPSingleRatio(double da, double xiao, double dan, double shuang, double lon, double hu) {
        this.da = da;
        this.xiao = xiao;
        this.dan = dan;
        this.shuang = shuang;
        this.lon = lon;
        this.hu = hu;
    }

    public double getDa() {
        return da;
    }

    public void setDa(double da) {
        this.da = da;
    }

    public double getXiao() {
        return xiao;
    }

    public void setXiao(double xiao) {
        this.xiao = xiao;
    }

    public double getDan() {
        return dan;
    }

    public void setDan(double dan) {
        this.dan = dan;
    }

    public double getShuang() {
        return shuang;
    }

    public void setShuang(double shuang) {
        this.shuang = shuang;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getHu() {
        return hu;
    }

    public void setHu(double hu) {
        this.hu = hu;
    }

    @Override
    public String toString() {
        return "SMPSingleRatio{" +
                "da=" + da +
                ", xiao=" + xiao +
                ", dan=" + dan +
                ", shuang=" + shuang +
                ", lon=" + lon +
                ", hu=" + hu +
                '}';
    }
}
