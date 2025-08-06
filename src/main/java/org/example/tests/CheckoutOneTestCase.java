package org.example.tests;

import org.example.pages.CartPage;
import org.example.pages.CheckoutOnePage;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CheckoutOneTestCase {
    public WebDriver driver;
    CartPage cartPage;
    CheckoutOnePage checkoutOnePage;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        String[] itemsToAdd = new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket", "Test.allTheThings() T-Shirt (Red)", "Sauce Labs Bike Light"};

        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);

        for(String item : itemsToAdd) {
            inventoryPage.addOrRemoveItemToCart(inventoryPage.getItem(item));
        }
        inventoryPage.viewCart();

        cartPage = new CartPage(driver);

        cartPage.clickCheckoutBtn();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class)
    public void test() {
        checkoutOnePage = new CheckoutOnePage(driver);

        checkoutOnePage.fillForm("Test", "Kumar", "123123");
        checkoutOnePage.submitForm();


        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/checkout-step-two.html";
        Assert.assertEquals(actualUrl, expectedUrl, "Page should have navigated to Checkout step two but url is: " + actualUrl);
    }
}
