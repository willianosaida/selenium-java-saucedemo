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

@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseTest implements HasWebDriver {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.create();
        DriverManager.setDriver(driver);
        driver.get(TestConfig.baseUrl());
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    void tearDown() {
        DriverManager.quitDriver();
        driver = null;
    }

    @Override
    public Optional<WebDriver> currentWebDriver() {
        return DriverManager.currentDriver();
    }
}
