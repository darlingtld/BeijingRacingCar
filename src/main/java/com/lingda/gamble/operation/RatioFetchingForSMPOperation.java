package com.lingda.gamble.operation;

import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleRatio;
import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

//抓取北京赛车 双面盘赔率
@Component
public class RatioFetchingForSMPOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForSMPOperation.class);

    public void doFetchRatio(WebDriver driver) throws InterruptedException {
        driver.switchTo().parentFrame();
        DriverUtils.returnOnFindingFrame(driver, "mainFrame");
        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - 双面盘");
        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("tbdData"));
        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - 双面盘");
        List<WebElement> ratioList = ratioTable.findElements(By.className("tblMy3D")).subList(0, 10);
        SMPRatio smpRatio = new SMPRatio();
        for (int i = 0; i < ratioList.size(); i++) {
            List<WebElement> trList = ratioList.get(i).findElements(By.tagName("tr"));
            SMPSingleRatio smpSingleRatio = new SMPSingleRatio();
            switch (i) {
                case 0:
                    smpSingleRatio.setDa(Double.parseDouble(trList.get(1).findElements(By.tagName("th")).get(1).getText()));
                    smpSingleRatio.setXiao(Double.parseDouble(trList.get(2).findElements(By.tagName("th")).get(1).getText()));
                    smpSingleRatio.setDan(Double.parseDouble(trList.get(3).findElements(By.tagName("th")).get(1).getText()));
                    smpSingleRatio.setShuang(Double.parseDouble(trList.get(4).findElements(By.tagName("th")).get(1).getText()));
                    smpSingleRatio.setLon(Double.parseDouble(trList.get(5).findElements(By.tagName("th")).get(1).getText()));
                    smpSingleRatio.setHu(Double.parseDouble(trList.get(6).findElements(By.tagName("th")).get(1).getText()));
            }
            smpRatio.setRatioFirst(smpSingleRatio);
        }
        System.out.println(smpRatio);
    }
}
