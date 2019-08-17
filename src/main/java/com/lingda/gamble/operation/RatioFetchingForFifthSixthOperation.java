package com.lingda.gamble.operation;

import com.lingda.gamble.model.*;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.FifthSixthRatioRepository;
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

//抓取北京赛车 五六赔率
@Component
public class RatioFetchingForFifthSixthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForFifthSixthOperation.class);

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


        FifthSixthRatio ratio = new FifthSixthRatio();
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
        RankSingleRatio fifthRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("o_B5_1"));
        if (ratioTest.getText().equals("--")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("bet_panel"));
        try {
            fifthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("o_B5_1")).getText()));
            fifthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("o_B5_2")).getText()));
            fifthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("o_B5_3")).getText()));
            fifthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("o_B5_4")).getText()));
            fifthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("o_B5_5")).getText()));
            fifthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("o_B5_6")).getText()));
            fifthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("o_B5_7")).getText()));
            fifthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("o_B5_8")).getText()));
            fifthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("o_B5_9")).getText()));
            fifthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("o_B5_10")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioFifth(fifthRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("bet_panel"));
        RankSingleRatio sixthRatio = new RankSingleRatio();
        sixthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("o_B6_1")).getText()));
        sixthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("o_B6_2")).getText()));
        sixthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("o_B6_3")).getText()));
        sixthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("o_B6_4")).getText()));
        sixthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("o_B6_5")).getText()));
        sixthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("o_B6_6")).getText()));
        sixthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("o_B6_7")).getText()));
        sixthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("o_B6_8")).getText()));
        sixthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("o_B6_9")).getText()));
        sixthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("o_B6_10")).getText()));
        ratio.setRatioSixth(sixthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 幸运飞艇 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (fifthSixthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 幸运飞艇 - {} - 赔率", PLAYGROUND);
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
