package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.FifthSixthRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.FifthSixthRatioRepository;
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

//抓取北京赛车 五六名赔率
@Component
public class RatioFetchingForFifthSixthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForFifthSixthOperation.class);

    private static final Pattern roundPattern = Pattern.compile("^([0-9]+)\\s+期");

    private static final String PLAYGROUND = "五六名";

    private final FifthSixthRatioRepository fifthSixthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForFifthSixthOperation(FifthSixthRatioRepository fifthSixthRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.fifthSixthRatioRepository = fifthSixthRatioRepository;
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
        FifthSixthRatio ratio = new FifthSixthRatio();
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

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("tblMy3DArea"));
        RankSingleRatio fifthRatio = new RankSingleRatio();
        try {
            fifthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_01")).getText()));
            fifthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_02")).getText()));
            fifthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_03")).getText()));
            fifthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_04")).getText()));
            fifthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_05")).getText()));
            fifthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_06")).getText()));
            fifthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_07")).getText()));
            fifthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_08")).getText()));
            fifthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_09")).getText()));
            fifthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_5_0_10")).getText()));
            fifthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_5_1_1")).getText()));
            fifthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_5_1_2")).getText()));
            fifthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_5_2_1")).getText()));
            fifthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_5_2_2")).getText()));
            fifthRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("setR_5_6_1")).getText()));
            fifthRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("setR_5_6_2")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioFifth(fifthRatio);

        RankSingleRatio sixthRatio = new RankSingleRatio();
        sixthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_01")).getText()));
        sixthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_02")).getText()));
        sixthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_03")).getText()));
        sixthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_04")).getText()));
        sixthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_05")).getText()));
        sixthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_06")).getText()));
        sixthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_07")).getText()));
        sixthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_08")).getText()));
        sixthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_09")).getText()));
        sixthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_6_0_10")).getText()));
        sixthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_6_1_1")).getText()));
        sixthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_6_1_2")).getText()));
        sixthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_6_2_1")).getText()));
        sixthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_6_2_2")).getText()));
        ratio.setRatioSixth(sixthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (fifthSixthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            fifthSixthRatioRepository.save(ratio);
        }
        if (winLostMoney.getWinLostMoney() + Config.getLostThreshold() < 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Blast %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }
        if (winLostMoney.getWinLostMoney() - Config.getWinThreshold() > 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Wow %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }
        return ratio.getRound();
    }
}
