package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InventoryTestCase extends BaseTest {
    protected void testSetup() {
        logger.info("Navigating to https://www.saucedemo.com/");
        driver.get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"smoke", "regression"})
    public void addingItemsToCart() {
        InventoryPage inventoryPage = new InventoryPage(driver);
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
            logger.info("Adding Item to cart");
            inventoryPage.addOrRemoveItemToCart(target);
        }

        for (String item: itemsToRemove) {
            logger.info("Removing Item from  cart");
            inventoryPage.addOrRemoveItemToCart(inventoryPage.getItem(item));
        }

        int actualCartItemCount = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(actualCartItemCount, expectedCartItemCount, "Cart items Count is supposed to be " + expectedCartItemCount + " but actual item count is " + actualCartItemCount);

        logger.info("Navigating to cart page");
        inventoryPage.viewCart();

        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/cart.html";
        Assert.assertEquals(actualUrl, expectedUrl, "Current page is supposed to be " + expectedUrl + " but actual item count is " + actualUrl);
    }
}
