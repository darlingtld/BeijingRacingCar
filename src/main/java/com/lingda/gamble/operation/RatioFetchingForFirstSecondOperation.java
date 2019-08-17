package com.lingda.gamble.operation;

import com.lingda.gamble.model.FirstSecondRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.FirstSecondRatioRepository;
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

//抓取北京赛车 冠亚军赔率
@Component
public class RatioFetchingForFirstSecondOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForFirstSecondOperation.class);

    private static final String PLAYGROUND = "冠、亞軍 組合";

    private final FirstSecondRatioRepository firstSecondRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForFirstSecondOperation(FirstSecondRatioRepository firstSecondRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.firstSecondRatioRepository = firstSecondRatioRepository;
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


        FirstSecondRatio ratio = new FirstSecondRatio();
        ratio.setRound(Integer.parseInt(element.getText().substring(2)));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("bresult"));
        Double todayWinLostMoney = Double.parseDouble(todayWinLost.getText());
        logger.info("[Operation - FetchRatio] Today win/lost for 幸运飞艇 - {} - {}", PLAYGROUND, todayWinLostMoney);
        WinLostMoney winLostMoney = new WinLostMoney();
        winLostMoney.setAccountName(Store.getAccountName());
        winLostMoney.setRound(ratio.getRound());
        winLostMoney.setWinLostMoney(todayWinLostMoney);
        if (winLostMoneyRepository.findByRoundAndAccountName(winLostMoney.getRound(), winLostMoney.getAccountName()) == null) {
            logger.info("[Operation - FetchRatio] Save today win/lost for 幸运飞艇 - {}", PLAYGROUND);
            winLostMoneyRepository.save(winLostMoney);
        }

        WebElement gameBox = DriverUtils.returnOnFindingElement(driver, By.id("main"));
        RankSingleRatio firstRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("o_B1_1"));
        if (ratioTest.getText().equals("--")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("bet_panel"));
        try {
            firstRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("o_B1_1")).getText()));
            firstRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("o_B1_2")).getText()));
            firstRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("o_B1_3")).getText()));
            firstRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("o_B1_4")).getText()));
            firstRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("o_B1_5")).getText()));
            firstRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("o_B1_6")).getText()));
            firstRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("o_B1_7")).getText()));
            firstRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("o_B1_8")).getText()));
            firstRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("o_B1_9")).getText()));
            firstRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("o_B1_10")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioFirst(firstRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("bet_panel"));
        RankSingleRatio secondRatio = new RankSingleRatio();
        secondRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("o_B2_1")).getText()));
        secondRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("o_B2_2")).getText()));
        secondRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("o_B2_3")).getText()));
        secondRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("o_B2_4")).getText()));
        secondRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("o_B2_5")).getText()));
        secondRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("o_B2_6")).getText()));
        secondRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("o_B2_7")).getText()));
        secondRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("o_B2_8")).getText()));
        secondRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("o_B2_9")).getText()));
        secondRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("o_B2_10")).getText()));
        ratio.setRatioSecond(secondRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 幸运飞艇 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (firstSecondRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 幸运飞艇 - {} - 赔率", PLAYGROUND);
            firstSecondRatioRepository.save(ratio);
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
