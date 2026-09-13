package br.com.willian.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

// Centraliza os valores do ambiente para evitar repetir URL, credenciais e tempos nos testes.
public final class TestConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private TestConfig() {
        // Usamos os métodos pela classe, sem precisar criar um objeto TestConfig.
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        // Lê o recurso de src/test/resources. O try fecha o arquivo automaticamente ao terminar.
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Arquivo não encontrado no classpath: " + CONFIG_FILE);
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Não foi possível carregar " + CONFIG_FILE, exception);
        }
    }

    private static String value(String key) {
        // Uma opção enviada com -D no Maven tem prioridade sobre o arquivo de configuração.
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String fileValue = PROPERTIES.getProperty(key);
        if (fileValue == null || fileValue.isBlank()) {
            throw new IllegalStateException("Configuração obrigatória não informada: " + key);
        }
        return fileValue.trim();
    }

    public static String baseUrl() {
        return value("base.url");
    }

    public static String browser() {
        return value("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(value("headless"));
    }

    public static Duration timeout() {
        // Converte os segundos escritos no arquivo em uma duração usada pelas esperas do Selenium.
        return Duration.ofSeconds(Long.parseLong(value("timeout.seconds")));
    }

    public static int windowWidth() {
        return Integer.parseInt(value("window.width"));
    }

    public static int windowHeight() {
        return Integer.parseInt(value("window.height"));
    }

    public static String standardUsername() {
        return value("standard.username");
    }

    public static String standardPassword() {
        return value("standard.password");
    }
}
