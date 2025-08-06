package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage {
    WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By itemName = By.xpath(".//div[@data-test='inventory-item-name']");
    private final By cartItem = By.className("cart_item");
    private final By removeBtn = By.cssSelector(".cart_item_label > .item_pricebar > button");
    private final By checkoutBtn = By.xpath("//button[@data-test='checkout']");
    private final By backBtn = By.xpath("//button[@data-test='continue-shopping']");

    public List<WebElement> getAllItems() {
        return driver.findElements(cartItem);
    }

    public WebElement getItem(String name) {
        List<WebElement> allItems = getAllItems();

        for (WebElement item : allItems) {
            if (item.findElement(itemName).getText().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(WebElement item) {
        item.findElement(removeBtn).click();
    }

    public void clickBackBtn() {
        driver.findElement(backBtn).click();
    }

    public void clickCheckoutBtn() {
        driver.findElement(checkoutBtn).click();
    }

}
