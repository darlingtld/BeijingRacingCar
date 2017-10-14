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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//抓取北京赛车 冠亚军赔率
@Component
public class RatioFetchingForFirstSecondOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForFirstSecondOperation.class);

    private static final Pattern roundPattern = Pattern.compile("^([0-9]+)\\s+期");

    private static final String PLAYGROUND = "冠亚军";

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

        FirstSecondRatio ratio = new FirstSecondRatio();
        ratio.setRound(Integer.parseInt(element.getText()));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("profit"));
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

        WebElement gameBox = DriverUtils.returnOnFindingElement(driver, By.id("gameBox"));
        RankSingleRatio firstRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("odds_1_1"));
        if (ratioTest.getText().equals("-")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=冠軍]"));
        try {
            firstRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_1_1")).getText()));
            firstRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_1_2")).getText()));
            firstRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_1_3")).getText()));
            firstRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_1_4")).getText()));
            firstRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_1_5")).getText()));
            firstRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_1_6")).getText()));
            firstRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_1_7")).getText()));
            firstRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_1_8")).getText()));
            firstRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_1_9")).getText()));
            firstRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_1_10")).getText()));
            firstRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_3_13")).getText()));
            firstRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_3_14")).getText()));
            firstRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_2_11")).getText()));
            firstRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_2_12")).getText()));
            firstRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("odds_4_15")).getText()));
            firstRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("odds_4_16")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioFirst(firstRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=亞軍]"));
        RankSingleRatio secondRatio = new RankSingleRatio();
        secondRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_5_17")).getText()));
        secondRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_5_18")).getText()));
        secondRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_5_19")).getText()));
        secondRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_5_20")).getText()));
        secondRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_5_21")).getText()));
        secondRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_5_22")).getText()));
        secondRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_5_23")).getText()));
        secondRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_5_24")).getText()));
        secondRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_5_25")).getText()));
        secondRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_5_26")).getText()));
        secondRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_7_29")).getText()));
        secondRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_7_30")).getText()));
        secondRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_6_27")).getText()));
        secondRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_6_28")).getText()));
        secondRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("odds_8_31")).getText()));
        secondRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("odds_8_32")).getText()));
        ratio.setRatioSecond(secondRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (firstSecondRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
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
