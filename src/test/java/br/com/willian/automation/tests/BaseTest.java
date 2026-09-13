package br.com.willian.automation.tests;

import br.com.willian.automation.config.TestConfig;
import br.com.willian.automation.driver.DriverFactory;
import br.com.willian.automation.driver.DriverManager;
import br.com.willian.automation.driver.HasWebDriver;
import br.com.willian.automation.extensions.ScreenshotOnFailureExtension;
import br.com.willian.automation.pages.LoginPage;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

// Base comum herdada pelos testes: concentra a preparação e a limpeza de cada cenário.
// A extensão tenta salvar uma imagem se o método de teste falhar.
@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseTest implements HasWebDriver {

    protected WebDriver driver;
    protected LoginPage loginPage;

    // O JUnit executa este método antes de cada teste, criando uma sessão sem login anterior.
    @BeforeEach
    void setUp() {
        driver = DriverFactory.create();
        DriverManager.setDriver(driver);
        driver.get(TestConfig.baseUrl());
        loginPage = new LoginPage(driver);
    }

    // Executado após cada teste, inclusive quando uma verificação falha.
    // quitDriver encerra toda a sessão do navegador e libera seus recursos.
    @AfterEach
    void tearDown() {
        DriverManager.quitDriver();
        driver = null;
    }

    @Override
    public Optional<WebDriver> currentWebDriver() {
        // Optional representa a possibilidade de ainda não existir um navegador disponível.
        return DriverManager.currentDriver();
    }
}
