package br.com.willian.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return visible(loginButton).isDisplayed();
    }

    public InventoryPage loginSuccessfully(String username, String password) {
        fillCredentials(username, password);
        click(loginButton);
        return new InventoryPage(driver);
    }

    public LoginPage attemptLogin(String username, String password) {
        fillCredentials(username, password);
        click(loginButton);
        return this;
    }

    public String errorMessage() {
        return text(errorMessage);
    }

    private void fillCredentials(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
    }
}
