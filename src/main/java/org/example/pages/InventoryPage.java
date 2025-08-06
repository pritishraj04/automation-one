package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InventoryPage {
    WebDriver driver;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By item = By.xpath("//div[@data-test='inventory-item']");
    private final By itemName = By.xpath(".//div[@data-test='inventory-item-name']");
    private final By itemPrice = By.xpath(".//div[@data-test='inventory-item-price']");
    private final By addToCartBtn = By.cssSelector(".inventory_item_description > .pricebar > button");
    private final By cartBadge = By.xpath("//span[@data-test='shopping-cart-badge']");
    private final By cartBtn = By.xpath("//a[@data-test='shopping-cart-link']");


    public int getAllItemCount() {
        return driver.findElements(item).size();
    }

    public WebElement getItem(String name) {
        List<WebElement> allItems = driver.findElements(item);
        for (WebElement item : allItems) {
            if (!item.findElements(itemName).isEmpty() && item.findElement(itemName).getText().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public double getItemPrice(WebElement item) {
        String priceString = item.findElement(itemPrice).getText().replaceAll("\\$", "");
        return Double.parseDouble(priceString);
    }

    public void addOrRemoveItemToCart(WebElement item) {
        item.findElement(addToCartBtn).click();
    }

    public int getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(driver.findElement(cartBadge).getText());
    }

    public void viewCart() {
        driver.findElement(cartBtn).click();
    }

}
