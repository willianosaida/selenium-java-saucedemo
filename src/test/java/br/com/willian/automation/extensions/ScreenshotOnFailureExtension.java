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

// O JUnit chama esta extensão após o método de teste e antes do @AfterEach fechar o navegador.
// A imagem ajuda a investigar a falha, mas não substitui a mensagem de erro do teste.
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Path SCREENSHOT_DIRECTORY = Path.of("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            // Sem erro no método de teste, não precisamos registrar uma imagem.
            return;
        }

        Object testInstance = context.getRequiredTestInstance();
        if (!(testInstance instanceof HasWebDriver provider)) {
            return;
        }

        // Só tenta capturar se houver um driver disponível para esta execução.
        provider.currentWebDriver().ifPresent(driver -> saveScreenshot(driver, context.getDisplayName()));
    }

    private void saveScreenshot(WebDriver driver, String testName) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return;
        }

        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
            // Remove caracteres inadequados para nomes de arquivo e acrescenta data e hora.
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
