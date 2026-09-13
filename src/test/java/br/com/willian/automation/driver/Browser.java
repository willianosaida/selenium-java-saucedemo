package br.com.willian.automation.driver;

import java.util.Arrays;

public enum Browser {
    CHROME,
    FIREFOX,
    EDGE;

    public static Browser from(String value) {
        return Arrays.stream(values())
                .filter(browser -> browser.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Navegador inválido: '" + value + "'. Use chrome, firefox ou edge."));
    }
}
