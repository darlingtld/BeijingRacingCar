package com.lingda.gamble.test;

import com.lingda.gamble.model.SMPBet;
import com.lingda.gamble.model.SMPSingleBet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrategyTest {

    public static double money = 1000;

    public static void main(String[] args) {
        List<SMPBet> smpBetList = new ArrayList<>();
        List<List<Integer>> lotteryResultList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("===== lottery result =====");
            List<Integer> lotteryResult = getRacingResult();
            lotteryResultList.add(lotteryResult);
            System.out.println(lotteryResult);
            if (smpBetList.isEmpty()) {
                SMPBet smpBet = new SMPBet();
                SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                smpSingleBet1.setDa(1);
                smpBet.setBetFirst(smpSingleBet1);
                SMPSingleBet smpSingleBet2 = new SMPSingleBet();
                smpSingleBet2.setDa(1);
                smpBet.setBetSecond(smpSingleBet2);
                SMPSingleBet smpSingleBet3 = new SMPSingleBet();
                smpSingleBet3.setDa(1);
                smpBet.setBetThird(smpSingleBet3);
                SMPSingleBet smpSingleBet4 = new SMPSingleBet();
                smpSingleBet4.setDa(1);
                smpBet.setBetFourth(smpSingleBet4);
                SMPSingleBet smpSingleBet5 = new SMPSingleBet();
                smpSingleBet5.setDa(1);
                smpBet.setBetFifth(smpSingleBet5);
                SMPSingleBet smpSingleBet6 = new SMPSingleBet();
                smpSingleBet6.setXiao(1);
                smpBet.setBetSixth(smpSingleBet6);
                SMPSingleBet smpSingleBet7 = new SMPSingleBet();
                smpSingleBet7.setXiao(1);
                smpBet.setBetSeventh(smpSingleBet7);
                SMPSingleBet smpSingleBet8 = new SMPSingleBet();
                smpSingleBet8.setXiao(1);
                smpBet.setBetEighth(smpSingleBet8);
                SMPSingleBet smpSingleBet9 = new SMPSingleBet();
                smpSingleBet9.setXiao(1);
                smpBet.setBetNineth(smpSingleBet9);
                SMPSingleBet smpSingleBet10 = new SMPSingleBet();
                smpSingleBet10.setXiao(1);
                smpBet.setBetTenth(smpSingleBet10);
                money -= 10;
                System.out.println(smpBet);
                smpBetList.add(smpBet);
            } else {
                List<Integer> lastLotteryResult = lotteryResultList.get(i - 1);
                SMPBet lastSMPBet = smpBetList.get(i - 1);
                if (lastLotteryResult.get(0) > 5) {
                    money += lastSMPBet.getBetFirst().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetFirst().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(1) > 5) {
                    money += lastSMPBet.getBetSecond().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetSecond().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(2) > 5) {
                    money += lastSMPBet.getBetThird().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetThird().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(3) > 5) {
                    money += lastSMPBet.getBetFourth().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetFourth().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(4) > 5) {
                    money += lastSMPBet.getBetFifth().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetFifth().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(5) > 5) {
                    money += lastSMPBet.getBetSixth().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetSixth().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(6) > 5) {
                    money += lastSMPBet.getBetSeventh().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetSeventh().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(7) > 5) {
                    money += lastSMPBet.getBetEighth().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetEighth().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(8) > 5) {
                    money += lastSMPBet.getBetNineth().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetNineth().getXiao() * 1.98;
                }
                if (lastLotteryResult.get(9) > 5) {
                    money += lastSMPBet.getBetTenth().getDa() * 1.98;
                } else {
                    money += lastSMPBet.getBetTenth().getXiao() * 1.98;
                }

                SMPBet smpBet = new SMPBet();
//                1
                if (lastLotteryResult.get(0) > 5) {
                    if (lastSMPBet.getBetFirst().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetFirst().getDa()));
                        smpBet.setBetFirst(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetFirst(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetFirst().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetFirst().getXiao()));
                        smpBet.setBetFirst(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetFirst(smpSingleBet1);
                        money -= 1;
                    }
                }
//                2
                if (lastLotteryResult.get(1) > 5) {
                    if (lastSMPBet.getBetSecond().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetSecond().getDa()));
                        smpBet.setBetSecond(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetSecond(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetSecond().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetSecond().getXiao()));
                        smpBet.setBetSecond(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetSecond(smpSingleBet1);
                        money -= 1;
                    }
                }
//                3
                if (lastLotteryResult.get(2) > 5) {
                    if (lastSMPBet.getBetThird().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetThird().getDa()));
                        smpBet.setBetThird(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetThird(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetThird().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetThird().getXiao()));
                        smpBet.setBetThird(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetThird(smpSingleBet1);
                        money -= 1;
                    }
                }
//                4
                if (lastLotteryResult.get(3) > 5) {
                    if (lastSMPBet.getBetFourth().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetFourth().getDa()));
                        smpBet.setBetFourth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetFourth(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetFourth().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetFourth().getXiao()));
                        smpBet.setBetFourth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetFourth(smpSingleBet1);
                        money -= 1;
                    }
                }
//                5
                if (lastLotteryResult.get(4) > 5) {
                    if (lastSMPBet.getBetFifth().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetFifth().getDa()));
                        smpBet.setBetFifth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetFifth(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetFifth().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetFifth().getXiao()));
                        smpBet.setBetFifth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetFifth(smpSingleBet1);
                        money -= 1;
                    }
                }
//                6
                if (lastLotteryResult.get(5) > 5) {
                    if (lastSMPBet.getBetSixth().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetSixth().getDa()));
                        smpBet.setBetSixth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetSixth(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetSixth().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetSixth().getXiao()));
                        smpBet.setBetSixth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetSixth(smpSingleBet1);
                        money -= 1;
                    }
                }
//                7
                if (lastLotteryResult.get(6) > 5) {
                    if (lastSMPBet.getBetSeventh().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetSeventh().getDa()));
                        smpBet.setBetSeventh(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetSeventh(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetSeventh().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetSeventh().getXiao()));
                        smpBet.setBetSeventh(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetSeventh(smpSingleBet1);
                        money -= 1;
                    }
                }
//                8
                if (lastLotteryResult.get(7) > 5) {
                    if (lastSMPBet.getBetEighth().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetEighth().getDa()));
                        smpBet.setBetEighth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetEighth(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetEighth().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetEighth().getXiao()));
                        smpBet.setBetEighth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetEighth(smpSingleBet1);
                        money -= 1;
                    }
                }
//                9
                if (lastLotteryResult.get(8) > 5) {
                    if (lastSMPBet.getBetNineth().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetNineth().getDa()));
                        smpBet.setBetNineth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetNineth(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetNineth().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetNineth().getXiao()));
                        smpBet.setBetNineth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetNineth(smpSingleBet1);
                        money -= 1;
                    }
                }
//                10
                if (lastLotteryResult.get(9) > 5) {
                    if (lastSMPBet.getBetTenth().getDa() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(decideBet(lastSMPBet.getBetTenth().getDa()));
                        smpBet.setBetTenth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setDa(1);
                        smpBet.setBetTenth(smpSingleBet1);
                        money -= 1;
                    }
                } else {
                    if (lastSMPBet.getBetTenth().getXiao() > 0) {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(decideBet(lastSMPBet.getBetTenth().getXiao()));
                        smpBet.setBetTenth(smpSingleBet1);
                    } else {
                        SMPSingleBet smpSingleBet1 = new SMPSingleBet();
                        smpSingleBet1.setXiao(1);
                        smpBet.setBetTenth(smpSingleBet1);
                        money -= 1;
                    }
                }
                System.out.println(smpBet);
                smpBetList.add(smpBet);
            }
            System.out.println(money);
            System.out.println("==============");
        }
    }

    private static double decideBet(double chip) {
        double retChip = 0;
        if (chip > 2 << (4 - 1)) {
            retChip = 0;
        } else {
            retChip = chip * 2;
        }
        money -= retChip;
        return retChip;
    }

    private static List<Integer> getRacingResult() {
        List<Integer> result = new ArrayList<>();
        while (true) {
            int i = new Random().nextInt(Integer.MAX_VALUE) % 11;
            if ((i >= 1 && i <= 10) && !result.contains(i)) {
                result.add(i);
                if (result.size() == 10) {
                    return result;
                }
            }
        }
    }
}
