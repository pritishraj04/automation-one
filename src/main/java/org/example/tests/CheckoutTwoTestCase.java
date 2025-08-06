package org.example.tests;

import org.example.pages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CheckoutTwoTestCase {
    public WebDriver driver;
    CheckoutTwoPage checkoutTwoPage;

    String[] itemsToAdd = new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket", "Test.allTheThings() T-Shirt (Red)", "Sauce Labs Bike Light"};


    @BeforeMethod(alwaysRun = true)
    public void setup() {
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

        CartPage cartPage = new CartPage(driver);
        cartPage.clickCheckoutBtn();

        CheckoutOnePage checkoutOnePage = new CheckoutOnePage(driver);
        checkoutOnePage.fillForm("Test", "Kumar", "123123");
        checkoutOnePage.submitForm();

        checkoutTwoPage = new CheckoutTwoPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class)
    public void test() {
        String[] itemsAdded = checkoutTwoPage.namesOfAllItems();

        Assert.assertEquals(itemsAdded, itemsToAdd, "Items added do not appear correctly on summary page");

        double actualSubTotal = checkoutTwoPage.calculateSubTotal();
        double expectedSubTotal = checkoutTwoPage.getSubTotal();

        Assert.assertEquals(actualSubTotal, expectedSubTotal, 0.01, "Subtotal does not match");

        double actualTotal = actualSubTotal + checkoutTwoPage.getTax();
        double expectedTotal = checkoutTwoPage.getTotal();

        Assert.assertEquals(actualTotal, expectedTotal, 0.01 , "Total does not match");

        checkoutTwoPage.finishOrder();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html", "Expected to land on order complete page but currently at: " + driver.getCurrentUrl());

    }
}
