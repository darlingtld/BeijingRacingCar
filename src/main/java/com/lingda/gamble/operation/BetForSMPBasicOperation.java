package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.SMPBet;
import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleBet;
import com.lingda.gamble.model.SingleBetCategory;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.SMPBetRepository;
import com.lingda.gamble.repository.SMPRatioRepository;
import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//北京赛车 双面盘下注
//赢就翻倍，输收场，重新来，
@Component
public class BetForSMPBasicOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForSMPBasicOperation.class);

    private static final String PLAYGROUND = "双面盘";

    @Value("${gamble.bet.money}")
    private double money;

    private final SMPBetRepository smpBetRepository;

    private final SMPRatioRepository smpRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public BetForSMPBasicOperation(SMPBetRepository smpBetRepository,
                                   SMPRatioRepository smpRatioRepository,
                                   LotteryResultRepository lotteryResultRepository) {
        this.smpBetRepository = smpBetRepository;
        this.smpRatioRepository = smpRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public boolean doBet(WebDriver driver, Integer round, boolean isPlayTime) throws InterruptedException {
        Double chip = Double.valueOf(Config.getSmpChip());
        logger.info("[Operation - Bet] Base chip is {}", chip);
        logger.info("[Operation - Bet] Play Time is {}", isPlayTime);
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        logger.info("[Operation - Bet] Bet for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
        SMPRatio smpRatio = smpRatioRepository.findByRound(round);
        if (smpRatio == null) {
            logger.info("[Operation - Bet] No ratio information for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
        LotteryResult lastLotteryResult = lotteryResultRepository.findByRound(round - 1);
        if (lastLotteryResult == null) {
            logger.info("[Operation - Bet] No last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
            return false;
        }

//      check if the bet is already done
        if (smpBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - {}", PLAYGROUND);
        SMPBet lastSmpBet = smpBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 (for test) ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastSmpBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");

        SMPBet smpBet = new SMPBet();
        smpBet.setRound(round);
//        ============== 策略逻辑 ==============
        if (lastSmpBet == null) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                return false;
            }
            logger.info("[Operation - Bet] No last bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
            //      投注 冠-五 大
            betForFirst(smpBet, chip, SingleBetCategory.DA, driver);
            betForSecond(smpBet, chip, SingleBetCategory.DA, driver);
            betForThird(smpBet, chip, SingleBetCategory.DA, driver);
            betForFourth(smpBet, chip, SingleBetCategory.DA, driver);
            betForFifth(smpBet, chip, SingleBetCategory.DA, driver);
            //      投注 六-十 小
            betForSixth(smpBet, chip, SingleBetCategory.XIAO, driver);
            betForSeventh(smpBet, chip, SingleBetCategory.XIAO, driver);
            betForEighth(smpBet, chip, SingleBetCategory.XIAO, driver);
            betForNineth(smpBet, chip, SingleBetCategory.XIAO, driver);
            betForTenth(smpBet, chip, SingleBetCategory.XIAO, driver);
            money = calculateMoney(money, -10 * chip);
        } else {
//            check if last bet is a winner
//            check 冠军
            if (lastLotteryResult.getFirst() > 5) {
                if (lastSmpBet.getBetFirst().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "胜利");
//                winner.  继续下注大
                        //      投注 冠军 大
                        double betChip = decideBetChip(lastSmpBet.getBetFirst(), SingleBetCategory.DA);
                        betForFirst(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "失败");
//                    //      投注 冠军 小
                    betForFirst(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetFirst().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "胜利");
//                winner.  继续下注小，
                        //      投注 冠军 小
                        double betChip = decideBetChip(lastSmpBet.getBetFirst(), SingleBetCategory.XIAO);
                        betForFirst(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "失败");
//                loser.  不加筹码
                    //      投注 冠军 大
                    betForFirst(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 亚军
            if (lastLotteryResult.getSecond() > 5) {
                if (lastSmpBet.getBetSecond().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "胜利");
//                winner.  继续下注大，增加筹码
                        //      投注 亚军 大
                        double betChip = decideBetChip(lastSmpBet.getBetSecond(), SingleBetCategory.DA);
                        betForSecond(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "失败");
//                    //      投注 亚军 大
                    betForSecond(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetSecond().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "胜利");
//                winner.  继续下注小
                        //      投注 亚军 小
                        double betChip = decideBetChip(lastSmpBet.getBetSecond(), SingleBetCategory.XIAO);
                        betForSecond(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "失败");
//                loser.  不加筹码
                    //      投注 亚军 大
                    betForSecond(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 季军
            if (lastLotteryResult.getThird() > 5) {
                if (lastSmpBet.getBetThird().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "胜利");
//                winner.  继续下注大，增加筹码
                        //      投注 季军 大
                        double betChip = decideBetChip(lastSmpBet.getBetThird(), SingleBetCategory.DA);
                        betForThird(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "失败");
//                    //      投注 季军 小
                    betForThird(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetThird().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "胜利");
//                winner.  继续下注小，增加筹码
                        double betChip = decideBetChip(lastSmpBet.getBetThird(), SingleBetCategory.XIAO);
                        betForThird(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "失败");
//                loser.  不加筹码
                    betForThird(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 四
            if (lastLotteryResult.getFourth() > 5) {
                if (lastSmpBet.getBetFourth().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "胜利");
//                winner.  继续下注大
                        //      投注 四 大
                        double betChip = decideBetChip(lastSmpBet.getBetFourth(), SingleBetCategory.DA);
                        betForFourth(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "失败");
//                    //      投注 四 大
                    betForFourth(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetFourth().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "胜利");
//                winner.  继续下注小
                        //      投注 四 小
                        double betChip = decideBetChip(lastSmpBet.getBetFourth(), SingleBetCategory.XIAO);
                        betForFourth(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "失败");
//                loser.
                    //      投注 四 小
                    betForFourth(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 五
            if (lastLotteryResult.getFifth() > 5) {
                if (lastSmpBet.getBetFifth().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "胜利");
//                winner.  继续下注大
                        //      投注 五 大
                        double betChip = decideBetChip(lastSmpBet.getBetFifth(), SingleBetCategory.DA);
                        betForFifth(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "失败");
//                    //      投注 五 大
                    betForFifth(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetFifth().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "胜利");
//                winner.  继续下注小
                        double betChip = decideBetChip(lastSmpBet.getBetFifth(), SingleBetCategory.XIAO);
                        betForFifth(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "失败");
//                loser.  不加筹码
                    //      投注 五 小
                    betForFifth(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

//            check 六
            if (lastLotteryResult.getSixth() > 5) {
                if (lastSmpBet.getBetSixth().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "胜利");
//                winner.  继续下注大
                        //      投注 六 大
                        double betChip = decideBetChip(lastSmpBet.getBetSixth(), SingleBetCategory.DA);
                        betForSixth(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "失败");
//                    //      投注 六 大
                    betForSixth(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetSixth().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "胜利");
//                winner.  继续下注小
                        double betChip = decideBetChip(lastSmpBet.getBetSixth(), SingleBetCategory.XIAO);
                        betForSixth(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "失败");
//                loser.  不加筹码
                    betForSixth(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 七
            if (lastLotteryResult.getSeventh() > 5) {
                if (lastSmpBet.getBetSeventh().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "胜利");
//                winner.  继续下注大
                        //      投注 七 大
                        double betChip = decideBetChip(lastSmpBet.getBetSeventh(), SingleBetCategory.DA);
                        betForSeventh(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "失败");
                    betForSeventh(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetSeventh().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "胜利");
//                winner.  继续下注小
                        double betChip = decideBetChip(lastSmpBet.getBetSeventh(), SingleBetCategory.XIAO);
                        betForSeventh(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "失败");
//                loser.
                    betForSeventh(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 八
            if (lastLotteryResult.getEighth() > 5) {
                if (lastSmpBet.getBetEighth().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "胜利");
//                winner.  继续下注大
                        double betChip = decideBetChip(lastSmpBet.getBetEighth(), SingleBetCategory.DA);
                        betForEighth(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "失败");
                    betForEighth(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetEighth().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "胜利");
//                winner.  继续下注小
                        double betChip = decideBetChip(lastSmpBet.getBetEighth(), SingleBetCategory.XIAO);
                        betForEighth(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "失败");
//                loser
                    betForEighth(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 九
            if (lastLotteryResult.getNineth() > 5) {
                if (lastSmpBet.getBetNineth().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "胜利");
//                winner.  继续下注大
                        //      投注 九 大
                        double betChip = decideBetChip(lastSmpBet.getBetNineth(), SingleBetCategory.DA);
                        betForNineth(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "失败");
//                    //      投注 九 大
                    betForNineth(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetNineth().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "胜利");
//                winner.  继续下注小
                        double betChip = decideBetChip(lastSmpBet.getBetNineth(), SingleBetCategory.XIAO);
                        betForNineth(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "失败");
//                loser.
                    betForNineth(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }

            //            check 十
            if (lastLotteryResult.getTenth() > 5) {
                if (lastSmpBet.getBetTenth().getDa() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "胜利");
//                winner.  继续下注大
                        //      投注 十 大
                    double betChip = decideBetChip(lastSmpBet.getBetTenth(), SingleBetCategory.DA);
                        betForTenth(smpBet, betChip, SingleBetCategory.DA, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "失败");
//                    //      投注 十 大
                    betForTenth(smpBet, chip, SingleBetCategory.XIAO, driver);
                    money = calculateMoney(money, -chip);
                }
            } else {
                if (lastSmpBet.getBetTenth().getXiao() > 0) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "胜利");
//                winner.  继续下注小
                        //      投注 十 小
                    double betChip = decideBetChip(lastSmpBet.getBetTenth(), SingleBetCategory.XIAO);
                        betForTenth(smpBet, betChip, SingleBetCategory.XIAO, driver);
                        money = calculateMoney(money, -betChip);
                    }
                } else {
                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "失败");
//                loser.  不加筹码
                    //      投注 十 小
                    betForTenth(smpBet, chip, SingleBetCategory.DA, driver);
                    money = calculateMoney(money, -chip);
                }
            }
        }

        logger.info("=============== 金额 (for test) ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        smpBetRepository.save(smpBet);
        return true;

    }

    private double calculateLastLotteryResult(SMPBet lastSmpBet, LotteryResult lotteryResult) {
        if (lastSmpBet == null) {
            return 0;
        }
        double winningMoney = 0;
        SMPRatio lastSMPRatio = smpRatioRepository.findByRound(lastSmpBet.getRound());
        if (lotteryResult.getFirst() > 5) {
            winningMoney += lastSmpBet.getBetFirst().getDa() * lastSMPRatio.getRatioFirst().getDa();
        } else {
            winningMoney += lastSmpBet.getBetFirst().getXiao() * lastSMPRatio.getRatioFirst().getXiao();
        }

        if (lotteryResult.getSecond() > 5) {
            winningMoney += lastSmpBet.getBetSecond().getDa() * lastSMPRatio.getRatioSecond().getDa();
        } else {
            winningMoney += lastSmpBet.getBetSecond().getXiao() * lastSMPRatio.getRatioSecond().getXiao();
        }

        if (lotteryResult.getThird() > 5) {
            winningMoney += lastSmpBet.getBetThird().getDa() * lastSMPRatio.getRatioThird().getDa();
        } else {
            winningMoney += lastSmpBet.getBetThird().getXiao() * lastSMPRatio.getRatioThird().getXiao();
        }

        if (lotteryResult.getFourth() > 5) {
            winningMoney += lastSmpBet.getBetFourth().getDa() * lastSMPRatio.getRatioFourth().getDa();
        } else {
            winningMoney += lastSmpBet.getBetFourth().getXiao() * lastSMPRatio.getRatioFourth().getXiao();
        }

        if (lotteryResult.getFifth() > 5) {
            winningMoney += lastSmpBet.getBetFifth().getDa() * lastSMPRatio.getRatioFifth().getDa();
        } else {
            winningMoney += lastSmpBet.getBetFifth().getXiao() * lastSMPRatio.getRatioFifth().getXiao();
        }

        if (lotteryResult.getSixth() > 5) {
            winningMoney += lastSmpBet.getBetSixth().getDa() * lastSMPRatio.getRatioSixth().getDa();
        } else {
            winningMoney += lastSmpBet.getBetSixth().getXiao() * lastSMPRatio.getRatioSixth().getXiao();
        }

        if (lotteryResult.getSeventh() > 5) {
            winningMoney += lastSmpBet.getBetSeventh().getDa() * lastSMPRatio.getRatioSeventh().getDa();
        } else {
            winningMoney += lastSmpBet.getBetSeventh().getXiao() * lastSMPRatio.getRatioSeventh().getXiao();
        }

        if (lotteryResult.getEighth() > 5) {
            winningMoney += lastSmpBet.getBetEighth().getDa() * lastSMPRatio.getRatioEighth().getDa();
        } else {
            winningMoney += lastSmpBet.getBetEighth().getXiao() * lastSMPRatio.getRatioEighth().getXiao();
        }

        if (lotteryResult.getNineth() > 5) {
            winningMoney += lastSmpBet.getBetNineth().getDa() * lastSMPRatio.getRatioNineth().getDa();
        } else {
            winningMoney += lastSmpBet.getBetNineth().getXiao() * lastSMPRatio.getRatioNineth().getXiao();
        }

        if (lotteryResult.getTenth() > 5) {
            winningMoney += lastSmpBet.getBetTenth().getDa() * lastSMPRatio.getRatioTenth().getDa();
        } else {
            winningMoney += lastSmpBet.getBetTenth().getXiao() * lastSMPRatio.getRatioTenth().getXiao();
        }

        logger.info("[Operation - Summary] Winning money for round {} is {}", lastSmpBet.getRound(), winningMoney);

        return winningMoney;
    }

    private double decideBetChip(SMPSingleBet smpSingleBet, SingleBetCategory category) {
        Double chip = Double.valueOf(Config.getSmpChip());
        double betChip = 0;
        switch (category) {
            case DA:
                betChip = smpSingleBet.getDa() * 2 + chip;
                break;
            case XIAO:
                betChip = smpSingleBet.getXiao() * 2 + chip;
                break;
            case DAN:
                betChip = smpSingleBet.getDan() * 2 + chip;
                break;
            case SHUANG:
                betChip = smpSingleBet.getShuang() * 2 + chip;
                break;
            case LON:
                betChip = smpSingleBet.getLon() * 2 + chip;
                break;
            case HU:
                betChip = smpSingleBet.getHu() * 2 + chip;
                break;
        }
        if (betChip / chip > 2 << (Config.getSmpLevels() - 1)) {
            return chip;
        } else {
            return betChip;
        }
    }

    private double calculateMoney(double money, double value) {
        return money + value;
    }

    private void betForFirst(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "冠军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetFirst(generateSingleBet(category, chip));
        String name = getInputName(category, 1);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForSecond(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "亚军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetSecond(generateSingleBet(category, chip));
        String name = getInputName(category, 2);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForThird(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "季军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetThird(generateSingleBet(category, chip));
        String name = getInputName(category, 3);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForFourth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "四", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetFourth(generateSingleBet(category, chip));
        String name = getInputName(category, 4);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForFifth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "五", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetFifth(generateSingleBet(category, chip));
        String name = getInputName(category, 5);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForSixth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "六", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetSixth(generateSingleBet(category, chip));
        String name = getInputName(category, 6);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForSeventh(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "七", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetSeventh(generateSingleBet(category, chip));
        String name = getInputName(category, 7);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForEighth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "八", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetEighth(generateSingleBet(category, chip));
        String name = getInputName(category, 8);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForNineth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "九", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetNineth(generateSingleBet(category, chip));
        String name = getInputName(category, 9);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private void betForTenth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "十", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetTenth(generateSingleBet(category, chip));
        String name = getInputName(category, 10);
        sendKeys(driver, name, String.valueOf(chip));
    }

    private SMPSingleBet generateSingleBet(SingleBetCategory category, Double chip) {
        SMPSingleBet smpSingleBet = new SMPSingleBet();
        switch (category) {
            case DA:
                smpSingleBet.setDa(chip);
                break;
            case XIAO:
                smpSingleBet.setXiao(chip);
                break;
            case DAN:
                smpSingleBet.setDan(chip);
                break;
            case SHUANG:
                smpSingleBet.setShuang(chip);
                break;
            case LON:
                smpSingleBet.setLon(chip);
                break;
            case HU:
                smpSingleBet.setHu(chip);
                break;
        }
        return smpSingleBet;
    }

    private void sendKeys(WebDriver driver, String name, String chip) {
        DriverUtils.returnOnFindingElementEqualsName(driver, By.tagName("input"), name).sendKeys(chip);
    }

    private String getInputName(SingleBetCategory category, int position) {
        String name = "";
        switch (category) {
            case DA:
                name = String.format("b_%s_2_1", position);
                break;
            case XIAO:
                name = String.format("b_%s_2_2", position);
                break;
            case DAN:
                name = String.format("b_%s_1_1", position);
                break;
            case SHUANG:
                name = String.format("b_%s_1_2", position);
                break;
            case LON:
                name = String.format("b_%s_6_1", position);
                break;
            case HU:
                name = String.format("b_%s_6_2", position);
                break;
        }

        return name;
    }
}
