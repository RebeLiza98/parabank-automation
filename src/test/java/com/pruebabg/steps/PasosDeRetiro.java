package com.pruebabg.steps;

import com.microsoft.playwright.APIResponse;
import com.pruebabg.api.ServicioBancario;
import com.pruebabg.context.ContextoDelEscenario;
import com.pruebabg.driver.GestorDePlaywright;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasosDeRetiro {

    private final GestorDePlaywright playwright;
    private final ContextoDelEscenario context;

    public PasosDeRetiro(GestorDePlaywright playwright, ContextoDelEscenario context) {
        this.playwright = playwright;
        this.context = context;
    }

    @When("solicito un retiro de {string} dólares sobre mi cuenta")
    public void iRequestAWithdrawalFromMyAccount(String amount) {
        ServicioBancario service = service();
        context.saveSourceBalanceBefore(service.balanceOf(context.sourceAccount()));
        context.log("Saldo antes del retiro: " + context.sourceBalanceBefore());
        context.saveApiResponse(service.withdraw(context.sourceAccount(), amount));
    }

    @When("solicito un retiro de {string} dólares sobre la cuenta {string}")
    public void iRequestAWithdrawalFromAccount(String amount, String account) {
        context.saveApiResponse(service().withdraw(account, amount));
    }

    @Then("el servicio confirma el retiro")
    public void theServiceConfirmsTheWithdrawal() {
        APIResponse response = context.lastApiResponse();
        String body = response.text();
        context.log("Respuesta del servicio [" + response.status() + "]: " + body);

        assertEquals(200, response.status(),
                "El servicio no respondió correctamente. Cuerpo: " + body);
        assertTrue(body.contains("Successfully withdrew"),
                "El servicio no confirmó el retiro. Cuerpo: " + body);
    }

    @And("el saldo de mi cuenta disminuye en {string}")
    public void myAccountBalanceDecreasesBy(String amount) {
        BigDecimal expected = context.sourceBalanceBefore().subtract(new BigDecimal(amount));
        BigDecimal actual = service().awaitBalanceOf(context.sourceAccount(), expected);
        context.log("Saldo después del retiro: " + actual);
        assertEquals(0, expected.compareTo(actual),
                "El saldo de la cuenta " + context.sourceAccount() + " debía ser "
                        + expected + " y el servicio devolvió " + actual);
    }

    @Then("el servicio responde con el código {int} y el mensaje {string}")
    public void theServiceRespondsWithStatusAndMessage(int expectedStatus, String expectedMessage) {
        APIResponse response = context.lastApiResponse();
        String body = response.text();
        context.log("Respuesta del servicio [" + response.status() + "]: " + body);

        assertEquals(expectedStatus, response.status(),
                "Código HTTP inesperado. Cuerpo: " + body);
        assertTrue(body.contains(expectedMessage),
                "Se esperaba el mensaje '" + expectedMessage + "' y el servicio devolvió: " + body);
    }

    private ServicioBancario service() {
        return new ServicioBancario(playwright.apiClient());
    }
}
