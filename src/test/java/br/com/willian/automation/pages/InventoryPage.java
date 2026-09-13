package br.com.willian.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By addToCartButton = By.cssSelector("button.btn_inventory");
    private final By sortSelect = By.cssSelector("[data-test='product-sort-container']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Products".equals(text(pageTitle)) && driver.getCurrentUrl().contains("inventory.html");
    }

    public InventoryPage addProductToCart(String productName) {
        WebElement item = findItem(productName);
        item.findElement(addToCartButton).click();
        return this;
    }

    public int cartItemCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    public InventoryPage sortBy(String optionValue) {
        new Select(visible(sortSelect)).selectByValue(optionValue);
        return this;
    }

    public List<Double> displayedPrices() {
        return allVisible(itemPrice).stream()
                .map(WebElement::getText)
                .map(value -> value.replace("$", ""))
                .map(Double::parseDouble)
                .toList();
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    private WebElement findItem(String productName) {
        return allVisible(inventoryItems).stream()
                .filter(item -> productName.equals(item.findElement(itemName).getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Produto não encontrado no catálogo: " + productName));
    }
}
