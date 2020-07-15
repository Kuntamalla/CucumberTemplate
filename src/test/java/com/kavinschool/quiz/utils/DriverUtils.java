package com.kavinschool.quiz.utils;

/*
 * Copyright (c) 2018. Kavin School LLC
 */

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class DriverUtils {

    public static final String SERV_PROP_FILE = "src/test/resources/server.properties.txt";
    public String browserType;
    private WebDriver driver;
    private Properties props;

    public DriverUtils(WebDriver driver) {
        this.driver = driver;
    }

    public static void delay(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void jsClick(final WebElement element, final WebDriver driver) throws InterruptedException {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        ((RemoteWebDriver) executor).executeScript("arguments[0].click();", element);
    }

    public static void jsClickById(final String locatorId, final WebDriver driver) throws InterruptedException {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        ((RemoteWebDriver) executor).executeScript("return document.getElementById('"+locatorId+"').click();");
    }

    public static void reduceImplicitWait(WebDriver driver, final int timeToReduceInSeconds) {
        driver.manage().timeouts().implicitlyWait(timeToReduceInSeconds, TimeUnit.SECONDS);
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public String closeAlertAndGetItsText(boolean acceptNextAlert) {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        if (acceptNextAlert) {
            alert.accept();
        } else {
            alert.dismiss();
        }
        return alertText;
    }

    public String getBaseURL()  {
        props = new Properties();
        try {
            props.load(new FileInputStream(SERV_PROP_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty("url");
    }

    public void navigateToBaseURL() {
        driver.navigate().to(getBaseURL() + "/osticket/");
    }

    public void openURL() {
        driver.get(getBaseURL() + "/osticket/");
    }

    public void navigateToGmail() {
        driver.navigate().to(getBaseURL() + "/osticket/");
    }

    public void clearAndType(WebElement field, String text) {
        field.clear();
        field.sendKeys(text);
    }

    public String getDateTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHmmss");
        Date today = new Date();
        return formatter.format(new java.sql.Timestamp(today.getTime()));
    }

    public boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void selectOption(WebDriver driver, final String selectOption,
                             final By by) {
        Select select = new Select(driver.findElement(by));
        select.selectByVisibleText(selectOption);
    }

    public void selectOption(WebElement selectedElement, final String selectOption)
    {
        Select select = new Select(selectedElement);
        select.selectByVisibleText(selectOption);
    }

    public boolean isTextPresent(WebDriver driver, String Text) {
        try {
            return driver.findElement(By.tagName("body")).getText()
                    .contains(Text);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public byte[] saveScreenShotTo(String screenShotfileName) {
        System.out.println("saveScreenShotTo");
        if (browserType.equalsIgnoreCase("htmlunit"))
            return new byte[0];
        try {
            return writeScreenShot(screenShotfileName);
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] writeScreenShot(String screenShotfileName)
            throws IOException {
        System.out.println("writeScreenShot: " + screenShotfileName);
        File tempScrShotFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(tempScrShotFile, new File(screenShotfileName));
        return ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);

    }

    // This method creates a file name with the following format
    // ScreenShot/Date/time_classname_testname.png
    // ScreenShot is a folder
    // Date is a folder
    // time_classname_testname.png is a file
    // Date format is yyyyMMdd
    // time format is HHmmssSSS
    // className and methodName special characters ".][" are replaced with "_"
    public String getScreenShotFileName(String className, String methodName) {
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        DateFormat dateFormat1 = new SimpleDateFormat("HHmmssSSS");
        String now = dateFormat1.format(new Date());
        String today = dateFormat2.format(new Date());
        String fileName;
        System.out.println("Method Name:" + methodName);
        System.out.println("Class Name:" + className);
        if (methodName != null)
            fileName = className + "." + methodName;
        else
            fileName = className;
        fileName = "target/Screenshots/" + today + "/" + now + '_' + browserType + '_'
                + fileName.replaceAll("\\.|\\[|\\]", "_") + ".png";
        System.out.println("name:" + fileName);
        return fileName;
    }

    public void waitForText(String text, By by,
                               String timeoutMsg) throws Exception {
        for (int second = 0;; second++) {
            if (second >= 60)
                fail(timeoutMsg);
            try {
                if (text.equals(driver.findElement(by).getText()))
                    break;
            } catch (Exception e) {
            }
            delay(1000);
        }
    }

    public void clickAt(final WebDriver driver, final By byMethod) {
        final WebElement element = driver.findElement(byMethod);
        final Actions builder = new Actions(driver);
        final Action action = builder.moveToElement(element).click().build();
        action.perform();
    }

    public void clickAt(WebDriver driver, By by, int xOffset, int yOffset) {
        WebElement element = driver.findElement(by);
        Actions builder = new Actions(driver);
        Action action = builder.moveToElement(element, 10, 10).click().build();
        action.perform();
    }

    public void clickAt(final WebDriver driver, final WebElement element) {
        final Actions builder = new Actions(driver);
        final Action action = builder.moveToElement(element).click().build();
        action.perform();
    }

    public void clickAt(final WebDriver driver, final WebElement element, final int xOffset,
                        final int yOffset) {
        final Actions builder = new Actions(driver);
        final Action action = builder.moveToElement(element, xOffset, yOffset).click().build();
        action.perform();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public String getBrowserType() {
        return browserType;
    }

    public DriverUtils setBrowserType(String browserType) {
        this.browserType = browserType;
        return this;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
