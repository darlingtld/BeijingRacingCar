package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class LoginOperation {

    private static final Logger logger = LoggerFactory.getLogger(LoginOperation.class);

    @Value("${gamble.website}")
    private String website;

    private String secureCodeWindow = null;

    public void doLogin(WebDriver driver) {
        logger.info("[Operation - Login] Navigate to website={}", website);
        driver.get(website);
//        wait for the user to input secure code
        try {
            secureCodeWindow = driver.getWindowHandle();
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
            Set<String> windowHandles = driver.getWindowHandles();
            if (windowHandles.size() == 2) {
                windowHandles.remove(secureCodeWindow);
                String lineWindow = windowHandles.stream().findFirst().get();
                driver.switchTo().window(lineWindow);
                Document doc = Jsoup.parse(driver.getPageSource());
                if (doc.getElementById("menuText") == null) {
                    logger.info("[Operation - Login] Login process is incomplete");
                    Thread.sleep(1000);
                } else {
                    isLoginProcessFinished = true;
                }
            }
        }
    }
}
