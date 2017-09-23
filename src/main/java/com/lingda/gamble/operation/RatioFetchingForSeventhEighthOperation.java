package com.lingda.gamble.operation;

import com.lingda.gamble.model.SeventhEighthRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.SeventhEighthRatioRepository;
import com.lingda.gamble.repository.LotteryResultRepository;
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

//抓取北京赛车 七八名赔率
@Component
public class RatioFetchingForSeventhEighthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForSeventhEighthOperation.class);

    private static final Pattern roundPattern = Pattern.compile("^([0-9]+)\\s+期");

    private static final String PLAYGROUND = "七八名";

    private final SeventhEighthRatioRepository seventhEighthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForSeventhEighthOperation(SeventhEighthRatioRepository seventhEighthRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.seventhEighthRatioRepository = seventhEighthRatioRepository;
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
        SeventhEighthRatio ratio = new SeventhEighthRatio();
        ratio.setRound(Integer.parseInt(element.findElements(By.tagName("span")).get(0).getText()));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("todayWinLost"));
        Double todayWinLostMoney = Double.parseDouble(todayWinLost.getText());
        logger.info("[Operation - FetchRatio] Today win/lost for 北京赛车 - {} - {}", PLAYGROUND, todayWinLostMoney);
        WinLostMoney winLostMoney = new WinLostMoney();
        winLostMoney.setAccountName(Store.getAccountName());
        winLostMoney.setRound(ratio.getRound());
        winLostMoney.setWinLostMoney(todayWinLostMoney);
        if (winLostMoneyRepository.findByRoundAndAccountName(winLostMoney.getRound(), winLostMoney.getAccountName()) == null) {
            logger.info("[Operation - FetchRatio] Save today win/lost for 北京赛车 - {}", PLAYGROUND);
            winLostMoneyRepository.save(winLostMoney);
        }
        if (winLostMoney.getWinLostMoney() + Config.getLostThreshold() < 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Blast %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("tblMy3DArea"));
        RankSingleRatio seventhRatio = new RankSingleRatio();
        try {
            seventhRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_01")).getText()));
            seventhRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_02")).getText()));
            seventhRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_03")).getText()));
            seventhRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_04")).getText()));
            seventhRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_05")).getText()));
            seventhRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_06")).getText()));
            seventhRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_07")).getText()));
            seventhRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_08")).getText()));
            seventhRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_09")).getText()));
            seventhRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_7_0_10")).getText()));
            seventhRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_7_1_1")).getText()));
            seventhRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_7_1_2")).getText()));
            seventhRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_7_2_1")).getText()));
            seventhRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_7_2_2")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioSeventh(seventhRatio);

        RankSingleRatio eighthRatio = new RankSingleRatio();
        eighthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_01")).getText()));
        eighthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_02")).getText()));
        eighthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_03")).getText()));
        eighthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_04")).getText()));
        eighthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_05")).getText()));
        eighthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_06")).getText()));
        eighthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_07")).getText()));
        eighthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_08")).getText()));
        eighthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_09")).getText()));
        eighthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_8_0_10")).getText()));
        eighthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_8_1_1")).getText()));
        eighthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_8_1_2")).getText()));
        eighthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_8_2_1")).getText()));
        eighthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_8_2_2")).getText()));
        ratio.setRatioEighth(eighthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (seventhEighthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            seventhEighthRatioRepository.save(ratio);
        }
        return ratio.getRound();
    }
}
