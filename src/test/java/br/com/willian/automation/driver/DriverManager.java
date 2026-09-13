package br.com.willian.automation.driver;

import java.util.Optional;
import org.openqa.selenium.WebDriver;

// Guarda e encerra a sessão do navegador usada pelo teste atual.
public final class DriverManager {

    // ThreadLocal separa o driver por thread (linha de execução).
    // Isso não ativa testes paralelos: neste projeto eles estão desabilitados no JUnit.
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver ainda não foi inicializado.");
        }
        return driver;
    }

    public static Optional<WebDriver> currentDriver() {
        return Optional.ofNullable(DRIVER.get());
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                // Remove a referência mesmo se ocorrer um erro ao fechar o navegador.
                DRIVER.remove();
            }
        }
    }
}
