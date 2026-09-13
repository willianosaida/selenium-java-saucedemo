package br.com.willian.automation.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.willian.automation.config.TestConfig;
import br.com.willian.automation.pages.CartPage;
import br.com.willian.automation.pages.InventoryPage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Catálogo e carrinho")
class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeEach
    void login() {
        inventoryPage = loginPage.loginSuccessfully(
                TestConfig.standardUsername(), TestConfig.standardPassword());
        assertTrue(inventoryPage.isLoaded(), "Pré-condição: login válido não foi concluído");
    }

    @Test
    @Tag("smoke")
    @DisplayName("Deve adicionar um produto ao carrinho")
    void shouldAddProductToCart() {
        String product = "Sauce Labs Backpack";

        CartPage cartPage = inventoryPage
                .addProductToCart(product)
                .openCart();

        assertTrue(cartPage.isLoaded(), "A página do carrinho deveria ser exibida");
        assertEquals(List.of(product), cartPage.productNames());
    }

    @Test
    @Tag("regression")
    @DisplayName("Deve ordenar os produtos do menor para o maior preço")
    void shouldSortProductsByPriceLowToHigh() {
        inventoryPage.sortBy("lohi");
        List<Double> actualPrices = inventoryPage.displayedPrices();
        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        sortedPrices.sort(Comparator.naturalOrder());

        assertEquals(sortedPrices, actualPrices, "Os preços deveriam estar em ordem crescente");
    }

    @Test
    @Tag("regression")
    @DisplayName("Deve atualizar a quantidade mostrada no ícone do carrinho")
    void shouldUpdateCartBadge() {
        inventoryPage
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light");

        assertEquals(2, inventoryPage.cartItemCount());
    }
}
