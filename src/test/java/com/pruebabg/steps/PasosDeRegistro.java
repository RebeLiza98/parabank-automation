package com.pruebabg.steps;

import com.pruebabg.context.ContextoDelEscenario;
import com.pruebabg.data.GeneradorDeDatos;
import com.pruebabg.driver.GestorDePlaywright;
import com.pruebabg.model.Usuario;
import com.pruebabg.pages.PaginaResumenDeCuentas;
import com.pruebabg.pages.PaginaDeRegistro;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasosDeRegistro {

    private final GestorDePlaywright playwright;
    private final ContextoDelEscenario context;

    public PasosDeRegistro(GestorDePlaywright playwright, ContextoDelEscenario context) {
        this.playwright = playwright;
        this.context = context;
    }

    @Given("el visitante se encuentra en la página de registro")
    public void theVisitorIsOnTheRegistrationPage() {
        registrationPage().open();
    }

    @When("completa el formulario con datos válidos y lo envía")
    public void theyFillInTheRegistrationFormWithValidData() {
        Usuario user = GeneradorDeDatos.newUser();
        registrationPage().fillForm(user).submit();
        context.saveUser(user);
        context.log("Usuario registrado: " + user.username());
    }

    @Then("el portal confirma que la cuenta fue creada")
    public void thePortalConfirmsThatTheAccountWasCreated() {
        assertThat(registrationPage().confirmation())
                .containsText("Your account was created successfully");
    }

    @And("el usuario queda autenticado con acceso al resumen de cuentas")
    public void theUserEndsUpSignedIn() {
        assertThat(new PaginaResumenDeCuentas(playwright.page()).open().title()).isVisible();
    }

    @When("intenta registrarse nuevamente con el mismo nombre de usuario")
    public void theyTryToRegisterAgainWithTheSameUsername() {
        registrationPage().register(context.user());
    }

    @Then("el portal muestra el error {string}")
    public void thePortalShowsTheError(String expectedMessage) {
        List<String> errors = registrationPage().errorMessages();
        assertTrue(errors.contains(expectedMessage),
                "Se esperaba el mensaje '" + expectedMessage + "' pero el portal mostró: " + errors);
    }

    @When("envía el formulario de registro sin completar ningún campo")
    public void theySubmitTheEmptyRegistrationForm() {
        registrationPage().submit();
    }

    @Then("el portal exige los siguientes campos obligatorios:")
    public void thePortalRequiresTheFollowingMandatoryFields(List<String> expectedMessages) {
        List<String> errors = registrationPage().errorMessages();
        assertTrue(errors.containsAll(expectedMessages),
                "Faltaron mensajes de validación. Esperados: " + expectedMessages
                        + ". Mostrados: " + errors);
    }

    private PaginaDeRegistro registrationPage() {
        return new PaginaDeRegistro(playwright.page());
    }
}
