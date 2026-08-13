package com.pruebabg.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.util.List;

public class PaginaAperturaDeCuenta extends PaginaBase {

    private static final String PATH = "/parabank/openaccount.htm";

    private final Locator accountType;
    private final Locator sourceAccount;
    private final Locator openAccountButton;
    private final Locator newAccountId;

    public PaginaAperturaDeCuenta(Page page) {
        super(page);
        this.accountType = page.locator("select#type");
        this.sourceAccount = page.locator("select#fromAccountId");
        this.openAccountButton = page.locator("input[value='Open New Account']");
        this.newAccountId = page.locator("#newAccountId");
    }

    public PaginaAperturaDeCuenta open() {
        navigateTo(PATH);
        waitForOptionsOf(sourceAccount);
        return this;
    }

    public List<String> customerAccounts() {
        return valuesOf(sourceAccount);
    }

    public String openAccountOfType(String type) {
        accountType.selectOption(new SelectOption().setLabel(type));
        openAccountButton.click();
        newAccountId.waitFor();
        return newAccountId.innerText().trim();
    }
}
