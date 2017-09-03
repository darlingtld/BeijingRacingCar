package com.lingda.gamble.operation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

public class BrowserDriver {

    private static WebDriver driver = null;

    static WebDriver getDriver() {
        if (driver == null) {
            return new ChromeDriver(new ChromeDriverService.Builder().withSilent(true).build());
        } else {
            return driver;
        }
    }
}
