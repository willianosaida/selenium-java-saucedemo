package br.com.willian.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// Representa o carrinho, onde conferimos os produtos antes de iniciar o checkout.
public class CartPage extends BasePage {

    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Your Cart".equals(text(pageTitle));
    }

    public List<String> productNames() {
        // Transforma os elementos da tela em uma lista de textos que o teste pode comparar.
        return allVisible(itemNames).stream().map(WebElement::getText).toList();
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        // O clique navega no site; este objeto permite interagir com a próxima etapa.
        return new CheckoutPage(driver);
    }
}
