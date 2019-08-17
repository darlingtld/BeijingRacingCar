package com.lingda.gamble.operation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
//        wait for the user to input secure code
        try {
            waitForUserLogin(driver);
        } catch (InterruptedException e) {
            logger.error("[Operation - Login] error {}", e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("[Operation - Login] Login process finished");

    }

    private void waitForUserLogin(WebDriver driver) throws InterruptedException {
        boolean isLoginProcessFinished = false;
        while (!isLoginProcessFinished) {
            Document doc = Jsoup.parse(driver.getPageSource());
            if (doc.getElementById("l_XYFT") == null) {
                logger.info("[Operation - Login] Login process is has not been completed");
                Thread.sleep(1000);
            } else {
                isLoginProcessFinished = true;
            }
        }
    }
}
