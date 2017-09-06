package com.lingda.gamble.operation;

import com.lingda.gamble.model.SMPBet;
import com.lingda.gamble.model.SMPSingleBet;
import com.lingda.gamble.repository.SMPBetRepository;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//北京赛车 双面盘下注
@Component
public class BetForSMPOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForSMPOperation.class);

    @Autowired
    private SMPBetRepository smpBetRepository;

    public void doBet(WebDriver driver) throws InterruptedException {
        logger.info("[Operation - Bet] Bet for 北京赛车 - 双面盘");
//      投注 冠军 大
        SMPBet smpBet = new SMPBet();
        SMPSingleBet smpSingleBet = new SMPSingleBet();
        smpSingleBet.setDa(1);
        smpBet.setBetFirst(smpSingleBet);
        smpBetRepository.save(smpBet);
    }
}
