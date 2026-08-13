package com.pruebabg.steps;

import com.pruebabg.context.ContextoDelEscenario;
import com.pruebabg.driver.GestorDePlaywright;
import com.pruebabg.model.Usuario;
import com.pruebabg.pages.PaginaResumenDeCuentas;
import com.pruebabg.pages.PaginaDeIngreso;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasosDeIngreso {

    private static final String REGISTERED_USER = "REGISTERED_USER";

    private final GestorDePlaywright playwright;
    private final ContextoDelEscenario context;

    public PasosDeIngreso(GestorDePlaywright playwright, ContextoDelEscenario context) {
        this.playwright = playwright;
        this.context = context;
    }

    @When("ingresa con sus credenciales válidas")
    public void theySignInWithTheirValidCredentials() {
        Usuario user = context.user();
        loginPage().open().signInWith(user.username(), user.password());
    }

    @When("intenta ingresar con el usuario {string} y la contraseña {string}")
    public void theyTryToSignInWith(String username, String password) {
        loginPage().open().signInWith(resolve(username), password);
    }

    @Then("accede al resumen de cuentas")
    public void theyReachTheAccountsOverview() {
        PaginaResumenDeCuentas overview = new PaginaResumenDeCuentas(playwright.page());
        assertThat(overview.title()).isVisible();
        assertThat(overview.leftPanel()).containsText("Welcome");
    }

    @Then("el portal rechaza el ingreso")
    public void thePortalRejectsTheSignIn() {
        PaginaDeIngreso page = loginPage();
        context.log("Mensajes devueltos por el portal: " + page.errorMessages());
        assertThat(page.loginForm()).isVisible();
    }

    @And("muestra el mensaje {string}")
    public void showsTheMessage(String expectedMessage) {
        List<String> errors = loginPage().errorMessages();
        assertTrue(errors.contains(expectedMessage),
                "Se esperaba el mensaje '" + expectedMessage + "' pero el portal mostró: " + errors);
    }

    private String resolve(String username) {
        return REGISTERED_USER.equals(username) ? context.user().username() : username;
    }

    private PaginaDeIngreso loginPage() {
        return new PaginaDeIngreso(playwright.page());
    }
}
