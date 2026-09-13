package br.com.willian.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By overviewItemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By totalLabel = By.cssSelector("[data-test='total-label']");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillCustomerData(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutPage continueToOverview() {
        click(continueButton);
        return this;
    }

    public List<String> overviewProductNames() {
        return allVisible(overviewItemNames).stream().map(WebElement::getText).toList();
    }

    public String total() {
        return text(totalLabel);
    }

    public CheckoutPage finishOrder() {
        click(finishButton);
        return this;
    }

    public String confirmationMessage() {
        return text(completeHeader);
    }
}
