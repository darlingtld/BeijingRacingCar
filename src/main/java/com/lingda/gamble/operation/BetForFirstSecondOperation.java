//package com.lingda.gamble.operation;
//
//import com.lingda.gamble.model.FirstSecondBet;
//import com.lingda.gamble.model.FirstSecondRatio;
//import com.lingda.gamble.model.LotteryResult;
//import com.lingda.gamble.model.SMPBet;
//import com.lingda.gamble.model.SMPRatio;
//import com.lingda.gamble.model.SMPSingleBet;
//import com.lingda.gamble.model.SingleBetCategory;
//import com.lingda.gamble.repository.FirstSecondBetRepository;
//import com.lingda.gamble.repository.FirstSecondRatioRepository;
//import com.lingda.gamble.repository.LotteryResultRepository;
//import com.lingda.gamble.repository.SMPBetRepository;
//import com.lingda.gamble.repository.SMPRatioRepository;
//import com.lingda.gamble.util.DriverUtils;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
////北京赛车 冠亚军下注
//@Component
//public class BetForFirstSecondOperation {
//
//    private static final Logger logger = LoggerFactory.getLogger(BetForFirstSecondOperation.class);
//
//    private static final String PLAYGROUND = "冠亚军";
//
//    @Value("${gamble.bet.money}")
//    private double money;
//
//    @Value("${gamble.bet.chip}")
//    private double chip;
//
//    @Value("${gamble.bet.smp.level}")
//    private int level;
//
//    private final FirstSecondBetRepository firstSecondBetRepository;
//
//    private final FirstSecondRatioRepository firstSecondRatioRepository;
//
//    private final LotteryResultRepository lotteryResultRepository;
//
//    @Autowired
//    public BetForFirstSecondOperation(FirstSecondBetRepository firstSecondBetRepository,
//                                      FirstSecondRatioRepository firstSecondRatioRepository,
//                                      LotteryResultRepository lotteryResultRepository) {
//        this.firstSecondBetRepository = firstSecondBetRepository;
//        this.firstSecondRatioRepository = firstSecondRatioRepository;
//        this.lotteryResultRepository = lotteryResultRepository;
//    }
//
//    public boolean doBet(WebDriver driver, Integer round) throws InterruptedException {
//        if (round == null) {
//            logger.info("[Operation - Bet] 当前无法下注");
//            return false;
//        }
//        logger.info("[Operation - Bet] Bet for 北京赛车 - {}", PLAYGROUND);
//
//        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
//        FirstSecondRatio ratio = firstSecondRatioRepository.findByRound(round);
//        if (ratio == null) {
//            logger.info("[Operation - Bet] No ratio information for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
//            return false;
//        }
//
//        logger.info("[Operation - Bet] Get last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
//        LotteryResult lastLotteryResult = lotteryResultRepository.findByRound(round - 1);
//        if (lastLotteryResult == null) {
//            logger.info("[Operation - Bet] No last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
//            return false;
//        }
//
////      check if the bet is already done
//        if (firstSecondBetRepository.findByRound(round) != null) {
//            logger.info("[Operation - Bet] Already bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
//            return false;
//        }
//
//        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - {}", PLAYGROUND);
//        FirstSecondBet lastBet = firstSecondBetRepository.findByRound(round - 1);
//        //            结算上次中奖情况
//        logger.info("=============== 金额 ===============");
//        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
//        logger.info("我的余额:{}", money);
//        logger.info("====================================");
//
//        SMPBet smpBet = new SMPBet();
//        smpBet.setRound(round);
////        ============== 策略逻辑 ==============
//        if (lastBet == null) {
//            logger.info("[Operation - Bet] No last bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
//            //      投注 冠-五 大
//            betForFirst(smpBet, chip, SingleBetCategory.DA, driver);
//            betForSecond(smpBet, chip, SingleBetCategory.DA, driver);
//            betForThird(smpBet, chip, SingleBetCategory.DA, driver);
//            betForFourth(smpBet, chip, SingleBetCategory.DA, driver);
//            betForFifth(smpBet, chip, SingleBetCategory.DA, driver);
//            //      投注 六-十 小
//            betForSixth(smpBet, chip, SingleBetCategory.XIAO, driver);
//            betForSeventh(smpBet, chip, SingleBetCategory.XIAO, driver);
//            betForEighth(smpBet, chip, SingleBetCategory.XIAO, driver);
//            betForNineth(smpBet, chip, SingleBetCategory.XIAO, driver);
//            betForTenth(smpBet, chip, SingleBetCategory.XIAO, driver);
//            money = calculateMoney(money, -10 * chip);
//        } else {
////            check if last bet is a winner
////            check 冠军
//            if (lastLotteryResult.getFirst() > 5) {
//                if (lastBet.getBetFirst().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 冠军 大
//                    double betChip = decideBetChip(lastBet.getBetFirst(), SingleBetCategory.DA);
//                    betForFirst(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "失败");
////                    //      投注 冠军 大
//                    betForFirst(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetFirst().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 冠军 小
//                    double betChip = decideBetChip(lastBet.getBetFirst(), SingleBetCategory.XIAO);
//                    betForFirst(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFirst(), "失败");
////                loser.  不加筹码
//                    //      投注 冠军 小
//                    betForFirst(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 亚军
//            if (lastLotteryResult.getSecond() > 5) {
//                if (lastBet.getBetSecond().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 亚军 大
//                    double betChip = decideBetChip(lastBet.getBetSecond(), SingleBetCategory.DA);
//                    betForSecond(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "失败");
////                    //      投注 亚军 大
//                    betForSecond(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetSecond().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 亚军 小
//                    double betChip = decideBetChip(lastBet.getBetSecond(), SingleBetCategory.XIAO);
//                    betForSecond(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSecond(), "失败");
////                loser.  不加筹码
//                    //      投注 亚军 小
//                    betForSecond(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 季军
//            if (lastLotteryResult.getThird() > 5) {
//                if (lastBet.getBetThird().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 季军 大
//                    double betChip = decideBetChip(lastBet.getBetThird(), SingleBetCategory.DA);
//                    betForThird(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "失败");
////                    //      投注 季军 大
//                    betForThird(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetThird().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 季军 小
//                    double betChip = decideBetChip(lastBet.getBetThird(), SingleBetCategory.XIAO);
//                    betForThird(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getThird(), "失败");
////                loser.  不加筹码
//                    //      投注 季军 小
//                    betForThird(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 四
//            if (lastLotteryResult.getFourth() > 5) {
//                if (lastBet.getBetFourth().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 四 大
//                    double betChip = decideBetChip(lastBet.getBetFourth(), SingleBetCategory.DA);
//                    betForFourth(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "失败");
////                    //      投注 四 大
//                    betForFourth(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetFourth().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 四 小
//                    double betChip = decideBetChip(lastBet.getBetFourth(), SingleBetCategory.XIAO);
//                    betForFourth(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFourth(), "失败");
////                loser.  不加筹码
//                    //      投注 四 小
//                    betForFourth(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 五
//            if (lastLotteryResult.getFifth() > 5) {
//                if (lastBet.getBetFifth().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 五 大
//                    double betChip = decideBetChip(lastBet.getBetFifth(), SingleBetCategory.DA);
//                    betForFifth(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "失败");
////                    //      投注 五 大
//                    betForFifth(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetFifth().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 五 小
//                    double betChip = decideBetChip(lastBet.getBetFifth(), SingleBetCategory.XIAO);
//                    betForFifth(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getFifth(), "失败");
////                loser.  不加筹码
//                    //      投注 五 小
//                    betForFifth(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
////            check 六
//            if (lastLotteryResult.getSixth() > 5) {
//                if (lastBet.getBetSixth().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 六 大
//                    double betChip = decideBetChip(lastBet.getBetSixth(), SingleBetCategory.DA);
//                    betForSixth(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "失败");
////                    //      投注 六 大
//                    betForSixth(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetSixth().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 六 小
//                    double betChip = decideBetChip(lastBet.getBetSixth(), SingleBetCategory.XIAO);
//                    betForSixth(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSixth(), "失败");
////                loser.  不加筹码
//                    //      投注 六 小
//                    betForSixth(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 七
//            if (lastLotteryResult.getSeventh() > 5) {
//                if (lastBet.getBetSeventh().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 七 大
//                    double betChip = decideBetChip(lastBet.getBetSeventh(), SingleBetCategory.DA);
//                    betForSeventh(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "失败");
////                    //      投注 七 大
//                    betForSeventh(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetSeventh().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 七 小
//                    double betChip = decideBetChip(lastBet.getBetSeventh(), SingleBetCategory.XIAO);
//                    betForSeventh(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getSeventh(), "失败");
////                loser.  不加筹码
//                    //      投注 七 小
//                    betForSeventh(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 八
//            if (lastLotteryResult.getEighth() > 5) {
//                if (lastBet.getBetEighth().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 八 大
//                    double betChip = decideBetChip(lastBet.getBetEighth(), SingleBetCategory.DA);
//                    betForEighth(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "失败");
////                    //      投注 八 大
//                    betForEighth(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetEighth().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 八 小
//                    double betChip = decideBetChip(lastBet.getBetEighth(), SingleBetCategory.XIAO);
//                    betForEighth(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getEighth(), "失败");
////                loser.  不加筹码
//                    //      投注 八 小
//                    betForEighth(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 九
//            if (lastLotteryResult.getNineth() > 5) {
//                if (lastBet.getBetNineth().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 九 大
//                    double betChip = decideBetChip(lastBet.getBetNineth(), SingleBetCategory.DA);
//                    betForNineth(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "失败");
////                    //      投注 九 大
//                    betForNineth(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetNineth().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 九 小
//                    double betChip = decideBetChip(lastBet.getBetNineth(), SingleBetCategory.XIAO);
//                    betForNineth(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getNineth(), "失败");
////                loser.  不加筹码
//                    //      投注 九 小
//                    betForNineth(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//
//            //            check 十
//            if (lastLotteryResult.getTenth() > 5) {
//                if (lastBet.getBetTenth().getDa() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "胜利");
////                winner.  继续下注大，增加筹码
//                    //      投注 十 大
//                    double betChip = decideBetChip(lastBet.getBetTenth(), SingleBetCategory.DA);
//                    betForTenth(smpBet, betChip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "失败");
////                    //      投注 十 大
//                    betForTenth(smpBet, chip, SingleBetCategory.DA, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            } else {
//                if (lastBet.getBetTenth().getXiao() > 0) {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "胜利");
////                winner.  继续下注小，增加筹码
//                    //      投注 十 小
//                    double betChip = decideBetChip(lastBet.getBetTenth(), SingleBetCategory.XIAO);
//                    betForTenth(smpBet, betChip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -betChip);
//                } else {
//                    logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lastLotteryResult.getTenth(), "失败");
////                loser.  不加筹码
//                    //      投注 十 小
//                    betForTenth(smpBet, chip, SingleBetCategory.XIAO, driver);
//                    money = calculateMoney(money, -chip);
//                }
//            }
//        }
//
//        logger.info("=============== 金额 ===============");
//        logger.info("我的余额:{}", money);
//        logger.info("====================================");
//        smpBetRepository.save(smpBet);
//        return true;
//
//    }
//
//    private double calculateLastLotteryResult(FirstSecondBet lastBet, LotteryResult lotteryResult) {
//        if (lastBet == null) {
//            return 0;
//        }
//        double winningMoney = 0;
//        FirstSecondRatio lastRatio = firstSecondRatioRepository.findByRound(lastBet.getRound());
//        if (lotteryResult.getFirst() > 5) {
//            winningMoney += lastBet.getBetFirst().getDa() * lastRatio.getRatioFirst().getDa();
//        } else {
//            winningMoney += lastBet.getBetFirst().getXiao() * lastRatio.getRatioFirst().getXiao();
//        }
//
//        if (lotteryResult.getSecond() > 5) {
//            winningMoney += lastBet.getBetSecond().getDa() * lastRatio.getRatioSecond().getDa();
//        } else {
//            winningMoney += lastBet.getBetSecond().getXiao() * lastRatio.getRatioSecond().getXiao();
//        }
//
//        if (lotteryResult.getThird() > 5) {
//            winningMoney += lastBet.getBetThird().getDa() * lastRatio.getRatioThird().getDa();
//        } else {
//            winningMoney += lastBet.getBetThird().getXiao() * lastRatio.getRatioThird().getXiao();
//        }
//
//        if (lotteryResult.getFourth() > 5) {
//            winningMoney += lastBet.getBetFourth().getDa() * lastRatio.getRatioFourth().getDa();
//        } else {
//            winningMoney += lastBet.getBetFourth().getXiao() * lastRatio.getRatioFourth().getXiao();
//        }
//
//        if (lotteryResult.getFifth() > 5) {
//            winningMoney += lastBet.getBetFifth().getDa() * lastRatio.getRatioFifth().getDa();
//        } else {
//            winningMoney += lastBet.getBetFifth().getXiao() * lastRatio.getRatioFifth().getXiao();
//        }
//
//        if (lotteryResult.getSixth() > 5) {
//            winningMoney += lastBet.getBetSixth().getDa() * lastRatio.getRatioSixth().getDa();
//        } else {
//            winningMoney += lastBet.getBetSixth().getXiao() * lastRatio.getRatioSixth().getXiao();
//        }
//
//        if (lotteryResult.getSeventh() > 5) {
//            winningMoney += lastBet.getBetSeventh().getDa() * lastRatio.getRatioSeventh().getDa();
//        } else {
//            winningMoney += lastBet.getBetSeventh().getXiao() * lastRatio.getRatioSeventh().getXiao();
//        }
//
//        if (lotteryResult.getEighth() > 5) {
//            winningMoney += lastBet.getBetEighth().getDa() * lastRatio.getRatioEighth().getDa();
//        } else {
//            winningMoney += lastBet.getBetEighth().getXiao() * lastRatio.getRatioEighth().getXiao();
//        }
//
//        if (lotteryResult.getNineth() > 5) {
//            winningMoney += lastBet.getBetNineth().getDa() * lastRatio.getRatioNineth().getDa();
//        } else {
//            winningMoney += lastBet.getBetNineth().getXiao() * lastRatio.getRatioNineth().getXiao();
//        }
//
//        if (lotteryResult.getTenth() > 5) {
//            winningMoney += lastBet.getBetTenth().getDa() * lastRatio.getRatioTenth().getDa();
//        } else {
//            winningMoney += lastBet.getBetTenth().getXiao() * lastRatio.getRatioTenth().getXiao();
//        }
//
//        logger.info("[Operation - Summary] Winning money for round {} is {}", lastBet.getRound(), winningMoney);
//
//        return winningMoney;
//    }
//
//    private double decideBetChip(SMPSingleBet smpSingleBet, SingleBetCategory category) {
//        double betChip = 0;
//        switch (category) {
//            case DA:
//                betChip = smpSingleBet.getDa() * 2;
//                break;
//            case XIAO:
//                betChip = smpSingleBet.getXiao() * 2;
//                break;
//            case DAN:
//                betChip = smpSingleBet.getDan() * 2;
//                break;
//            case SHUANG:
//                betChip = smpSingleBet.getShuang() * 2;
//                break;
//            case LON:
//                betChip = smpSingleBet.getLon() * 2;
//                break;
//            case HU:
//                betChip = smpSingleBet.getHu() * 2;
//                break;
//        }
//        if (betChip / chip > 2 << (level - 1)) {
//            return chip;
//        } else {
//            return betChip;
//        }
//    }
//
//    private double calculateMoney(double money, double value) {
//        return money + value;
//    }
//
//    private void betForFirst(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "冠军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetFirst(generateSingleBet(category, chip));
//        String name = getInputName(category, 1);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForSecond(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "亚军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetSecond(generateSingleBet(category, chip));
//        String name = getInputName(category, 2);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForThird(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "季军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetThird(generateSingleBet(category, chip));
//        String name = getInputName(category, 3);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForFourth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "四", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetFourth(generateSingleBet(category, chip));
//        String name = getInputName(category, 4);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForFifth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "五", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetFifth(generateSingleBet(category, chip));
//        String name = getInputName(category, 5);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForSixth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "六", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetSixth(generateSingleBet(category, chip));
//        String name = getInputName(category, 6);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForSeventh(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "七", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetSeventh(generateSingleBet(category, chip));
//        String name = getInputName(category, 7);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForEighth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "八", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetEighth(generateSingleBet(category, chip));
//        String name = getInputName(category, 8);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForNineth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "九", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetNineth(generateSingleBet(category, chip));
//        String name = getInputName(category, 9);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private void betForTenth(SMPBet smpBet, Double chip, SingleBetCategory category, WebDriver driver) {
//        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "十", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
//        smpBet.setBetTenth(generateSingleBet(category, chip));
//        String name = getInputName(category, 10);
//        sendKeys(driver, name, String.valueOf(chip));
//    }
//
//    private SMPSingleBet generateSingleBet(SingleBetCategory category, Double chip) {
//        SMPSingleBet smpSingleBet = new SMPSingleBet();
//        switch (category) {
//            case DA:
//                smpSingleBet.setDa(chip);
//                break;
//            case XIAO:
//                smpSingleBet.setXiao(chip);
//                break;
//            case DAN:
//                smpSingleBet.setDan(chip);
//                break;
//            case SHUANG:
//                smpSingleBet.setShuang(chip);
//                break;
//            case LON:
//                smpSingleBet.setLon(chip);
//                break;
//            case HU:
//                smpSingleBet.setHu(chip);
//                break;
//        }
//        return smpSingleBet;
//    }
//
//    private void sendKeys(WebDriver driver, String name, String chip) {
//        DriverUtils.returnOnFindingElementEqualsName(driver, By.tagName("input"), name).sendKeys(chip);
//    }
//
//    private String getInputName(SingleBetCategory category, int position) {
//        String name = "";
//        switch (category) {
//            case DA:
//                name = String.format("b_%s_2_1", position);
//                break;
//            case XIAO:
//                name = String.format("b_%s_2_2", position);
//                break;
//            case DAN:
//                name = String.format("b_%s_1_1", position);
//                break;
//            case SHUANG:
//                name = String.format("b_%s_1_2", position);
//                break;
//            case LON:
//                name = String.format("b_%s_6_1", position);
//                break;
//            case HU:
//                name = String.format("b_%s_6_2", position);
//                break;
//        }
//
//        return name;
//    }
//}