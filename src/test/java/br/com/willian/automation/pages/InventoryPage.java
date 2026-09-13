package br.com.willian.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

// Representa o catálogo: seleção de produtos, ordenação e acesso ao carrinho.
public class InventoryPage extends BasePage {

    // data-test é um atributo do HTML usado aqui para localizar elementos da interface.
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
        // Procura o botão dentro do produto escolhido, evitando clicar no de outro produto.
        item.findElement(addToCartButton).click();
        return this;
    }

    public int cartItemCount() {
        // findElements retorna uma lista vazia quando o contador não está presente.
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    public InventoryPage sortBy(String optionValue) {
        // Select é o recurso do Selenium para interagir com uma lista HTML do tipo <select>.
        new Select(visible(sortSelect)).selectByValue(optionValue);
        return this;
    }

    public List<Double> displayedPrices() {
        // Lê os preços na ordem da tela, remove o símbolo de moeda e converte para números.
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
        // Filtra os cartões pelo nome exato e informa um erro se o produto não for encontrado.
        return allVisible(inventoryItems).stream()
                .filter(item -> productName.equals(item.findElement(itemName).getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Produto não encontrado no catálogo: " + productName));
    }
}
