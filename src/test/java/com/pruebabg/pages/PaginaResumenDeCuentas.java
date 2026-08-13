package com.pruebabg.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class PaginaResumenDeCuentas extends PaginaBase {

    private static final String PATH = "/parabank/overview.htm";

    private final Locator title;

    public PaginaResumenDeCuentas(Page page) {
        super(page);
        this.title = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Accounts Overview"));
    }

    public PaginaResumenDeCuentas open() {
        navigateTo(PATH);
        return this;
    }

    public Locator title() {
        return title;
    }
}
