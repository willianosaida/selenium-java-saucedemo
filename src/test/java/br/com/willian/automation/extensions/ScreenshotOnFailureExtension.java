package br.com.willian.automation.extensions;

import br.com.willian.automation.driver.HasWebDriver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Path SCREENSHOT_DIRECTORY = Path.of("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        Object testInstance = context.getRequiredTestInstance();
        if (!(testInstance instanceof HasWebDriver provider)) {
            return;
        }

        provider.currentWebDriver().ifPresent(driver -> saveScreenshot(driver, context.getDisplayName()));
    }

    private void saveScreenshot(WebDriver driver, String testName) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return;
        }

        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String timestamp = LocalDateTime.now().format(TIMESTAMP);
            Path destination = SCREENSHOT_DIRECTORY.resolve(safeName + "-" + timestamp + ".png");
            Files.write(destination, screenshotDriver.getScreenshotAs(OutputType.BYTES));
            System.out.println("Screenshot da falha salvo em: " + destination.toAbsolutePath());
        } catch (IOException exception) {
            System.err.println("Não foi possível salvar o screenshot: " + exception.getMessage());
        }
    }
}
