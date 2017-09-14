package com.lingda.gamble;

import com.lingda.gamble.operation.BetForFirstSecondOperation;
import com.lingda.gamble.operation.BetForSMPOperation;
import com.lingda.gamble.operation.BetForSMPSafeOperation;
import com.lingda.gamble.operation.BrowserDriver;
import com.lingda.gamble.operation.FinishBetOperation;
import com.lingda.gamble.operation.LoginOperation;
import com.lingda.gamble.operation.NavigationFirstSecondOperation;
import com.lingda.gamble.operation.NavigationOperation;
import com.lingda.gamble.operation.NavigationSMPOperation;
import com.lingda.gamble.operation.RatioFetchingForFirstSecondOperation;
import com.lingda.gamble.operation.RatioFetchingForSMPOperation;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import java.time.LocalTime;

@SpringBootApplication
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
                                  BetForSMPOperation betForSMPOperation,
                                  BetForSMPSafeOperation betForSMPSafeOperation,
                                  NavigationFirstSecondOperation navigationFirstSecondOperation,
                                  RatioFetchingForFirstSecondOperation ratioFetchingForFirstSecondOperation,
                                  BetForFirstSecondOperation betForFirstSecondOperation,
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

            WebDriver driver = BrowserDriver.getDriver();
            loginOperation.doLogin(driver);
            navigationOperation.doNavigate(driver);
//            晚上八点四十后，逐渐收尾，不继续下注
            LocalTime endTime = LocalTime.parse("20:40:00");
            boolean isPlayTime = false;
            while (true) {
                if (endTime.isAfter(LocalTime.now())) {
                    isPlayTime = true;
                }
                try {
                    navigationSMPOperation.doNavigate(driver);
                    Integer round = ratioFetchingForSMPOperation.doFetchRatio(driver);
//                    boolean isSMPBet = betForSMPOperation.doBet(driver, round);
                    boolean isSMPBet = betForSMPSafeOperation.doBet(driver, round, isPlayTime);
                    if (isSMPBet) {
                        finishBetOperation.doFinish(driver, "双面盘");
                    }
                    navigationFirstSecondOperation.doNavigate(driver);
                    Integer firstSecondRound = ratioFetchingForFirstSecondOperation.doFetchRatio(driver);
                    boolean isFirstSecondBet = betForFirstSecondOperation.doBet(driver, firstSecondRound, isPlayTime);
                    if (isFirstSecondBet) {
                        finishBetOperation.doFinish(driver, "冠.亚军");
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
