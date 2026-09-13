package br.com.willian.automation.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.willian.automation.config.TestConfig;
import br.com.willian.automation.pages.InventoryPage;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

// Casos positivos verificam o acesso permitido; negativos verificam as rejeições esperadas.
@DisplayName("Login")
class LoginTest extends BaseTest {

    // @Test identifica um teste; @DisplayName define o nome que aparece nos resultados.
    // A tag smoke agrupa verificações essenciais para uma checagem rápida do sistema.
    @Test
    @Tag("smoke")
    @DisplayName("Deve entrar com um usuário válido")
    void shouldLoginWithValidUser() {
        // Ação: enviamos as credenciais públicas configuradas para o ambiente de treinamento.
        InventoryPage inventoryPage = loginPage.loginSuccessfully(
                TestConfig.standardUsername(), TestConfig.standardPassword());

        // assertTrue exige uma condição verdadeira; a mensagem ajuda a entender a falha.
        assertTrue(inventoryPage.isLoaded(), "A página de produtos deveria ser exibida");
    }

    // Executa o mesmo teste para cada conjunto de dados fornecido por invalidCredentials.
    // {index} é o número da execução e {0} é a descrição do cenário recebido.
    // A tag regression agrupa cenários para detectar problemas após mudanças no sistema.
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidCredentials")
    @Tag("regression")
    @DisplayName("Deve rejeitar credenciais inválidas")
    void shouldRejectInvalidCredentials(String scenario, String username, String password,
                                        String expectedMessage) {
        loginPage.attemptLogin(username, password);

        // Uma rejeição correta faz este teste passar: comparamos a mensagem esperada com a exibida.
        assertEquals(expectedMessage, loginPage.errorMessage());
    }

    @Test
    @Tag("regression")
    @DisplayName("Deve impedir o acesso do usuário bloqueado")
    void shouldRejectLockedOutUser() {
        loginPage.attemptLogin("locked_out_user", TestConfig.standardPassword());

        assertEquals("Epic sadface: Sorry, this user has been locked out.", loginPage.errorMessage());
    }

    private static Stream<Arguments> invalidCredentials() {
        // Cada linha de dados contém: descrição, usuário, senha e mensagem esperada.
        return Stream.of(
                Arguments.of(
                        "usuário e senha incorretos",
                        "usuario_invalido",
                        "senha_invalida",
                        "Epic sadface: Username and password do not match any user in this service"),
                Arguments.of(
                        "usuário vazio",
                        "",
                        "secret_sauce",
                        "Epic sadface: Username is required"),
                Arguments.of(
                        "senha vazia",
                        "standard_user",
                        "",
                        "Epic sadface: Password is required")
        );
    }
}
