package com.kavinschool.quiz.hooks;

import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.lang.System.setProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kavinschool.quiz.utils.DriverUtils;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class DriverFactory {
    public static final String BROWSER_TYPE = "browserType";
    public static final String RUN_TYPE = "runType";
    public static final String SELENIUM_GRID_HUB = "selenium_grid_hub";
    public static final String GC_BROWSER_PATH = "gc_browser_path";
    public static final String GC_DRIVER_PATH = "gc_driver_path";
    public static final String LOCAL = "local";
    public static final String REMOTE = "remote";
    private static final String SERV_PROP_FILE = "src/test/resources/server.properties.txt";
    private static final Boolean LOCAL_CAPS_ON = true;

    private static boolean isBrowserExist = false;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private WebDriver driver;
    private String browserType;
    private String runType;
    private Scenario scenario;
    private DriverUtils driverUtils;

    private Properties props = new Properties();


    public static boolean isBrowserExist() {
        return isBrowserExist;
    }

    public static void setIsBrowserExist(boolean isBrowserExist) {
        DriverFactory.isBrowserExist = isBrowserExist;
    }

    @Before
    public void setUp(Scenario scenario) throws Exception {
        System.out.println("scenario:" + scenario.getName());
        this.scenario = scenario;
        props.load(new FileInputStream(SERV_PROP_FILE));
        if (!isBrowserExist || driver == null) {
            driver = createDriver();
            isBrowserExist = true;
        }
        driverUtils = new DriverUtils(driver).setBrowserType(browserType);
    }


    public WebDriver createDriver() throws Exception {
        browserType = getProperty(BROWSER_TYPE);
        runType = getProperty(RUN_TYPE);
        // if the system variables not set,
        // then take it from the property file
        if (browserType == null || browserType.isEmpty()) {
            browserType = props.getProperty(BROWSER_TYPE);
        }

        if (runType == null || runType.isEmpty()) {
            runType = props.getProperty(RUN_TYPE);
        }

        switch (runType.toLowerCase()) {
            case LOCAL:
                driver = createLocalDriver(browserType);
                break;
            case REMOTE:
                driver = createRemoteDriver(browserType);
                break;
            default:
                throw new Exception(RUN_TYPE + " is not correct, " + "accepted values are:" + LOCAL + " & " + REMOTE);
        }
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }


    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }


    @After
    public void tearDown()  {
        DriverUtils.delay(3000);
        driver.quit();
    }

    public WebDriver createLocalDriver(String browserType)  {

        out.println(BROWSER_TYPE + " " + browserType);
        if (browserType.toLowerCase().equalsIgnoreCase(BrowserType.CHROME)) {
            setChromeProperties();
            if (LOCAL_CAPS_ON) {
                driver = new ChromeDriver(getChromeDriverOptions());
            } else {
                driver = new ChromeDriver();
            }
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
    }

    private WebDriver createRemoteDriver(String browserType) throws
            IOException {

        String seleniumGridHub = props.getProperty(SELENIUM_GRID_HUB);
        out.println(BROWSER_TYPE + " " + browserType);
        if (browserType.toLowerCase().equalsIgnoreCase(BrowserType.CHROME)) {
            setChromeProperties();
            driver = new RemoteWebDriver(new URL(seleniumGridHub), getChromeDriverOptions());
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
    }

    private void setChromeProperties() {
        String gcDriverPath = props.getProperty(GC_DRIVER_PATH);
        File file = new File(gcDriverPath);
        setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, file.getAbsolutePath());
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public DriverUtils getDriverUtils() {
        return driverUtils;
    }

    private ChromeOptions getChromeDriverOptions() {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--test-type");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("disable-popup-blocking", "true");
        options.addArguments("download.directory_upgrade", "true");
        options.addArguments("download.prompt_for_download", "false");
        return options;
    }

}
