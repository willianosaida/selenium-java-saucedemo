package br.com.willian.automation.driver;

import br.com.willian.automation.config.TestConfig;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

// Fábrica: concentra a criação do WebDriver conforme o navegador escolhido na configuração.
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create() {
        Browser browser = Browser.from(TestConfig.browser());
        WebDriver driver = switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions());
            case FIREFOX -> new FirefoxDriver(firefoxOptions());
            case EDGE -> new EdgeDriver(edgeOptions());
        };

        // Padroniza o tamanho da janela para reduzir variações no layout durante os testes.
        driver.manage().window().setSize(
                new Dimension(TestConfig.windowWidth(), TestConfig.windowHeight()));
        return driver;
    }

    private static ChromeOptions chromeOptions() {
        // Headless executa sem janela visível, como no pipeline de integração contínua.
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-search-engine-choice-screen");
        if (TestConfig.headless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (TestConfig.headless()) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications");
        if (TestConfig.headless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }
}
