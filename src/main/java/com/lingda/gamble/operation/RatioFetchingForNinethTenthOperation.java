package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.NinethTenthRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.NinethTenthRatioRepository;
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

//抓取北京赛车 九十名赔率
@Component
public class RatioFetchingForNinethTenthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForNinethTenthOperation.class);

    private static final Pattern roundPattern = Pattern.compile("^([0-9]+)\\s+期");

    private static final String PLAYGROUND = "九十名";

    private final NinethTenthRatioRepository ninethTenthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForNinethTenthOperation(NinethTenthRatioRepository ninethTenthRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.ninethTenthRatioRepository = ninethTenthRatioRepository;
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
        NinethTenthRatio ratio = new NinethTenthRatio();
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
        RankSingleRatio ninethRatio = new RankSingleRatio();
        try {
            ninethRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_01")).getText()));
            ninethRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_02")).getText()));
            ninethRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_03")).getText()));
            ninethRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_04")).getText()));
            ninethRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_05")).getText()));
            ninethRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_06")).getText()));
            ninethRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_07")).getText()));
            ninethRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_08")).getText()));
            ninethRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_09")).getText()));
            ninethRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_9_0_10")).getText()));
            ninethRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_9_1_1")).getText()));
            ninethRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_9_1_2")).getText()));
            ninethRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_9_2_1")).getText()));
            ninethRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_9_2_2")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioNineth(ninethRatio);

        RankSingleRatio tenthRatio = new RankSingleRatio();
        tenthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_01")).getText()));
        tenthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_02")).getText()));
        tenthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_03")).getText()));
        tenthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_04")).getText()));
        tenthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_05")).getText()));
        tenthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_06")).getText()));
        tenthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_07")).getText()));
        tenthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_08")).getText()));
        tenthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_09")).getText()));
        tenthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_10_0_10")).getText()));
        tenthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_10_1_1")).getText()));
        tenthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_10_1_2")).getText()));
        tenthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_10_2_1")).getText()));
        tenthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_10_2_2")).getText()));
        ratio.setRatioTenth(tenthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (ninethTenthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            ninethTenthRatioRepository.save(ratio);
        }
        return ratio.getRound();
    }
}
