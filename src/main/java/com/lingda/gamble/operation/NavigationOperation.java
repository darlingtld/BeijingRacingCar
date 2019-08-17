package com.lingda.gamble.operation;

import com.lingda.gamble.mail.SimpleMailSender;
import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NavigationOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationOperation.class);

    @Value("${gamble.website}")
    private String website;

    @Value("${account}")
    private String account;

    @Value("${gamble.notification.admin}")
    private String admin;

    public void doNavigate(WebDriver driver) throws InterruptedException {
        logger.info("[Operation - Navigate] Navigate to 幸运飞艇");
        WebElement accountEle = DriverUtils.returnOnFindingElement(driver, By.className("inline-name"));
        logger.info("[Operation - Navigate] Username is {}", accountEle.getText().trim());
        Store.setAccountName(accountEle.getText().substring(0, account.length()));
        if (!Store.getAccountName().equals(account)) {
            throw new RuntimeException(String.format("not authentic user should be %s but %s", account, Store.getAccountName()));
        }

        logger.info("[Operation - Usage notification]");
        String subject = String.format("%s start to use my application", Store.getAccountName());
//        SimpleMailSender.send(admin, subject, "fyi");

        boolean retry = true;
        while (retry) {
            try {
                WebElement bjscBtn = DriverUtils.returnOnFindingElement(driver, By.cssSelector("#l_XYFT"));
                bjscBtn.click();
                Thread.sleep(1000);
                retry = false;
            } catch (WebDriverException e) {
                logger.info("[Operation - Navigate] Retry navigating to 幸运飞艇");

            }
        }
        logger.info("[Operation - Navigate] Navigate to 幸运飞艇");
//        make sure 幸运飞艇 is loaded
        driver = driver.switchTo().frame("frame");
        DriverUtils.returnOnFindingElementEqualsValue(driver, By.id("lotteryName"), "幸运飞艇");
    }
}
