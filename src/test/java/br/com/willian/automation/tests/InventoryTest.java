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

// Verifica comportamentos do catálogo e do carrinho com um usuário autenticado.
@DisplayName("Catálogo e carrinho")
class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    // O preparo da BaseTest acontece primeiro. Depois fazemos login antes de cada cenário.
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
        // Preparação: um produto conhecido torna o resultado esperado fácil de conferir.
        String product = "Sauce Labs Backpack";

        CartPage cartPage = inventoryPage
                .addProductToCart(product)
                .openCart();

        assertTrue(cartPage.isLoaded(), "A página do carrinho deveria ser exibida");
        // Confere a lista inteira: deve haver somente o produto escolhido.
        assertEquals(List.of(product), cartPage.productNames());
    }

    @Test
    @Tag("regression")
    @DisplayName("Deve ordenar os produtos do menor para o maior preço")
    void shouldSortProductsByPriceLowToHigh() {
        // "lohi" é o valor da opção do site para ordenar do menor para o maior preço.
        inventoryPage.sortBy("lohi");
        List<Double> actualPrices = inventoryPage.displayedPrices();
        // Ordenamos uma cópia para preservar a ordem real capturada da tela.
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

        // O contador deve refletir os dois produtos adicionados neste cenário.
        assertEquals(2, inventoryPage.cartItemCount());
    }
}
