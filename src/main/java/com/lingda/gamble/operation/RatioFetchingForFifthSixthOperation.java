package com.lingda.gamble.operation;

import com.lingda.gamble.model.FifthSixthRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
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

        FifthSixthRatio ratio = new FifthSixthRatio();
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
        RankSingleRatio fifthRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("odds_17_65"));
        if (ratioTest.getText().equals("-")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第五名]"));
        try {
            fifthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_17_65")).getText()));
            fifthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_17_66")).getText()));
            fifthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_17_67")).getText()));
            fifthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_17_68")).getText()));
            fifthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_17_69")).getText()));
            fifthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_17_70")).getText()));
            fifthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_17_71")).getText()));
            fifthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_17_72")).getText()));
            fifthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_17_73")).getText()));
            fifthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_17_74")).getText()));
            fifthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_19_77")).getText()));
            fifthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_19_78")).getText()));
            fifthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_18_75")).getText()));
            fifthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_18_76")).getText()));
            fifthRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("odds_20_79")).getText()));
            fifthRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("odds_20_80")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioFifth(fifthRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第六名]"));
        RankSingleRatio sixthRatio = new RankSingleRatio();
        sixthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_21_81")).getText()));
        sixthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_21_82")).getText()));
        sixthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_21_83")).getText()));
        sixthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_21_84")).getText()));
        sixthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_21_85")).getText()));
        sixthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_21_86")).getText()));
        sixthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_21_87")).getText()));
        sixthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_21_88")).getText()));
        sixthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_21_89")).getText()));
        sixthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_21_90")).getText()));
        sixthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_23_93")).getText()));
        sixthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_23_94")).getText()));
        sixthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_22_91")).getText()));
        sixthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_22_92")).getText()));
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
