package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutOnePage {
    WebDriver driver;

    public CheckoutOnePage(WebDriver driver) {
        this.driver = driver;
    }

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By zipCode = By.id("postal-code");
    private final By continueBtn = By.id("continue");

    private void enterFirstName(String fName) {
        driver.findElement(firstName).sendKeys(fName);
    }

    private void enterLastName(String lName) {
        driver.findElement(lastName).sendKeys(lName);
    }

    private void enterPostalCode(String pCode) {
        driver.findElement(zipCode).sendKeys(pCode);
    }

    public void fillForm(String fName, String lName, String pCode) {
        enterFirstName(fName);
        enterLastName(lName);
        enterPostalCode(pCode);
    }

    public void submitForm() {
        driver.findElement(continueBtn).click();
    }
}
