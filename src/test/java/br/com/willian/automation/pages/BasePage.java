package br.com.willian.automation.pages;

import br.com.willian.automation.config.TestConfig;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// Page Object Model: as páginas cuidam da interação com a tela; os testes conferem os resultados.
// Esta classe reúne ações reutilizadas pelas páginas e não é instanciada diretamente (abstract).
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        // WebDriver controla o navegador. Todas as páginas do cenário usam a mesma sessão.
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.timeout());
    }

    protected WebElement visible(By locator) {
        // A espera explícita aguarda a condição até o limite configurado e falha se ela não ocorrer.
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        // Para esta condição do Selenium, o elemento precisa estar visível e habilitado.
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> allVisible(By locator) {
        // Espera encontrar elementos visíveis; uma lista vazia não satisfaz esta condição.
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        // Limpa o conteúdo anterior para não misturá-lo com o novo valor.
        element.clear();
        element.sendKeys(value);
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }
}
