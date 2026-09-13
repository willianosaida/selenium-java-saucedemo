package br.com.willian.automation.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.willian.automation.config.TestConfig;
import br.com.willian.automation.pages.CheckoutPage;
import br.com.willian.automation.pages.InventoryPage;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

// Teste de ponta a ponta (E2E): percorre o caminho do usuário até concluir uma compra.
@DisplayName("Checkout")
class CheckoutTest extends BaseTest {

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("Deve concluir uma compra de ponta a ponta")
    void shouldCompletePurchaseEndToEnd() {
        // Preparação: escolhemos o produto e entramos com um usuário válido.
        String product = "Sauce Labs Backpack";

        InventoryPage inventoryPage = loginPage.loginSuccessfully(
                TestConfig.standardUsername(), TestConfig.standardPassword());

        // Ação: cada método representa uma etapa da compra na interface.
        CheckoutPage checkoutPage = inventoryPage
                .addProductToCart(product)
                .openCart()
                .proceedToCheckout()
                .fillCustomerData("Willian", "Osaida", "12200-000")
                .continueToOverview();

        // Verificação: assertEquals compara o resultado esperado (primeiro) com o obtido.
        assertEquals(List.of(product), checkoutPage.overviewProductNames());
        // Aqui verificamos apenas a apresentação do total, sem conferir o cálculo do valor.
        assertTrue(checkoutPage.total().startsWith("Total: $"),
                "O total da compra deveria ser apresentado");

        checkoutPage.finishOrder();

        // A mensagem final é a evidência que este teste usa para confirmar a conclusão.
        assertEquals("Thank you for your order!", checkoutPage.confirmationMessage());
    }
}
