package com.lingda.gamble.operation;

import com.lingda.gamble.mail.SimpleMailSender;
import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        logger.info("[Operation - Navigate] Navigate to 北京赛车");
        WebElement accountEle = DriverUtils.returnOnFindingElement(driver, By.className("user_name"));
        logger.info("[Operation - Navigate] Username is {}", accountEle.getText().trim());
        Store.setAccountName(accountEle.getText().substring(0, account.length()));
        if (!Store.getAccountName().equals(account)) {
            throw new RuntimeException(String.format("not authentic user should be %s but %s", account, Store.getAccountName()));
        }

        logger.info("[Operation - Usage notification]");
        String subject = String.format("%s start to use my application", Store.getAccountName());
        SimpleMailSender.send(admin, subject, "fyi");


        WebElement menuTextBtn = DriverUtils.returnOnFindingElement(driver, By.id("menuText"));
        menuTextBtn.click();
        Thread.sleep(1000);

        WebElement bjscBtn = DriverUtils.returnOnFindingElementEqualsValue(driver, By.tagName("a"), "北京賽車(PK10)");
        bjscBtn.click();
        Thread.sleep(1000);
        logger.info("[Operation - Navigate] Navigate to 北京赛车f");
//        make sure 北京赛车 is loaded
        DriverUtils.returnOnFindingFrame(driver, "mainIframe");
        DriverUtils.returnOnFindingElementEqualsValue(driver, By.id("game_big_name"), "北京賽車(PK10)");
    }
}
