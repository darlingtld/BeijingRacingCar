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

    private static final Pattern roundPattern = Pattern.compile("([0-9]+)\\s+期");

    private static final String PLAYGROUND = "两面盘";

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
        logger.info("[Operation - FetchRatio] Fetch ratio for 幸运飞艇 - {}", PLAYGROUND);

        logger.info("[Operation - FetchRatio] Fetch lottery result for 幸运飞艇 - {} - 期数", PLAYGROUND);
//        获取之前开奖信息
        LotteryResult lotteryResult = new LotteryResult();
        WebElement round = DriverUtils.returnOnFindingElement(driver, By.id("result_info"));
        lotteryResult.setRound(Integer.parseInt(round.getText().substring(7,16)));
        WebElement lotteryResultEle = DriverUtils.returnOnFindingElement(driver, By.id("result_balls"));
        List<WebElement> spanList = lotteryResultEle.findElements(By.tagName("span"));

        lotteryResult.setFirst(Integer.parseInt(spanList.get(0).getText()));
        lotteryResult.setSecond(Integer.parseInt(spanList.get(1).getText()));
        lotteryResult.setThird(Integer.parseInt(spanList.get(2).getText()));
        lotteryResult.setFourth(Integer.parseInt(spanList.get(3).getText()));
        lotteryResult.setFifth(Integer.parseInt(spanList.get(4).getText()));
        lotteryResult.setSixth(Integer.parseInt(spanList.get(5).getText()));
        lotteryResult.setSeventh(Integer.parseInt(spanList.get(6).getText()));
        lotteryResult.setEighth(Integer.parseInt(spanList.get(7).getText()));
        lotteryResult.setNineth(Integer.parseInt(spanList.get(8).getText()));
        lotteryResult.setTenth(Integer.parseInt(spanList.get(9).getText()));
        logger.info(lotteryResult.toString());
        if (lotteryResultRepository.findByRound(lotteryResult.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save lotteryResult to DB for 幸运飞艇 - {} - 开奖信息", PLAYGROUND);
            lotteryResultRepository.save(lotteryResult);
        }

        logger.info("[Operation - FetchRatio] Fetch round for 幸运飞艇 - {} - 期数", PLAYGROUND);
//        获取当前下注期数
        driver.switchTo().frame("frame");
        WebElement element = DriverUtils.returnOnFindingElement(driver, By.id("drawNumber"));

        SMPRatio smpRatio = new SMPRatio();
        smpRatio.setRound(Integer.parseInt(element.getText().substring(2)));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("bresult"));
        Double todayWinLostMoney = Double.parseDouble(todayWinLost.getText());
        logger.info("[Operation - FetchRatio] Today win/lost for 幸运飞艇 - {} - {}", PLAYGROUND, todayWinLostMoney);
        WinLostMoney winLostMoney = new WinLostMoney();
        winLostMoney.setAccountName(Store.getAccountName());
        winLostMoney.setRound(smpRatio.getRound());
        winLostMoney.setWinLostMoney(todayWinLostMoney);
        if (winLostMoneyRepository.findByRoundAndAccountName(winLostMoney.getRound(), winLostMoney.getAccountName()) == null) {
            logger.info("[Operation - FetchRatio] Save today win/lost for 幸运飞艇 - {}", PLAYGROUND);
            winLostMoneyRepository.save(winLostMoney);
        }


        WebElement gameBox = DriverUtils.returnOnFindingElement(driver, By.id("main"));
        SMPSingleRatio smpSingleRatio1 = new SMPSingleRatio();
        WebElement ratio = gameBox.findElement(By.id("o_DX1_D"));
        if (ratio.getText().equals("--")) {
//                正在开奖 或者 正在刷新
            return null;
        }
        smpSingleRatio1.setDa(Double.parseDouble(ratio.getText()));
        smpSingleRatio1.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX1_X")).getText()));
        smpSingleRatio1.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS1_D")).getText()));
        smpSingleRatio1.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS1_S")).getText()));
        smpSingleRatio1.setLon(Double.parseDouble(gameBox.findElement(By.id("o_LH1_L")).getText()));
        smpSingleRatio1.setHu(Double.parseDouble(gameBox.findElement(By.id("o_LH1_H")).getText()));
        smpRatio.setRatioFirst(smpSingleRatio1);

        SMPSingleRatio smpSingleRatio2 = new SMPSingleRatio();
        smpSingleRatio2.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX2_D")).getText()));
        smpSingleRatio2.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX2_X")).getText()));
        smpSingleRatio2.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS2_D")).getText()));
        smpSingleRatio2.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS2_S")).getText()));
        smpSingleRatio2.setLon(Double.parseDouble(gameBox.findElement(By.id("o_LH2_L")).getText()));
        smpSingleRatio2.setHu(Double.parseDouble(gameBox.findElement(By.id("o_LH2_H")).getText()));
        smpRatio.setRatioSecond(smpSingleRatio2);

        SMPSingleRatio smpSingleRatio3 = new SMPSingleRatio();
        smpSingleRatio3.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX3_D")).getText()));
        smpSingleRatio3.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX3_X")).getText()));
        smpSingleRatio3.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS3_D")).getText()));
        smpSingleRatio3.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS3_S")).getText()));
        smpSingleRatio3.setLon(Double.parseDouble(gameBox.findElement(By.id("o_LH3_L")).getText()));
        smpSingleRatio3.setHu(Double.parseDouble(gameBox.findElement(By.id("o_LH3_H")).getText()));
        smpRatio.setRatioThird(smpSingleRatio3);

        SMPSingleRatio smpSingleRatio4 = new SMPSingleRatio();
        smpSingleRatio4.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX4_D")).getText()));
        smpSingleRatio4.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX4_X")).getText()));
        smpSingleRatio4.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS4_D")).getText()));
        smpSingleRatio4.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS4_S")).getText()));
        smpSingleRatio4.setLon(Double.parseDouble(gameBox.findElement(By.id("o_LH4_L")).getText()));
        smpSingleRatio4.setHu(Double.parseDouble(gameBox.findElement(By.id("o_LH4_H")).getText()));
        smpRatio.setRatioFourth(smpSingleRatio4);

        SMPSingleRatio smpSingleRatio5 = new SMPSingleRatio();
        smpSingleRatio5.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX5_D")).getText()));
        smpSingleRatio5.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX5_X")).getText()));
        smpSingleRatio5.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS5_D")).getText()));
        smpSingleRatio5.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS5_S")).getText()));
        smpSingleRatio5.setLon(Double.parseDouble(gameBox.findElement(By.id("o_LH5_L")).getText()));
        smpSingleRatio5.setHu(Double.parseDouble(gameBox.findElement(By.id("o_LH5_H")).getText()));
        smpRatio.setRatioFifth(smpSingleRatio5);

        SMPSingleRatio smpSingleRatio6 = new SMPSingleRatio();
        smpSingleRatio6.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX6_D")).getText()));
        smpSingleRatio6.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX6_X")).getText()));
        smpSingleRatio6.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS6_D")).getText()));
        smpSingleRatio6.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS6_S")).getText()));
        smpRatio.setRatioSixth(smpSingleRatio6);

        SMPSingleRatio smpSingleRatio7 = new SMPSingleRatio();
        smpSingleRatio7.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX7_D")).getText()));
        smpSingleRatio7.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX7_X")).getText()));
        smpSingleRatio7.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS7_D")).getText()));
        smpSingleRatio7.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS7_S")).getText()));
        smpRatio.setRatioSeventh(smpSingleRatio7);

        SMPSingleRatio smpSingleRatio8 = new SMPSingleRatio();
        smpSingleRatio8.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX8_D")).getText()));
        smpSingleRatio8.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX8_X")).getText()));
        smpSingleRatio8.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS8_D")).getText()));
        smpSingleRatio8.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS8_S")).getText()));
        smpRatio.setRatioEighth(smpSingleRatio8);

        SMPSingleRatio smpSingleRatio9 = new SMPSingleRatio();
        smpSingleRatio9.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX9_D")).getText()));
        smpSingleRatio9.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX9_X")).getText()));
        smpSingleRatio9.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS9_D")).getText()));
        smpSingleRatio9.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS9_S")).getText()));
        smpRatio.setRatioNineth(smpSingleRatio9);

        SMPSingleRatio smpSingleRatio10 = new SMPSingleRatio();
        smpSingleRatio10.setDa(Double.parseDouble(gameBox.findElement(By.id("o_DX10_D")).getText()));
        smpSingleRatio10.setXiao(Double.parseDouble(gameBox.findElement(By.id("o_DX10_X")).getText()));
        smpSingleRatio10.setDan(Double.parseDouble(gameBox.findElement(By.id("o_DS10_D")).getText()));
        smpSingleRatio10.setShuang(Double.parseDouble(gameBox.findElement(By.id("o_DS10_S")).getText()));
        smpRatio.setRatioTenth(smpSingleRatio10);

        logger.info("[Operation - FetchRatio] Fetch ratio for 幸运飞艇 - {} - 赔率", PLAYGROUND);
        logger.info(smpRatio.toString());
        if (smpRatioRepository.findByRound(smpRatio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 幸运飞艇 - {} - 赔率", PLAYGROUND);
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
