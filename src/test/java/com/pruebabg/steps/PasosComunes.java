package com.pruebabg.steps;

import com.pruebabg.context.ContextoDelEscenario;
import com.pruebabg.data.GeneradorDeDatos;
import com.pruebabg.driver.GestorDePlaywright;
import com.pruebabg.model.Usuario;
import com.pruebabg.pages.PaginaAperturaDeCuenta;
import com.pruebabg.pages.PaginaDeRegistro;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PasosComunes {

    private final GestorDePlaywright playwright;
    private final ContextoDelEscenario context;

    public PasosComunes(GestorDePlaywright playwright, ContextoDelEscenario context) {
        this.playwright = playwright;
        this.context = context;
    }

    @Given("estoy autenticado en ParaBank")
    public void iAmSignedInToParaBank() {
        Usuario user = GeneradorDeDatos.newUser();
        PaginaDeRegistro registration = new PaginaDeRegistro(playwright.page()).register(user);
        assertThat(registration.confirmation()).containsText("Your account was created successfully");
        context.saveUser(user);
        context.log("Usuario creado para el escenario: " + user.username());
    }

    @Given("existe un usuario registrado en ParaBank")
    public void aUserIsAlreadyRegisteredInParaBank() {
        iAmSignedInToParaBank();
        new PaginaDeRegistro(playwright.page()).signOut();
    }

    @And("conozco el número de mi cuenta principal")
    public void iKnowMyMainAccountNumber() {
        List<String> accounts = new PaginaAperturaDeCuenta(playwright.page()).open().customerAccounts();
        assertFalse(accounts.isEmpty(),
                "El usuario recién registrado no tiene ninguna cuenta asociada en el portal.");
        context.saveSourceAccount(accounts.get(0));
        context.log("Cuenta principal del usuario: " + accounts.get(0));
    }
}
