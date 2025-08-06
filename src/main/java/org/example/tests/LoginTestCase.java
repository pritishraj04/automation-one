package org.example.tests;

import org.apache.logging.log4j.Logger;
import org.example.utils.LoggerUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.example.pages.LoginPage;

import java.util.HashMap;
import java.util.Map;

public class LoginTestCase {
    public WebDriver driver;
    LoginPage loginPage;

    public final Logger logger = LoggerUtil.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        logger.info("Setting up chrome with password manager leak detection off");
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        logger.info("Launching Chrome Browser");
        driver = new ChromeDriver(options);
        logger.info("Navigating to https://www.saucedemo.com/");
        driver.get("https://www.saucedemo.com/");
        logger.info("Maximizing Window");
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            logger.info("Quitting Browser");
            driver.quit();
        }
    }

    @Test(dataProvider = "validUsers", retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"smoke"})
    public void loginWithValidUsers (String uname, String pass) {
        logger.info("Checking if login page is properly setup for positive case");
        Assert.assertNotNull(loginPage, "LoginPage is null. Setup might have failed.");
        logger.info("Login with with valid user - username: {} and password: {}", uname, pass);
        loginPage.login(uname, pass);

        logger.info("Checking if user is navigated to inventory page");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "Login should succeed but failed");

        logger.info("Login test case passed");
    }

    @Test(dataProvider = "invalidUsers", retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"regression"})
    public void loginWithInvalidUsers (String uname, String pass) {
        logger.info("Checking if login page is properly setup for negative case");
        Assert.assertNotNull(loginPage, "LoginPage is null. Setup might have failed.");
        logger.info("Login with invalid user - username: {} and password: {}", uname, pass);
        loginPage.login(uname, pass);

        logger.info("Checking if error is displayed");
        Assert.assertTrue(loginPage.isErrorVisible(), "Error should be displayed but it is not displaying.");
    }


    @DataProvider(name = "validUsers")
    public Object[][] provideValidUsers() {
        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"}
        };
    }

    @DataProvider(name = "invalidUsers")
    public Object[][] provideInvalidUsers() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce"},
                {"invalid_user", "wrong_pass"}
        };
    }
}
