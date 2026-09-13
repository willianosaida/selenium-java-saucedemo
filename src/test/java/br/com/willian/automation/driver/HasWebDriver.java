package br.com.willian.automation.driver;

import java.util.Optional;
import org.openqa.selenium.WebDriver;

// Contrato que permite à extensão de screenshot consultar o navegador de uma classe de teste.
public interface HasWebDriver {
    Optional<WebDriver> currentWebDriver();
}
