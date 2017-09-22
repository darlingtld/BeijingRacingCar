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
        driver.switchTo().parentFrame();
        DriverUtils.returnOnFindingFrame(driver, "mainFrame");
        Thread.sleep(1000);
        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - FetchRatio] Fetch lottery result for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取之前开奖信息
        WebElement lotteryResultEle = DriverUtils.returnOnFindingElement(driver, By.id("betmyOpenRoundData"));
        List<WebElement> spanList = lotteryResultEle.findElements(By.tagName("span"));
        LotteryResult lotteryResult = new LotteryResult();
        Matcher m = roundPattern.matcher(spanList.get(0).getText());
        if (m.find()) {
            lotteryResult.setRound(Integer.parseInt(m.group(1)));
        } else {
            throw new RuntimeException("Can not find lottery result");
        }
        lotteryResult.setFirst(Integer.parseInt(spanList.get(1).getText()));
        lotteryResult.setSecond(Integer.parseInt(spanList.get(2).getText()));
        lotteryResult.setThird(Integer.parseInt(spanList.get(3).getText()));
        lotteryResult.setFourth(Integer.parseInt(spanList.get(4).getText()));
        lotteryResult.setFifth(Integer.parseInt(spanList.get(5).getText()));
        lotteryResult.setSixth(Integer.parseInt(spanList.get(6).getText()));
        lotteryResult.setSeventh(Integer.parseInt(spanList.get(7).getText()));
        lotteryResult.setEighth(Integer.parseInt(spanList.get(8).getText()));
        lotteryResult.setNineth(Integer.parseInt(spanList.get(9).getText()));
        lotteryResult.setTenth(Integer.parseInt(spanList.get(10).getText()));
        logger.info(lotteryResult.toString());
        if (lotteryResultRepository.findByRound(lotteryResult.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save lotteryResult to DB for 北京赛车 - {} - 开奖信息", PLAYGROUND);
            lotteryResultRepository.save(lotteryResult);
        }

        logger.info("[Operation - FetchRatio] Fetch round for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取当前下注期数
        WebElement element = DriverUtils.returnOnFindingElementContainsValue(driver, By.tagName("td"), "北京赛车");

        SMPRatio smpRatio = new SMPRatio();
        smpRatio.setRound(Integer.parseInt(element.findElements(By.tagName("span")).get(0).getText()));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("todayWinLost"));
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
        if (winLostMoney.getWinLostMoney() + Config.getLostThreshold() < 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Blast %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("tbdData"));
        List<WebElement> ratioList = ratioTable.findElements(By.className("tblMy3D")).subList(0, 10);
        for (int i = 0; i < ratioList.size(); i++) {
            List<WebElement> trList = ratioList.get(i).findElements(By.tagName("tr"));
            SMPSingleRatio smpSingleRatio = new SMPSingleRatio();
            if (trList.get(1).findElements(By.tagName("th")).get(1).getText().equals("-")) {
//                正在开奖 或者 正在刷新
                return null;
            }
            smpSingleRatio.setDa(Double.parseDouble(trList.get(1).findElements(By.tagName("th")).get(1).getText()));
            smpSingleRatio.setXiao(Double.parseDouble(trList.get(2).findElements(By.tagName("th")).get(1).getText()));
            smpSingleRatio.setDan(Double.parseDouble(trList.get(3).findElements(By.tagName("th")).get(1).getText()));
            smpSingleRatio.setShuang(Double.parseDouble(trList.get(4).findElements(By.tagName("th")).get(1).getText()));
            if (i < 5) {
//                后面的数字没有龙虎
                smpSingleRatio.setLon(Double.parseDouble(trList.get(5).findElements(By.tagName("th")).get(1).getText()));
                smpSingleRatio.setHu(Double.parseDouble(trList.get(6).findElements(By.tagName("th")).get(1).getText()));
            }
            switch (i) {
                case 0:
                    smpRatio.setRatioFirst(smpSingleRatio);
                    break;
                case 1:
                    smpRatio.setRatioSecond(smpSingleRatio);
                    break;
                case 2:
                    smpRatio.setRatioThird(smpSingleRatio);
                    break;
                case 3:
                    smpRatio.setRatioFourth(smpSingleRatio);
                    break;
                case 4:
                    smpRatio.setRatioFifth(smpSingleRatio);
                    break;
                case 5:
                    smpRatio.setRatioSixth(smpSingleRatio);
                    break;
                case 6:
                    smpRatio.setRatioSeventh(smpSingleRatio);
                    break;
                case 7:
                    smpRatio.setRatioEighth(smpSingleRatio);
                    break;
                case 8:
                    smpRatio.setRatioNineth(smpSingleRatio);
                    break;
                case 9:
                    smpRatio.setRatioTenth(smpSingleRatio);
                    break;
            }
        }
        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(smpRatio.toString());
        if (smpRatioRepository.findByRound(smpRatio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            smpRatioRepository.save(smpRatio);
        }
        return smpRatio.getRound();
    }
}
