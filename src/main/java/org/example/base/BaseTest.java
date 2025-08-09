package org.example.base;

import org.apache.logging.log4j.Logger;
import org.example.utils.LoggerUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {
    public WebDriver driver;

    public final Logger logger = LoggerUtil.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver = initDriver();
        testSetup();
    }

    protected WebDriver initDriver() {
        logger.info("Setting up chrome with password manager leak detection off");
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        logger.info("Launching Chrome Browser");
        WebDriver driver = new ChromeDriver(options);
        logger.info("Maximizing Window");
        driver.manage().window().maximize();
        return driver;
    }

    protected void testSetup() {
        logger.info("Navigating to https://www.saucedemo.com/");
        driver.get("https://www.saucedemo.com/");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            logger.info("Quitting Browser");
            driver.quit();
        }
    }
}
