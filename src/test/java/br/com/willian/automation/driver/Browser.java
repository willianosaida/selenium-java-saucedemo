package br.com.willian.automation.driver;

import java.util.Arrays;

// enum limita as opções aos navegadores que a fábrica sabe criar.
public enum Browser {
    CHROME,
    FIREFOX,
    EDGE;

    public static Browser from(String value) {
        // Aceita maiúsculas ou minúsculas e informa quais opções usar se o nome for inválido.
        return Arrays.stream(values())
                .filter(browser -> browser.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Navegador inválido: '" + value + "'. Use chrome, firefox ou edge."));
    }
}
