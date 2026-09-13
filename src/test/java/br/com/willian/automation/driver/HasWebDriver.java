package br.com.willian.automation.driver;

import java.util.Optional;
import org.openqa.selenium.WebDriver;

public interface HasWebDriver {
    Optional<WebDriver> currentWebDriver();
}
