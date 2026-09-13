package br.com.willian.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Representa a tela de login e reúne seus campos, botões e ações.
public class LoginPage extends BasePage {

    // Localizadores (By) descrevem como encontrar elementos no HTML: por id ou seletor CSS.
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
        // Representa a tela esperada após o login; o teste ainda precisa verificar se ela abriu.
        return new InventoryPage(driver);
    }

    public LoginPage attemptLogin(String username, String password) {
        fillCredentials(username, password);
        click(loginButton);
        // Retorna a própria página para permitir consultar o erro após a tentativa de login.
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
