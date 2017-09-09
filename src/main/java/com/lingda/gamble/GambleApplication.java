package com.lingda.gamble;

import com.lingda.gamble.operation.BetForSMPOperation;
import com.lingda.gamble.operation.BrowserDriver;
import com.lingda.gamble.operation.LoginOperation;
import com.lingda.gamble.operation.NavigationOperation;
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

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class GambleApplication {

    private static final Logger logger = LoggerFactory.getLogger(GambleApplication.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void postConstruct() throws IOException {
        String chromeDriverPath = resourceLoader.getResource("classpath:chromedriver").getFile().getPath();
        logger.info("Chrome driver path is {}", chromeDriverPath);
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    public static void main(String[] args) {
        SpringApplication.run(GambleApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(LoginOperation loginOperation,
                                  NavigationOperation navigationOperation,
                                  RatioFetchingForSMPOperation ratioFetchingForSMPOperation,
                                  BetForSMPOperation betForSMPOperation) {
        return (args) -> {
            WebDriver driver = BrowserDriver.getDriver();
            loginOperation.doLogin(driver);
            navigationOperation.doNavigate(driver);
            while (true) {
                try {
                    Integer round = ratioFetchingForSMPOperation.doFetchRatio(driver);
                    betForSMPOperation.doBet(driver, round);
                    Thread.sleep(15 * 1000);
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        };
    }
}
