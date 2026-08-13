package com.pruebabg.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pruebabg.model.Usuario;

public class PaginaDeRegistro extends PaginaBase {

    private static final String PATH = "/parabank/register.htm";

    private final Locator firstName;
    private final Locator lastName;
    private final Locator address;
    private final Locator city;
    private final Locator state;
    private final Locator zipCode;
    private final Locator phoneNumber;
    private final Locator ssn;
    private final Locator username;
    private final Locator password;
    private final Locator passwordConfirmation;
    private final Locator registerButton;
    private final Locator confirmation;

    public PaginaDeRegistro(Page page) {
        super(page);
        this.firstName = page.locator("input[name='customer.firstName']");
        this.lastName = page.locator("input[name='customer.lastName']");
        this.address = page.locator("input[name='customer.address.street']");
        this.city = page.locator("input[name='customer.address.city']");
        this.state = page.locator("input[name='customer.address.state']");
        this.zipCode = page.locator("input[name='customer.address.zipCode']");
        this.phoneNumber = page.locator("input[name='customer.phoneNumber']");
        this.ssn = page.locator("input[name='customer.ssn']");
        this.username = page.locator("input[name='customer.username']");
        this.password = page.locator("input[name='customer.password']");
        this.passwordConfirmation = page.locator("input[name='repeatedPassword']");
        this.registerButton = page.locator("input[value='Register']");
        this.confirmation = page.locator("#rightPanel p").first();
    }

    public PaginaDeRegistro open() {
        navigateTo(PATH);
        return this;
    }

    public PaginaDeRegistro fillForm(Usuario user) {
        firstName.fill(user.firstName());
        lastName.fill(user.lastName());
        address.fill(user.address());
        city.fill(user.city());
        state.fill(user.state());
        zipCode.fill(user.zipCode());
        phoneNumber.fill(user.phoneNumber());
        ssn.fill(user.ssn());
        username.fill(user.username());
        password.fill(user.password());
        passwordConfirmation.fill(user.password());
        return this;
    }

    public PaginaDeRegistro submit() {
        registerButton.click();
        waitForPortalResponse();
        return this;
    }

    public PaginaDeRegistro register(Usuario user) {
        return open().fillForm(user).submit();
    }

    public Locator confirmation() {
        return confirmation;
    }
}
