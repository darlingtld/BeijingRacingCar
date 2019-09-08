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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class GambleApplication {

    private static final Logger logger = LoggerFactory.getLogger(GambleApplication.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${gamble.expiration.year}")
    private Integer year;

    @Value("${gamble.expiration.month}")
    private Integer month;

    @Value("${gamble.expiration.day}")
    private Integer day;

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
            LocalDate endDate = LocalDate.of(year, month, day);
            WebDriver driver = BrowserDriver.getDriver();
            loginOperation.doLogin(driver);
            navigationOperation.doNavigate(driver);
//            晚上23:30后，逐渐收尾，不继续下注
            LocalTime endTime = LocalTime.parse("23:30:00");
            boolean isPlayTime = false;
            while (true) {
                logger.info("Current Date is {}. End Date is {}", LocalDate.now(), endDate);
                if(LocalDate.now().isAfter(endDate)){
                    break;
                }
                logger.info("Current time is {}. End Time is {}", LocalTime.now(), endTime);
                if (endTime.isAfter(LocalTime.now())) {
                    isPlayTime = true;
                }
                try {
                    driver.switchTo().parentFrame();
                    navigationSMPOperation.doNavigate(driver);
                    Integer round = ratioFetchingForSMPOperation.doFetchRatio(driver);
                    if (Config.getSmpEnabled()) {
                        boolean isSMPBet = betForSMPBasicOperation.doBet(driver, round, isPlayTime);
                        if (isSMPBet) {
                            finishBetOperation.doFinish(driver, "两面盘");
                        }
                    }
                    driver.switchTo().parentFrame();
                    navigationFirstSecondOperation.doNavigate(driver);
                    Integer firstSecondRound = ratioFetchingForFirstSecondOperation.doFetchRatio(driver);
                    if (Config.getFirstSecondEnabled()) {
                        boolean isFirstSecondBet = betForFirstSecondOperation.doBet(driver, firstSecondRound, isPlayTime);
                        if (isFirstSecondBet) {
                            finishBetOperation.doFinish(driver, "单号1 ~ 10");
                        }
                    }
                    driver.switchTo().parentFrame();
                    navigationThirdFourthOperation.doNavigate(driver);
                    Integer thirdFourthRound = ratioFetchingForThirdFourthOperation.doFetchRatio(driver);
                    if (Config.getThirdFourthEnabled()) {
                        boolean isThirdFourthBet = betForThirdFourthOperation.doBet(driver, thirdFourthRound, isPlayTime);
                        if (isThirdFourthBet) {
                            finishBetOperation.doFinish(driver, "单号1 ~ 10");
                        }
                    }
                    driver.switchTo().parentFrame();
                    navigationSeventhEighthOperation.doNavigate(driver);
                    Integer seventhEighthRound = ratioFetchingForSeventhEighthOperation.doFetchRatio(driver);
                    if (Config.getSeventhEighthEnabled()) {
                        boolean isSeventhEighthBet = betForSeventhEighthOperation.doBet(driver, seventhEighthRound, isPlayTime);
                        if (isSeventhEighthBet) {
                            finishBetOperation.doFinish(driver, "单号1 ~ 10");
                        }
                    }
                    driver.switchTo().parentFrame();
                    navigationFifthSixthOperation.doNavigate(driver);
                    Integer fifthSixthRound = ratioFetchingForFifthSixthOperation.doFetchRatio(driver);
                    if (Config.getFifthSixthEnabled()) {
                        boolean isFifthSixthBet = betForFifthSixthOperation.doBet(driver, fifthSixthRound, isPlayTime);
                        if (isFifthSixthBet) {
                            finishBetOperation.doFinish(driver, "单号1 ~ 10");
                        }
                    }
                    driver.switchTo().parentFrame();
                    navigationNinethTenthOperation.doNavigate(driver);
                    Integer ninethTenthRound = ratioFetchingForNinethTenthOperation.doFetchRatio(driver);
                    if (Config.getNinethTenthEnabled()) {
                        boolean isNinethTenthBet = betForNinethTenthOperation.doBet(driver, ninethTenthRound, isPlayTime);
                        if (isNinethTenthBet) {
                            finishBetOperation.doFinish(driver, "单号1 ~ 10");
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
