package com.pruebabg.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class PaginaDeIngreso extends PaginaBase {

    private static final String PATH = "/parabank/index.htm";

    private final Locator username;
    private final Locator password;
    private final Locator signInButton;

    public PaginaDeIngreso(Page page) {
        super(page);
        this.username = page.locator("input[name='username']");
        this.password = page.locator("input[name='password']");
        this.signInButton = page.locator("input[value='Log In']");
    }

    public PaginaDeIngreso open() {
        navigateTo(PATH);
        return this;
    }

    public PaginaDeIngreso signInWith(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        signInButton.click();
        waitForPortalResponse();
        return this;
    }

    public Locator loginForm() {
        return page.locator("#loginPanel");
    }
}
