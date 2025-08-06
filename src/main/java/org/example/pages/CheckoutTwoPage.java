package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckoutTwoPage {
    WebDriver driver;

    public CheckoutTwoPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By item = By.xpath("//div[@data-test=\"cart-list\"]/div[@data-test=\"inventory-item\"]");
    private final By itemName = By.xpath(".//div[@data-test=\"inventory-item-name\"]");
    private final By itemPrice = By.xpath(".//div[@data-test=\"inventory-item-price\"]");
    private final By subTotal = By.xpath("//div[@data-test=\"subtotal-label\"]");
    private final By taxTotal = By.xpath("//div[@data-test=\"tax-label\"]");
    private final By total = By.xpath("//div[@data-test=\"total-label\"]");
    private final By finishBtn = By.id("finish");

    public List<WebElement> getItems() {
        return driver.findElements(item);
    }

    public String[] namesOfAllItems() {
        List<WebElement> allItems = getItems();
        int index = 0;
        String[] itemNames = new String[allItems.size()];

        for (WebElement item : allItems) {
            itemNames[index] = item.findElement(itemName).getText();
            index++;
        }
        return itemNames;
    }

    public double getPrice(WebElement item) {
        return Double.parseDouble(item.findElement(itemPrice).getText().replaceAll("\\$", ""));
    }

    public double calculateSubTotal() {
        List<WebElement> allItems = getItems();
        double total = 0;
        for (WebElement item : allItems) {
            total += getPrice(item);
        }
        return total;
    }
    public double getSubTotal() {
        return Double.parseDouble(driver.findElement(subTotal).getText().replaceAll("Item total: \\$", ""));
    }

    public double getTax() {
        return Double.parseDouble(driver.findElement(taxTotal).getText().replaceAll("Tax: \\$", ""));
    }

    public double getTotal() {
        return Double.parseDouble(driver.findElement(total).getText().replaceAll("Total: \\$", ""));
    }

    public void finishOrder() {
        driver.findElement(finishBtn).click();
    }


}
