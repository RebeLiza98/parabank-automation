package com.pruebabg.steps;

import com.pruebabg.api.ServicioBancario;
import com.pruebabg.context.ContextoDelEscenario;
import com.pruebabg.driver.GestorDePlaywright;
import com.pruebabg.pages.PaginaAperturaDeCuenta;
import com.pruebabg.pages.PaginaDeTransferencia;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasosDeTransferencia {

    private final GestorDePlaywright playwright;
    private final ContextoDelEscenario context;

    public PasosDeTransferencia(GestorDePlaywright playwright, ContextoDelEscenario context) {
        this.playwright = playwright;
        this.context = context;
    }

    @And("dispongo de una segunda cuenta para recibir los fondos")
    public void iHaveASecondAccountToReceiveTheFunds() {
        String newAccount = new PaginaAperturaDeCuenta(playwright.page())
                .open()
                .openAccountOfType("SAVINGS");
        context.saveTargetAccount(newAccount);
        context.log("Cuenta destino abierta para el escenario: " + newAccount);
    }

    @When("transfiero {string} dólares de mi cuenta origen a mi cuenta destino")
    public void iTransferFromSourceToTarget(String amount) {
        ServicioBancario service = service();
        context.saveSourceBalanceBefore(service.balanceOf(context.sourceAccount()));
        context.saveTargetBalanceBefore(service.balanceOf(context.targetAccount()));
        context.log("Saldos antes de transferir -> origen: " + context.sourceBalanceBefore()
                + " | destino: " + context.targetBalanceBefore());

        new PaginaDeTransferencia(playwright.page())
                .open()
                .transfer(amount, context.sourceAccount(), context.targetAccount());
    }

    @Then("el portal confirma que la transferencia fue completada")
    public void thePortalConfirmsTheTransfer() {
        PaginaDeTransferencia page = new PaginaDeTransferencia(playwright.page());
        assertThat(page.resultTitle()).isVisible();
        assertThat(page.resultTitle()).containsText("Transfer Complete");
    }

    @And("el saldo de la cuenta origen disminuye en {string}")
    public void theSourceAccountBalanceDecreasesBy(String amount) {
        BigDecimal expected = context.sourceBalanceBefore().subtract(new BigDecimal(amount));
        verifyBalance(context.sourceAccount(), expected, "origen");
    }

    @And("el saldo de la cuenta destino aumenta en {string}")
    public void theTargetAccountBalanceIncreasesBy(String amount) {
        BigDecimal expected = context.targetBalanceBefore().add(new BigDecimal(amount));
        verifyBalance(context.targetAccount(), expected, "destino");
    }

    @Then("el portal no confirma la transferencia")
    public void thePortalDoesNotConfirmTheTransfer() {
        assertThat(new PaginaDeTransferencia(playwright.page()).resultTitle()).not().isVisible();
    }

    @And("muestra el mensaje de validación {string}")
    public void showsTheValidationMessage(String expectedMessage) {
        List<String> errors = new PaginaDeTransferencia(playwright.page()).errorMessages();
        assertTrue(errors.contains(expectedMessage),
                "Se esperaba el mensaje '" + expectedMessage + "' pero el portal mostró: " + errors);
    }

    private void verifyBalance(String account, BigDecimal expected, String description) {
        BigDecimal actual = service().awaitBalanceOf(account, expected);
        assertEquals(0, expected.compareTo(actual),
                "El saldo de la cuenta " + description + " (" + account + ") debía ser "
                        + expected + " y el servicio devolvió " + actual);
    }

    private ServicioBancario service() {
        return new ServicioBancario(playwright.apiClient());
    }
}
