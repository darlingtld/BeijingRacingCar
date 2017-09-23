package com.lingda.gamble.util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DriverUtils {
    private static final Logger logger = LoggerFactory.getLogger(DriverUtils.class);
    private static final Integer POLLING_INTERVAL_MS = 500;

    public static WebElement returnOnFindingElement(WebDriver driver, By selector) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding element by {}", selector.toString());
                WebElement element = driver.findElement(selector);
                logger.debug("Found element by {}", selector.toString());
                return element;
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static List<WebElement> returnOnFindingElements(WebDriver driver, By selector) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding element by {}", selector.toString());
                List<WebElement> elementList = driver.findElements(selector);
                logger.debug("Found element by {}", selector.toString());
                return elementList;
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static WebElement returnOnFindingElementEqualsValue(WebDriver driver, By selector, String value) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding element having exact value={} by {}", value, selector.toString());
                Optional<WebElement> element = driver.findElements(selector).stream().filter(ele -> ele.getText().equals(value)).findFirst();
                return element.orElseThrow(
                        () -> new RuntimeException(String.format("Failed to find element having exact value=%s by %s", value, selector.toString())));
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static WebElement returnOnFindingElementEqualsName(WebDriver driver, By selector, String name) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding element having exact name={} by {}", name, selector.toString());
                Optional<WebElement> element = driver.findElements(selector).stream().filter(ele -> ele.getAttribute("name").equals(name)).findFirst();
                return element.orElseThrow(
                        () -> new RuntimeException(String.format("Failed to find element having exact name=%s by %s", name, selector.toString())));
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static WebElement returnOnFindingElementEqualsType(WebDriver driver, By selector, String type) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding element having exact type={} by {}", type, selector.toString());
                Optional<WebElement> element = driver.findElements(selector).stream().filter(ele -> ele.getAttribute("type").equals(type)).findFirst();
                return element.orElseThrow(
                        () -> new RuntimeException(String.format("Failed to find element having exact type=%s by %s", type, selector.toString())));
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static WebElement returnOnFindingElementContainsValue(WebDriver driver, By selector, String value) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding element having value={} by {}", value, selector.toString());
                Optional<WebElement> element = driver.findElements(selector).stream().filter(ele -> ele.getText().contains(value)).findFirst();
                return element.orElseThrow(
                        () -> new RuntimeException(String.format("Failed to find element having value=%s by %s", value, selector.toString())));
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static WebDriver returnOnFindingFrame(WebDriver driver, String frame) {
        int i = 0;
        while (true) {
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                logger.debug("Finding frame by name={}", frame);
                WebDriver webDriver = driver.switchTo().frame(frame);
                logger.debug("Found frame by {}", frame);
                return webDriver;
            } catch (NoSuchFrameException e) {
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void returnOnFinishLoadingGivenLoadingIndicator(WebDriver driver, String loadingIndicator) {
        int i = 0;
        while (driver.getPageSource().contains(loadingIndicator)) {
            logger.debug("Waiting for page finish loading indicator={}", loadingIndicator);
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                Thread.sleep(POLLING_INTERVAL_MS);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void returnOnFinishLoadingGivenFinishingIndicator(WebDriver driver, String finishIndicator) {
        int i = 0;
        while (!driver.getPageSource().contains(finishIndicator)) {
            logger.debug("Waiting for page finish finishing indicator={}", finishIndicator);
            try {
                if (i++ > 50) {
                    throw new RuntimeException("Cannot find element within expected time");
                }
                Thread.sleep(POLLING_INTERVAL_MS);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
