package org.example.tests;

import org.example.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.example.pages.LoginPage;

public class LoginTestCase extends BaseTest {
    @Test(dataProvider = "validUsers", retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"smoke", "regression"})
    public void loginWithValidUsers (String uname, String pass) {
        LoginPage loginPage = new LoginPage(driver);
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
        LoginPage loginPage = new LoginPage(driver);
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
