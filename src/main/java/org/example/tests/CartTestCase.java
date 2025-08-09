package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CartPage;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CartTestCase extends BaseTest {
    CartPage cartPage;
    InventoryPage inventoryPage;

    public void testSetup() {
        String[] itemsToAdd = new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket", "Test.allTheThings() T-Shirt (Red)", "Sauce Labs Bike Light"};

        logger.info("Navigating to https://www.saucedemo.com/");
        driver.get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage = new InventoryPage(driver);
        for(String item : itemsToAdd) {
            logger.info("Adding Item to cart");
            inventoryPage.addOrRemoveItemToCart(inventoryPage.getItem(item));
        }
        logger.info("Navigating to cart page");
        inventoryPage.viewCart();

        cartPage = new CartPage(driver);
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"smoke", "regression"})
    public void proceedingToCheckoutWithItems() {
        cartPage.clickCheckoutBtn();
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/checkout-step-one.html";
        Assert.assertEquals(actualUrl, expectedUrl, "Current page is supposed to be " + expectedUrl + " but actual item count is " + actualUrl);
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"regression"})
    public void goingBackToInventoryPage() {
        logger.info("Navigating back to inventory page");
        cartPage.clickBackBtn();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "Current page is supposed to be https://www.saucedemo.com/inventory.html but actual item count is " + driver.getCurrentUrl());
        inventoryPage.viewCart();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html", "Current page is supposed to be https://www.saucedemo.com/cart.html but actual item count is " + driver.getCurrentUrl());
    }
    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"regression"})
    public void removeItemFromCart() {
        List<WebElement> allItems = cartPage.getAllItems();
        Assert.assertEquals(allItems.size(), 4, "Total Cart item expected to be " + 4 + " but found out to be" + allItems.size());

        String itemToRemove = "Sauce Labs Fleece Jacket";
        WebElement item = cartPage.getItem(itemToRemove);
        Assert.assertNotNull(item, "Item to remove not found in the cart: " + itemToRemove);
        cartPage.removeItem(cartPage.getItem(itemToRemove));
        allItems = cartPage.getAllItems();
        Assert.assertEquals(allItems.size(), 3, "Total Cart item expected to be " + 3 + " but found out to be" + allItems.size());
    }
}
