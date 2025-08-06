package org.example.tests;

import org.example.pages.CartPage;
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
import java.util.List;
import java.util.Map;

public class CartTestCase {
    public WebDriver driver;
    CartPage cartPage;
    InventoryPage inventoryPage;

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

        inventoryPage = new InventoryPage(driver);
        for(String item : itemsToAdd) {
            inventoryPage.addOrRemoveItemToCart(inventoryPage.getItem(item));
        }
        inventoryPage.viewCart();

        cartPage = new CartPage(driver);


    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class)
    public void test() {
        cartPage.clickBackBtn();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "Current page is supposed to be https://www.saucedemo.com/inventory.html but actual item count is " + driver.getCurrentUrl());
        inventoryPage.viewCart();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html", "Current page is supposed to be https://www.saucedemo.com/cart.html but actual item count is " + driver.getCurrentUrl());

        List<WebElement> allItems = cartPage.getAllItems();
        Assert.assertEquals(allItems.size(), 4, "Total Cart item expected to be " + 4 + " but found out to be" + allItems.size());

        String itemToRemove = "Sauce Labs Fleece Jacket";
        WebElement item = cartPage.getItem(itemToRemove);
        Assert.assertNotNull(item, "Item to remove not found in the cart: " + itemToRemove);
        cartPage.removeItem(cartPage.getItem(itemToRemove));
        allItems = cartPage.getAllItems();
        Assert.assertEquals(allItems.size(), 3, "Total Cart item expected to be " + 3 + " but found out to be" + allItems.size());

        cartPage.clickCheckoutBtn();
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/checkout-step-one.html";
        Assert.assertEquals(actualUrl, expectedUrl, "Current page is supposed to be " + expectedUrl + " but actual item count is " + actualUrl);
    }
}
