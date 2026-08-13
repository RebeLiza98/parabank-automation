package com.pruebabg.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

public class PaginaDeTransferencia extends PaginaBase {

    private static final String PATH = "/parabank/transfer.htm";

    private final Locator amount;
    private final Locator sourceAccount;
    private final Locator targetAccount;
    private final Locator transferButton;
    private final Locator resultTitle;
    private final Locator resultDetail;

    public PaginaDeTransferencia(Page page) {
        super(page);
        this.amount = page.locator("input#amount");
        this.sourceAccount = page.locator("select#fromAccountId");
        this.targetAccount = page.locator("select#toAccountId");
        this.transferButton = page.locator("input[value='Transfer']");
        this.resultTitle = page.locator("#showResult h1.title");
        this.resultDetail = page.locator("#showResult p").first();
    }

    public PaginaDeTransferencia open() {
        navigateTo(PATH);
        waitForOptionsOf(sourceAccount);
        return this;
    }

    public PaginaDeTransferencia transfer(String value, String from, String to) {
        amount.fill(value);
        sourceAccount.selectOption(new SelectOption().setValue(from));
        targetAccount.selectOption(new SelectOption().setValue(to));
        transferButton.click();
        waitForPortalResponse();
        return this;
    }

    public Locator resultTitle() {
        return resultTitle;
    }

    public Locator resultDetail() {
        return resultDetail;
    }
}
