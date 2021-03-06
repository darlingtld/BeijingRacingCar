package com.lingda.gamble.operation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginOperation {

    private static final Logger logger = LoggerFactory.getLogger(LoginOperation.class);

    @Value("${gamble.website}")
    private String website;

    public void doLogin(WebDriver driver) {
        logger.info("[Operation - Login] Navigate to website={}", website);
        driver.get(website);
//        wait for the user to input authnumber
        try {
            waitForUserInputAuthNumber(driver);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.get(driver.getCurrentUrl() + "&ok=1");

        logger.info("[Operation - Login] Login process finished");

    }

    private void waitForUserInputAuthNumber(WebDriver driver) throws InterruptedException {
        boolean isLoginProcessFinished = false;
        while (!isLoginProcessFinished) {
            try {
                Document doc = Jsoup.parse(driver.getPageSource());
                if (doc.getElementById("authinput") != null) {
                    logger.info("[Operation - Login] Wait for user to input auth number and login");
                    Thread.sleep(1000);
                } else {
                    isLoginProcessFinished = true;
                }
            } catch (UnhandledAlertException e) {
                logger.info("[Operation - Login] Accept the alert");
                driver.switchTo().alert().accept();
                isLoginProcessFinished = true;
            }
        }
    }
}
