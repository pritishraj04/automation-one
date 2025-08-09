package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CheckoutTwoTestCase extends BaseTest {

    String[] itemsToAdd = new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket", "Test.allTheThings() T-Shirt (Red)", "Sauce Labs Bike Light"};

    protected void testSetup() {
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

        CheckoutOnePage checkoutOnePage = new CheckoutOnePage(driver);
        logger.info("Filling up user details");
        checkoutOnePage.fillForm("Test", "Kumar", "123123");
        logger.info("Submitting form");
        checkoutOnePage.submitForm();
    }

    @Test(retryAnalyzer = org.example.listeners.RetryAnalyser.class, groups = {"smoke", "regression"})
    public void placingOrder() {
        CheckoutTwoPage checkoutTwoPage = new CheckoutTwoPage(driver);
        String[] itemsAdded = checkoutTwoPage.namesOfAllItems();

        Assert.assertEquals(itemsAdded, itemsToAdd, "Items added do not appear correctly on summary page");

        double actualSubTotal = checkoutTwoPage.calculateSubTotal();
        double expectedSubTotal = checkoutTwoPage.getSubTotal();

        Assert.assertEquals(actualSubTotal, expectedSubTotal, 0.01, "Subtotal does not match");

        double actualTotal = actualSubTotal + checkoutTwoPage.getTax();
        double expectedTotal = checkoutTwoPage.getTotal();

        Assert.assertEquals(actualTotal, expectedTotal, 0.01 , "Total does not match");

        logger.info("Placing order and navigating to order details page");
        checkoutTwoPage.finishOrder();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html", "Expected to land on order complete page but currently at: " + driver.getCurrentUrl());

    }
}
