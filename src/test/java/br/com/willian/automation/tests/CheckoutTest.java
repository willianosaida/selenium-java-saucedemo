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

@DisplayName("Checkout")
class CheckoutTest extends BaseTest {

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("Deve concluir uma compra de ponta a ponta")
    void shouldCompletePurchaseEndToEnd() {
        String product = "Sauce Labs Backpack";

        InventoryPage inventoryPage = loginPage.loginSuccessfully(
                TestConfig.standardUsername(), TestConfig.standardPassword());

        CheckoutPage checkoutPage = inventoryPage
                .addProductToCart(product)
                .openCart()
                .proceedToCheckout()
                .fillCustomerData("Willian", "Osaida", "12200-000")
                .continueToOverview();

        assertEquals(List.of(product), checkoutPage.overviewProductNames());
        assertTrue(checkoutPage.total().startsWith("Total: $"),
                "O total da compra deveria ser apresentado");

        checkoutPage.finishOrder();

        assertEquals("Thank you for your order!", checkoutPage.confirmationMessage());
    }
}
