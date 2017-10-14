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

//抓取北京赛车 七八赔率
@Component
public class RatioFetchingForSeventhEighthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForSeventhEighthOperation.class);

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

        SeventhEighthRatio ratio = new SeventhEighthRatio();
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
        RankSingleRatio seventhRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("odds_24_95"));
        if (ratioTest.getText().equals("-")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第七名]"));
        try {
            seventhRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_24_95")).getText()));
            seventhRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_24_96")).getText()));
            seventhRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_24_97")).getText()));
            seventhRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_24_98")).getText()));
            seventhRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_24_99")).getText()));
            seventhRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_24_100")).getText()));
            seventhRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_24_101")).getText()));
            seventhRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_24_102")).getText()));
            seventhRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_24_103")).getText()));
            seventhRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_24_104")).getText()));
            seventhRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_26_107")).getText()));
            seventhRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_26_108")).getText()));
            seventhRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_25_105")).getText()));
            seventhRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_25_106")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioSeventh(seventhRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第八名]"));
        RankSingleRatio eighthRatio = new RankSingleRatio();
        eighthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_27_109")).getText()));
        eighthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_27_110")).getText()));
        eighthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_27_111")).getText()));
        eighthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_27_112")).getText()));
        eighthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_27_113")).getText()));
        eighthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_27_114")).getText()));
        eighthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_27_115")).getText()));
        eighthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_27_116")).getText()));
        eighthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_27_117")).getText()));
        eighthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_27_118")).getText()));
        eighthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_29_121")).getText()));
        eighthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_29_122")).getText()));
        eighthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_28_119")).getText()));
        eighthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_28_120")).getText()));
        ratio.setRatioEighth(eighthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (seventhEighthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            seventhEighthRatioRepository.save(ratio);
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
