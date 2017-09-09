package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.SMPBet;
import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleBet;
import com.lingda.gamble.model.SingleBetCategory;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.SMPBetRepository;
import com.lingda.gamble.repository.SMPRatioRepository;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

//北京赛车 双面盘下注
@Component
public class BetForSMPOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForSMPOperation.class);

    private static final String PLAYGROUND = "双面盘";

    @Value("${gamble.bet.chip}")
    private double chip;

    private final SMPBetRepository smpBetRepository;

    private final SMPRatioRepository smpRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public BetForSMPOperation(SMPBetRepository smpBetRepository,
                              SMPRatioRepository smpRatioRepository,
                              LotteryResultRepository lotteryResultRepository) {
        this.smpBetRepository = smpBetRepository;
        this.smpRatioRepository = smpRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public void doBet(WebDriver driver, Integer round) throws InterruptedException {
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return;
        }
        logger.info("[Operation - Bet] Bet for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
        SMPRatio smpRatio = smpRatioRepository.findByRound(round);
        if (smpRatio == null) {
            logger.info("[Operation - Bet] No ratio information for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return;
        }

        logger.info("[Operation - Bet] Get last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
        LotteryResult lotteryResult = lotteryResultRepository.findByRound(round - 1);
        if (lotteryResult == null) {
            logger.info("[Operation - Bet] No last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
            return;
        }

//      check if the bet is already done
        if (smpBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return;
        }

        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - {}", PLAYGROUND);
        SMPBet lastSmpBet = smpBetRepository.findByRound(round - 1);
        SMPBet smpBet = new SMPBet();
        smpBet.setRound(round);
        if (lastSmpBet == null) {
            logger.info("[Operation - Bet] No last bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
            //      投注 冠-五 大
            betForFirst(smpBet, chip, SingleBetCategory.DA);
            betForSecond(smpBet, chip, SingleBetCategory.DA);
            betForThird(smpBet, chip, SingleBetCategory.DA);
            betForFourth(smpBet, chip, SingleBetCategory.DA);
            betForFifth(smpBet, chip, SingleBetCategory.DA);
            //      投注 六-十 小
            betForSixth(smpBet, chip, SingleBetCategory.XIAO);
            betForSeventh(smpBet, chip, SingleBetCategory.XIAO);
            betForEighth(smpBet, chip, SingleBetCategory.XIAO);
            betForNinth(smpBet, chip, SingleBetCategory.XIAO);
            betForTenth(smpBet, chip, SingleBetCategory.XIAO);
        } else {
//            check if last bet is a winner
//            check 冠军
            if (lotteryResult.getFirst() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getFirst(), "胜利");
//                winner.  继续下注大，不加筹码
                //      投注 冠军 大
                betForFirst(smpBet, chip, SingleBetCategory.DA);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "冠军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getFirst(), "失败");
//                loser.  增加筹码
                //      投注 冠军 大
                betForFirst(smpBet, lastSmpBet.getBetFirst() == null ? chip : lastSmpBet.getBetFirst().getDa() * 2, SingleBetCategory.DA);
            }

            //            check 亚军
            if (lotteryResult.getSecond() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getSecond(), "胜利");
//                winner.  继续下注大，不加筹码
                //      投注 亚军 大
                betForSecond(smpBet, chip, SingleBetCategory.DA);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "亚军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getSecond(), "失败");
//                loser.  增加筹码
                //      投注 亚军 大
                betForSecond(smpBet, lastSmpBet.getBetSecond() == null ? chip : lastSmpBet.getBetSecond().getDa() * 2, SingleBetCategory.DA);
            }

            //            check 季军
            if (lotteryResult.getThird() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getThird(), "胜利");
//                winner.  继续下注大，不加筹码
                //      投注 季军 大
                betForThird(smpBet, chip, SingleBetCategory.DA);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "季军", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getThird(), "失败");
//                loser.  增加筹码
                //      投注 季军 大
                betForThird(smpBet, lastSmpBet.getBetThird() == null ? chip : lastSmpBet.getBetThird().getDa() * 2, SingleBetCategory.DA);
            }

            //            check 四
            if (lotteryResult.getFourth() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getFourth(), "胜利");
//                winner.  继续下注大，不加筹码
                //      投注 四 大
                betForFourth(smpBet, chip, SingleBetCategory.DA);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "四", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getFourth(), "失败");
//                loser.  增加筹码
                //      投注 四 大
                betForFourth(smpBet, lastSmpBet.getBetFourth() == null ? chip : lastSmpBet.getBetFourth().getDa() * 2, SingleBetCategory.DA);
            }

            //            check 五
            if (lotteryResult.getFifth() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getFifth(), "胜利");
//                winner.  继续下注大，不加筹码
                //      投注 五 大
                betForFifth(smpBet, chip, SingleBetCategory.DA);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "五", SingleBetCategory.DA.getCategory(), PLAYGROUND, round - 1, lotteryResult.getFifth(), "失败");
//                loser.  增加筹码
                //      投注 五 大
                betForFifth(smpBet, lastSmpBet.getBetFifth() == null ? chip : lastSmpBet.getBetFifth().getDa() * 2, SingleBetCategory.DA);
            }

            //            check 六
            if (lotteryResult.getSixth() <= 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getSixth(), "胜利");
//                winner.  继续下注小，不加筹码
                //      投注 六 小
                betForSixth(smpBet, chip, SingleBetCategory.XIAO);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "六", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getSixth(), "失败");
//                loser.  增加筹码
                //      投注 六 小
                betForSixth(smpBet, lastSmpBet.getBetSixth() == null ? chip : lastSmpBet.getBetSixth().getXiao() * 2, SingleBetCategory.XIAO);
            }

            //            check 七
            if (lotteryResult.getSeventh() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getSeventh(), "胜利");
//                winner.  继续下注小，不加筹码
                //      投注 七 小
                betForSeventh(smpBet, chip, SingleBetCategory.XIAO);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "七", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getSeventh(), "失败");
//                loser.  增加筹码
                //      投注 七 小
                betForSeventh(smpBet, lastSmpBet.getBetSeventh() == null ? chip : lastSmpBet.getBetSeventh().getXiao() * 2, SingleBetCategory.XIAO);
            }

            //            check 八
            if (lotteryResult.getEighth() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getEighth(), "胜利");
//                winner.  继续下注小，不加筹码
                //      投注 八 小
                betForEighth(smpBet, chip, SingleBetCategory.XIAO);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "八", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getEighth(), "失败");
//                loser.  增加筹码
                //      投注 八 小
                betForEighth(smpBet, lastSmpBet.getBetEighth() == null ? chip : lastSmpBet.getBetEighth().getXiao() * 2, SingleBetCategory.XIAO);
            }

            //            check 九
            if (lotteryResult.getNineth() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getNineth(), "胜利");
//                winner.  继续下注小，不加筹码
                //      投注 九 小
                betForNinth(smpBet, chip, SingleBetCategory.XIAO);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "九", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getNineth(), "失败");
//                loser.  增加筹码
                //      投注 九 小
                betForNinth(smpBet, lastSmpBet.getBetNinth() == null ? chip : lastSmpBet.getBetNinth().getXiao() * 2, SingleBetCategory.XIAO);
            }

            //            check 十
            if (lotteryResult.getTenth() <= 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getTenth(), "胜利");
//                winner.  继续下注小，不加筹码
                //      投注 十 小
                betForTenth(smpBet, chip, SingleBetCategory.XIAO);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - {} - 期数 {} - 结果 - [{} {}]", "十", SingleBetCategory.XIAO.getCategory(), PLAYGROUND, round - 1, lotteryResult.getTenth(), "失败");
//                loser.  增加筹码
                //      投注 十 小
                betForTenth(smpBet, lastSmpBet.getBetTenth() == null ? chip : lastSmpBet.getBetTenth().getXiao() * 2, SingleBetCategory.XIAO);
            }

        }
        smpBetRepository.save(smpBet);

    }

    private void betForFirst(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "冠军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetFirst(generateSingleBet(category, chip));
    }

    private void betForSecond(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "亚军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetSecond(generateSingleBet(category, chip));
    }

    private void betForThird(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "季军", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetThird(generateSingleBet(category, chip));
    }

    private void betForFourth(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "四", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetFourth(generateSingleBet(category, chip));
    }

    private void betForFifth(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "五", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetFifth(generateSingleBet(category, chip));
    }

    private void betForSixth(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "六", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetSixth(generateSingleBet(category, chip));
    }

    private void betForSeventh(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "七", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetSeventh(generateSingleBet(category, chip));
    }

    private void betForEighth(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "八", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetEighth(generateSingleBet(category, chip));
    }

    private void betForNinth(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "九", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetNinth(generateSingleBet(category, chip));
    }

    private void betForTenth(SMPBet smpBet, Double chip, SingleBetCategory category) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "十", category.getCategory(), PLAYGROUND, smpBet.getRound(), chip);
        smpBet.setBetTenth(generateSingleBet(category, chip));
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
}
