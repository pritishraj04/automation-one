package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CartPage;
import org.example.pages.CheckoutOnePage;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CheckoutOneTestCase extends BaseTest {
    public void testSetup() {
        String[] itemsToAdd = new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket", "Test.allTheThings() T-Shirt (Red)", "Sauce Labs Bike Light"};

        logger.info("Navigating to https://www.saucedemo.com/");
        driver.get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        for(String item : itemsToAdd) {
            logger.info("Adding Item to cart");
            inventoryPage.addOrRemoveItemToCart(inventoryPage.getItem(item));
        }
        logger.info("Navigating to cart page");
        inventoryPage.viewCart();

        CartPage cartPage = new CartPage(driver);
        logger.info("Navigating to checkout user details page");
        cartPage.clickCheckoutBtn();
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"smoke", "regression"})
    public void submittingUserDetails() {
        CheckoutOnePage checkoutOnePage = new CheckoutOnePage(driver);

        logger.info("Filling up user details");
        checkoutOnePage.fillForm("Test", "Kumar", "123123");
        logger.info("Submitting form");
        checkoutOnePage.submitForm();


        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/checkout-step-two.html";
        Assert.assertEquals(actualUrl, expectedUrl, "Page should have navigated to Checkout step two but url is: " + actualUrl);
    }
}
