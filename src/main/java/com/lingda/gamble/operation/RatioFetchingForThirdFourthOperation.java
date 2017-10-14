package com.lingda.gamble.operation;

import com.lingda.gamble.model.ThirdFourthRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.ThirdFourthRatioRepository;
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

//抓取北京赛车 三四赔率
@Component
public class RatioFetchingForThirdFourthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForThirdFourthOperation.class);

    private static final String PLAYGROUND = "三四名";

    private final ThirdFourthRatioRepository thirdFourthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForThirdFourthOperation(ThirdFourthRatioRepository thirdFourthRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.thirdFourthRatioRepository = thirdFourthRatioRepository;
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

        ThirdFourthRatio ratio = new ThirdFourthRatio();
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
        RankSingleRatio thirdRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("odds_9_33"));
        if (ratioTest.getText().equals("-")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第三名]"));
        try {
            thirdRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_9_33")).getText()));
            thirdRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_9_34")).getText()));
            thirdRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_9_35")).getText()));
            thirdRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_9_36")).getText()));
            thirdRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_9_37")).getText()));
            thirdRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_9_38")).getText()));
            thirdRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_9_39")).getText()));
            thirdRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_9_40")).getText()));
            thirdRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_9_41")).getText()));
            thirdRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_9_42")).getText()));
            thirdRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_11_45")).getText()));
            thirdRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_11_46")).getText()));
            thirdRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_10_43")).getText()));
            thirdRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_10_44")).getText()));
            thirdRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("odds_12_47")).getText()));
            thirdRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("odds_12_48")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioThird(thirdRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第四名]"));
        RankSingleRatio fourthRatio = new RankSingleRatio();
        fourthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_13_49")).getText()));
        fourthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_13_50")).getText()));
        fourthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_13_51")).getText()));
        fourthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_13_52")).getText()));
        fourthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_13_53")).getText()));
        fourthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_13_54")).getText()));
        fourthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_13_55")).getText()));
        fourthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_13_56")).getText()));
        fourthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_13_57")).getText()));
        fourthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_13_58")).getText()));
        fourthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_15_61")).getText()));
        fourthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_15_62")).getText()));
        fourthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_14_59")).getText()));
        fourthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_14_60")).getText()));
        fourthRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("odds_16_63")).getText()));
        fourthRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("odds_16_64")).getText()));
        ratio.setRatioFourth(fourthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (thirdFourthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            thirdFourthRatioRepository.save(ratio);
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
