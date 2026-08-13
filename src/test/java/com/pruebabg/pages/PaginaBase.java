package com.pruebabg.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.pruebabg.config.Configuracion;

import java.util.List;

public abstract class PaginaBase {

    protected final Page page;

    protected PaginaBase(Page page) {
        this.page = page;
    }

    protected void navigateTo(String path) {
        page.navigate(Configuracion.urlDe(path));
    }

    protected void waitForPortalResponse() {
        page.waitForLoadState();
    }

    protected void waitForOptionsOf(Locator dropdown) {
        dropdown.locator("option").first()
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
    }

    protected List<String> valuesOf(Locator dropdown) {
        waitForOptionsOf(dropdown);
        return dropdown.locator("option").all().stream()
                .map(option -> option.getAttribute("value"))
                .filter(value -> value != null && !value.isBlank())
                .toList();
    }

    public Locator leftPanel() {
        return page.locator("#leftPanel");
    }

    protected Locator errorContainers() {
        return page.locator("p.error, span.error");
    }

    public List<String> errorMessages() {
        return errorContainers().allTextContents().stream()
                .map(text -> text.replaceAll("\\s+", " ").trim())
                .filter(text -> !text.isBlank())
                .toList();
    }

    public void signOut() {
        page.locator("a[href*='logout.htm']").first().click();
        waitForPortalResponse();
    }
}
