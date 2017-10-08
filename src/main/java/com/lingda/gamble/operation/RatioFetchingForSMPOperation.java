package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.SMPRatioRepository;
import com.lingda.gamble.repository.WinLostMoneyRepository;
import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Driver;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//抓取北京赛车 双面盘赔率
@Component
public class RatioFetchingForSMPOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForSMPOperation.class);

    private static final Pattern roundPattern = Pattern.compile("^([0-9]+)\\s+期");

    private static final String PLAYGROUND = "双面盘";

    private final SMPRatioRepository smpRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForSMPOperation(SMPRatioRepository smpRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.smpRatioRepository = smpRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
        this.winLostMoneyRepository = winLostMoneyRepository;
    }

    public Integer doFetchRatio(WebDriver driver) throws InterruptedException {
        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - FetchRatio] Fetch lottery result for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取之前开奖信息
        LotteryResult lotteryResult = new LotteryResult();
        WebElement round = DriverUtils.returnOnFindingElement(driver, By.id("newPhase"));
        lotteryResult.setRound(Integer.parseInt(round.getText()));
        WebElement lotteryResultEle = DriverUtils.returnOnFindingElement(driver, By.id("prevBall"));
        List<WebElement> spanList = lotteryResultEle.findElements(By.tagName("span"));

        lotteryResult.setFirst(Integer.parseInt(spanList.get(0).getAttribute("class").substring(3)));
        lotteryResult.setSecond(Integer.parseInt(spanList.get(1).getAttribute("class").substring(3)));
        lotteryResult.setThird(Integer.parseInt(spanList.get(2).getAttribute("class").substring(3)));
        lotteryResult.setFourth(Integer.parseInt(spanList.get(3).getAttribute("class").substring(3)));
        lotteryResult.setFifth(Integer.parseInt(spanList.get(4).getAttribute("class").substring(3)));
        lotteryResult.setSixth(Integer.parseInt(spanList.get(5).getAttribute("class").substring(3)));
        lotteryResult.setSeventh(Integer.parseInt(spanList.get(6).getAttribute("class").substring(3)));
        lotteryResult.setEighth(Integer.parseInt(spanList.get(7).getAttribute("class").substring(3)));
        lotteryResult.setNineth(Integer.parseInt(spanList.get(8).getAttribute("class").substring(3)));
        lotteryResult.setTenth(Integer.parseInt(spanList.get(9).getAttribute("class").substring(3)));
        logger.info(lotteryResult.toString());
        if (lotteryResultRepository.findByRound(lotteryResult.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save lotteryResult to DB for 北京赛车 - {} - 开奖信息", PLAYGROUND);
            lotteryResultRepository.save(lotteryResult);
        }

        logger.info("[Operation - FetchRatio] Fetch round for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取当前下注期数
        WebElement element = DriverUtils.returnOnFindingElement(driver, By.id("NowJq"));

        SMPRatio smpRatio = new SMPRatio();
        smpRatio.setRound(Integer.parseInt(element.getText()));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("profit"));
        Double todayWinLostMoney = Double.parseDouble(todayWinLost.getText());
        logger.info("[Operation - FetchRatio] Today win/lost for 北京赛车 - {} - {}", PLAYGROUND, todayWinLostMoney);
        WinLostMoney winLostMoney = new WinLostMoney();
        winLostMoney.setAccountName(Store.getAccountName());
        winLostMoney.setRound(smpRatio.getRound());
        winLostMoney.setWinLostMoney(todayWinLostMoney);
        if (winLostMoneyRepository.findByRoundAndAccountName(winLostMoney.getRound(), winLostMoney.getAccountName()) == null) {
            logger.info("[Operation - FetchRatio] Save today win/lost for 北京赛车 - {}", PLAYGROUND);
            winLostMoneyRepository.save(winLostMoney);
        }

        WebElement gameBox = DriverUtils.returnOnFindingElement(driver, By.id("gameBox"));
        SMPSingleRatio smpSingleRatio1 = new SMPSingleRatio();
        WebElement ratio = gameBox.findElement(By.id("odds_2_11"));
        if (ratio.getText().equals("-")) {
//                正在开奖 或者 正在刷新
            return null;
        }
        smpSingleRatio1.setDa(Double.parseDouble(ratio.getText()));
        smpSingleRatio1.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_2_12")).getText()));
        smpSingleRatio1.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_3_13")).getText()));
        smpSingleRatio1.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_3_14")).getText()));
        smpSingleRatio1.setLon(Double.parseDouble(gameBox.findElement(By.id("odds_4_15")).getText()));
        smpSingleRatio1.setHu(Double.parseDouble(gameBox.findElement(By.id("odds_4_16")).getText()));
        smpRatio.setRatioFirst(smpSingleRatio1);

        SMPSingleRatio smpSingleRatio2 = new SMPSingleRatio();
        smpSingleRatio2.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_6_27")).getText()));
        smpSingleRatio2.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_6_28")).getText()));
        smpSingleRatio2.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_7_29")).getText()));
        smpSingleRatio2.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_7_30")).getText()));
        smpSingleRatio2.setLon(Double.parseDouble(gameBox.findElement(By.id("odds_8_31")).getText()));
        smpSingleRatio2.setHu(Double.parseDouble(gameBox.findElement(By.id("odds_8_32")).getText()));
        smpRatio.setRatioSecond(smpSingleRatio2);

        SMPSingleRatio smpSingleRatio3 = new SMPSingleRatio();
        smpSingleRatio3.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_10_43")).getText()));
        smpSingleRatio3.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_10_44")).getText()));
        smpSingleRatio3.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_11_45")).getText()));
        smpSingleRatio3.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_11_46")).getText()));
        smpSingleRatio3.setLon(Double.parseDouble(gameBox.findElement(By.id("odds_12_47")).getText()));
        smpSingleRatio3.setHu(Double.parseDouble(gameBox.findElement(By.id("odds_12_48")).getText()));
        smpRatio.setRatioThird(smpSingleRatio3);

        SMPSingleRatio smpSingleRatio4 = new SMPSingleRatio();
        smpSingleRatio4.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_14_59")).getText()));
        smpSingleRatio4.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_14_60")).getText()));
        smpSingleRatio4.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_15_61")).getText()));
        smpSingleRatio4.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_15_62")).getText()));
        smpSingleRatio4.setLon(Double.parseDouble(gameBox.findElement(By.id("odds_16_63")).getText()));
        smpSingleRatio4.setHu(Double.parseDouble(gameBox.findElement(By.id("odds_16_64")).getText()));
        smpRatio.setRatioFourth(smpSingleRatio4);

        SMPSingleRatio smpSingleRatio5 = new SMPSingleRatio();
        smpSingleRatio5.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_18_75")).getText()));
        smpSingleRatio5.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_18_76")).getText()));
        smpSingleRatio5.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_19_77")).getText()));
        smpSingleRatio5.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_19_78")).getText()));
        smpSingleRatio5.setLon(Double.parseDouble(gameBox.findElement(By.id("odds_20_79")).getText()));
        smpSingleRatio5.setHu(Double.parseDouble(gameBox.findElement(By.id("odds_20_80")).getText()));
        smpRatio.setRatioFifth(smpSingleRatio5);

        SMPSingleRatio smpSingleRatio6 = new SMPSingleRatio();
        smpSingleRatio6.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_22_91")).getText()));
        smpSingleRatio6.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_22_92")).getText()));
        smpSingleRatio6.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_23_93")).getText()));
        smpSingleRatio6.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_23_94")).getText()));
        smpRatio.setRatioSixth(smpSingleRatio6);

        SMPSingleRatio smpSingleRatio7 = new SMPSingleRatio();
        smpSingleRatio7.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_25_105")).getText()));
        smpSingleRatio7.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_25_106")).getText()));
        smpSingleRatio7.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_26_107")).getText()));
        smpSingleRatio7.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_26_108")).getText()));
        smpRatio.setRatioSeventh(smpSingleRatio7);

        SMPSingleRatio smpSingleRatio8 = new SMPSingleRatio();
        smpSingleRatio8.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_28_119")).getText()));
        smpSingleRatio8.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_28_120")).getText()));
        smpSingleRatio8.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_29_121")).getText()));
        smpSingleRatio8.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_29_122")).getText()));
        smpRatio.setRatioEighth(smpSingleRatio8);

        SMPSingleRatio smpSingleRatio9 = new SMPSingleRatio();
        smpSingleRatio9.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_31_133")).getText()));
        smpSingleRatio9.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_31_134")).getText()));
        smpSingleRatio9.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_32_135")).getText()));
        smpSingleRatio9.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_32_136")).getText()));
        smpRatio.setRatioNineth(smpSingleRatio9);

        SMPSingleRatio smpSingleRatio10 = new SMPSingleRatio();
        smpSingleRatio10.setDa(Double.parseDouble(gameBox.findElement(By.id("odds_34_147")).getText()));
        smpSingleRatio10.setXiao(Double.parseDouble(gameBox.findElement(By.id("odds_34_148")).getText()));
        smpSingleRatio10.setDan(Double.parseDouble(gameBox.findElement(By.id("odds_35_149")).getText()));
        smpSingleRatio10.setShuang(Double.parseDouble(gameBox.findElement(By.id("odds_35_150")).getText()));
        smpRatio.setRatioTenth(smpSingleRatio10);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(smpRatio.toString());
        if (smpRatioRepository.findByRound(smpRatio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            smpRatioRepository.save(smpRatio);
        }
        if (winLostMoney.getWinLostMoney() + Config.getLostThreshold() < 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Blast %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }
        if (winLostMoney.getWinLostMoney() - Config.getWinThreshold() > 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Wow %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }
        return smpRatio.getRound();
    }
}
