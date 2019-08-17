package com.lingda.gamble.operation;

import com.lingda.gamble.model.NinethTenthRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.NinethTenthRatioRepository;
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

//抓取北京赛车 九十赔率
@Component
public class RatioFetchingForNinethTenthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForNinethTenthOperation.class);

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


        NinethTenthRatio ratio = new NinethTenthRatio();
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
        RankSingleRatio ninethRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("o_B9_1"));
        if (ratioTest.getText().equals("--")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("bet_panel"));
        try {
            ninethRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("o_B9_1")).getText()));
            ninethRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("o_B9_2")).getText()));
            ninethRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("o_B9_3")).getText()));
            ninethRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("o_B9_4")).getText()));
            ninethRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("o_B9_5")).getText()));
            ninethRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("o_B9_6")).getText()));
            ninethRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("o_B9_7")).getText()));
            ninethRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("o_B9_8")).getText()));
            ninethRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("o_B9_9")).getText()));
            ninethRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("o_B9_10")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioNineth(ninethRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("bet_panel"));
        RankSingleRatio tenthRatio = new RankSingleRatio();
        tenthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("o_B10_1")).getText()));
        tenthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("o_B10_2")).getText()));
        tenthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("o_B10_3")).getText()));
        tenthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("o_B10_4")).getText()));
        tenthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("o_B10_5")).getText()));
        tenthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("o_B10_6")).getText()));
        tenthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("o_B10_7")).getText()));
        tenthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("o_B10_8")).getText()));
        tenthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("o_B10_9")).getText()));
        tenthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("o_B10_10")).getText()));
        ratio.setRatioTenth(tenthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 幸运飞艇 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (ninethTenthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 幸运飞艇 - {} - 赔率", PLAYGROUND);
            ninethTenthRatioRepository.save(ratio);
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
