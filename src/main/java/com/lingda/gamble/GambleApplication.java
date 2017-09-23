package com.lingda.gamble;

import com.lingda.gamble.operation.BetForFifthSixthOperation;
import com.lingda.gamble.operation.BetForFirstSecondOperation;
import com.lingda.gamble.operation.BetForNinethTenthOperation;
import com.lingda.gamble.operation.BetForSMPBasicOperation;
import com.lingda.gamble.operation.BetForSMPSafeOperation;
import com.lingda.gamble.operation.BetForSeventhEighthOperation;
import com.lingda.gamble.operation.BetForThirdFourthOperation;
import com.lingda.gamble.operation.BrowserDriver;
import com.lingda.gamble.operation.FinishBetOperation;
import com.lingda.gamble.operation.LoginOperation;
import com.lingda.gamble.operation.NavigationFifthSixthOperation;
import com.lingda.gamble.operation.NavigationFirstSecondOperation;
import com.lingda.gamble.operation.NavigationNinethTenthOperation;
import com.lingda.gamble.operation.NavigationOperation;
import com.lingda.gamble.operation.NavigationSMPOperation;
import com.lingda.gamble.operation.NavigationSeventhEighthOperation;
import com.lingda.gamble.operation.NavigationThirdFourthOperation;
import com.lingda.gamble.operation.RatioFetchingForFifthSixthOperation;
import com.lingda.gamble.operation.RatioFetchingForFirstSecondOperation;
import com.lingda.gamble.operation.RatioFetchingForNinethTenthOperation;
import com.lingda.gamble.operation.RatioFetchingForSMPOperation;
import com.lingda.gamble.operation.RatioFetchingForSeventhEighthOperation;
import com.lingda.gamble.operation.RatioFetchingForThirdFourthOperation;
import com.lingda.gamble.param.Config;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalTime;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class GambleApplication {

    private static final Logger logger = LoggerFactory.getLogger(GambleApplication.class);

    @Autowired
    private ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run(GambleApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(LoginOperation loginOperation,
                                  NavigationOperation navigationOperation,
                                  NavigationSMPOperation navigationSMPOperation,
                                  RatioFetchingForSMPOperation ratioFetchingForSMPOperation,
                                  BetForSMPBasicOperation betForSMPBasicOperation,
                                  BetForSMPSafeOperation betForSMPSafeOperation,
                                  
                                  NavigationFirstSecondOperation navigationFirstSecondOperation,
                                  RatioFetchingForFirstSecondOperation ratioFetchingForFirstSecondOperation,
                                  BetForFirstSecondOperation betForFirstSecondOperation,

                                  NavigationThirdFourthOperation navigationThirdFourthOperation,
                                  RatioFetchingForThirdFourthOperation ratioFetchingForThirdFourthOperation,
                                  BetForThirdFourthOperation betForThirdFourthOperation,

                                  NavigationFifthSixthOperation navigationFifthSixthOperation,
                                  RatioFetchingForFifthSixthOperation ratioFetchingForFifthSixthOperation,
                                  BetForFifthSixthOperation betForFifthSixthOperation,

                                  NavigationSeventhEighthOperation navigationSeventhEighthOperation,
                                  RatioFetchingForSeventhEighthOperation ratioFetchingForSeventhEighthOperation,
                                  BetForSeventhEighthOperation betForSeventhEighthOperation,

                                  NavigationNinethTenthOperation navigationNinethTenthOperation,
                                  RatioFetchingForNinethTenthOperation ratioFetchingForNinethTenthOperation,
                                  BetForNinethTenthOperation betForNinethTenthOperation,
                                  FinishBetOperation finishBetOperation) {
        return (args) -> {
            if (args.length == 0) {
                String chromeDriverPath = resourceLoader.getResource("classpath:chromedriver").getFile().getPath();
                logger.info("Chrome driver path is {}", chromeDriverPath);
                System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            } else {
                String chromeDriverPath = args[0];
                logger.info("Chrome driver path is {}", chromeDriverPath);
                System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            }

            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));

            WebDriver driver = BrowserDriver.getDriver();
            loginOperation.doLogin(driver);
            navigationOperation.doNavigate(driver);
//            晚上八点四十后，逐渐收尾，不继续下注
            LocalTime endTime = LocalTime.parse("20:40:00");
            boolean isPlayTime = false;
            while (true) {
                logger.info("Current time is {}. End Time is {}", LocalTime.now(), endTime);
                if (endTime.isAfter(LocalTime.now())) {
                    isPlayTime = true;
                }
                try {
                    navigationSMPOperation.doNavigate(driver);
                    Integer round = ratioFetchingForSMPOperation.doFetchRatio(driver);
                    if (Config.getSmpEnabled()) {
                        boolean isSMPBet = betForSMPBasicOperation.doBet(driver, round, isPlayTime);
                        if (isSMPBet) {
                            finishBetOperation.doFinish(driver, "双面盘");
                        }
                    }
                    navigationFirstSecondOperation.doNavigate(driver);
                    Integer firstSecondRound = ratioFetchingForFirstSecondOperation.doFetchRatio(driver);
                    if (Config.getFirstSecondEnabled()) {
                        boolean isFirstSecondBet = betForFirstSecondOperation.doBet(driver, firstSecondRound, isPlayTime);
                        if (isFirstSecondBet) {
                            finishBetOperation.doFinish(driver, "冠.亚军");
                        }
                    }
                    navigationThirdFourthOperation.doNavigate(driver);
                    Integer thirdFourthRound = ratioFetchingForThirdFourthOperation.doFetchRatio(driver);
                    if (Config.getThirdFourthEnabled()) {
                        boolean isThirdFourthBet = betForThirdFourthOperation.doBet(driver, thirdFourthRound, isPlayTime);
                        if (isThirdFourthBet) {
                            finishBetOperation.doFinish(driver, "三.四名");
                        }
                    }
                    navigationFifthSixthOperation.doNavigate(driver);
                    Integer fifthSixthRound = ratioFetchingForFifthSixthOperation.doFetchRatio(driver);
                    if (Config.getFifthSixthEnabled()) {
                        boolean isFifthSixthBet = betForFifthSixthOperation.doBet(driver, fifthSixthRound, isPlayTime);
                        if (isFifthSixthBet) {
                            finishBetOperation.doFinish(driver, "五.六名");
                        }
                    }
                    navigationSeventhEighthOperation.doNavigate(driver);
                    Integer seventhEighthRound = ratioFetchingForSeventhEighthOperation.doFetchRatio(driver);
                    if (Config.getSeventhEighthEnabled()) {
                        boolean isSeventhEighthBet = betForSeventhEighthOperation.doBet(driver, seventhEighthRound, isPlayTime);
                        if (isSeventhEighthBet) {
                            finishBetOperation.doFinish(driver, "七.八名");
                        }
                    }
                    navigationNinethTenthOperation.doNavigate(driver);
                    Integer ninethTenthRound = ratioFetchingForNinethTenthOperation.doFetchRatio(driver);
                    if (Config.getNinethTenthEnabled()) {
                        boolean isNinethTenthBet = betForNinethTenthOperation.doBet(driver, ninethTenthRound, isPlayTime);
                        if (isNinethTenthBet) {
                            finishBetOperation.doFinish(driver, "九.十名");
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    Thread.sleep(15 * 1000);
                }
            }
        };
    }
}
