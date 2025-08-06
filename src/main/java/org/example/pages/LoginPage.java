package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver =  driver;
    }

    private final By userNameField = By.xpath("//input[@data-test='username']");
    private final By passwordField = By.xpath("//input[@data-test='password']");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.xpath("//div[contains(@class, 'error-message-container')]/h3[@data-test='error']");


    private void enterUserName(String username) {
        driver.findElement(userNameField).sendKeys(username);
    }

    private void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    private void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public boolean isErrorVisible() {
        return driver.findElement(errorMessage).isDisplayed();
    }



    public void login(String uname, String pass) {
        enterUserName(uname);
        enterPassword(pass);
        clickLogin();
    }
}
