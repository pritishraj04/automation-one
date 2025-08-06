package org.example.tests;

import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class InventoryTestCase {
    public WebDriver driver;
    InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
        inventoryPage = new InventoryPage(driver);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("standard_user", "secret_sauce");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class)
    public void test() {
        String[][] itemsToAdd = new String[][]{{"Sauce Labs Backpack", "29.99"}, {"Sauce Labs Fleece Jacket", "49.99"}, {"Test.allTheThings() T-Shirt (Red)", "15.99"}};
        String[] itemsToRemove = new String[]{"Sauce Labs Backpack"};
        int expectedTotalItemCount = 6;
        int expectedCartItemCount = itemsToAdd.length - itemsToRemove.length;


        int actualItemCount = inventoryPage.getAllItemCount();
        Assert.assertEquals(actualItemCount, expectedTotalItemCount, "Item Count is supposed to be " + expectedTotalItemCount + " but actual item count is " + actualItemCount);

        for (String[] item : itemsToAdd) {
            WebElement target = inventoryPage.getItem(item[0]);

            double actualPrice = inventoryPage.getItemPrice(target);
            double expectedPrice = Double.parseDouble(item[1]);

            Assert.assertEquals(actualPrice, expectedPrice, "Item Count is supposed to be " + expectedTotalItemCount + " but actual item count is " + actualItemCount);
            inventoryPage.addOrRemoveItemToCart(target);
        }

        for (String item: itemsToRemove) {
            inventoryPage.addOrRemoveItemToCart(inventoryPage.getItem(item));
        }

        int actualCartItemCount = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(actualCartItemCount, expectedCartItemCount, "Cart items Count is supposed to be " + expectedCartItemCount + " but actual item count is " + actualCartItemCount);

        inventoryPage.viewCart();

        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/cart.html";
        Assert.assertEquals(actualUrl, expectedUrl, "Current page is supposed to be " + expectedUrl + " but actual item count is " + actualUrl);
    }
}
