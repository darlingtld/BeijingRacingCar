package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.SMPBet;
import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleBet;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.SMPBetRepository;
import com.lingda.gamble.repository.SMPRatioRepository;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//北京赛车 双面盘下注
@Component
public class BetForSMPOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForSMPOperation.class);

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
        logger.info("[Operation - Bet] Bet for 北京赛车 - 双面盘");

        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - 双面盘 - 期数 {}", round);
        SMPRatio smpRatio = smpRatioRepository.findByRound(round);
        if (smpRatio == null) {
            logger.info("[Operation - Bet] No ratio information for 北京赛车 - 双面盘 - 期数 {}", round);
            return;
        }

        logger.info("[Operation - Bet] Get last lottery result for 北京赛车 - 双面盘 - 期数 {}", round - 1);
        LotteryResult lotteryResult = lotteryResultRepository.findByRound(round - 1);
        if (lotteryResult == null) {
            logger.info("[Operation - Bet] No last lottery result for 北京赛车 - 双面盘 - 期数 {}", round - 1);
            return;
        }

//      check if the bet is already done
        if (smpBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 北京赛车 - 双面盘 - 期数 {}", round);
            return;
        }

        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - 双面盘");
        SMPBet lastSmpBet = smpBetRepository.findByRound(round - 1);
        SMPBet smpBet = new SMPBet();
        smpBet.setRound(round);
        if (lastSmpBet == null) {
            logger.info("[Operation - Bet] No last bet for 北京赛车 - 双面盘 - 期数 {}", round - 1);
            //      投注 冠军 大
            betForFirstDa(smpBet, chip);
        } else {
//            check if last bet is a winner
            if (lotteryResult.getFirst() > 5) {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - 双面盘 - 期数 {} - 结果 - [{} {}]", "冠军", "大", round - 1, lotteryResult.getFirst(), "胜利");
//                winner.  继续下注大，不加筹码
                //      投注 冠军 大
                betForFirstDa(smpBet, chip);

            } else {
                logger.info("[Operation - Bet] Last bet [{} {}] for 北京赛车 - 双面盘 - 期数 {} - 结果 - [{} {}]", "冠军", "大", round - 1, lotteryResult.getFirst(), "失败");
//                loser.  增加筹码
                //      投注 冠军 大
                betForFirstDa(smpBet, lastSmpBet.getBetFirst().getDa() * 2);
            }
        }
        smpBetRepository.save(smpBet);

    }

    private void betForFirstDa(SMPBet smpBet, Double chip) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - 双面盘 - 期数 {} - 金额 - {}", "冠军", "大", smpBet.getRound(), chip);
        SMPSingleBet smpSingleBet = new SMPSingleBet();
        smpSingleBet.setDa(chip);
        smpBet.setBetFirst(smpSingleBet);
    }
}
