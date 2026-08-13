package com.pruebabg.context;

import com.microsoft.playwright.APIResponse;
import com.pruebabg.model.Usuario;
import io.cucumber.java.Scenario;

import java.math.BigDecimal;

public class ContextoDelEscenario {

    private Usuario user;
    private String sourceAccount;
    private String targetAccount;
    private BigDecimal sourceBalanceBefore;
    private BigDecimal targetBalanceBefore;
    private APIResponse lastApiResponse;
    private Scenario scenario;

    public Usuario user() {
        if (user == null) {
            throw new IllegalStateException(
                    "No hay un usuario en el contexto: falta ejecutar el paso de registro.");
        }
        return user;
    }

    public void saveUser(Usuario user) {
        this.user = user;
    }

    public String sourceAccount() {
        return require(sourceAccount, "cuenta origen");
    }

    public void saveSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String targetAccount() {
        return require(targetAccount, "cuenta destino");
    }

    public void saveTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public BigDecimal sourceBalanceBefore() {
        return sourceBalanceBefore;
    }

    public void saveSourceBalanceBefore(BigDecimal balance) {
        this.sourceBalanceBefore = balance;
    }

    public BigDecimal targetBalanceBefore() {
        return targetBalanceBefore;
    }

    public void saveTargetBalanceBefore(BigDecimal balance) {
        this.targetBalanceBefore = balance;
    }

    public APIResponse lastApiResponse() {
        if (lastApiResponse == null) {
            throw new IllegalStateException("No se ha ejecutado ninguna llamada al servicio REST.");
        }
        return lastApiResponse;
    }

    public void saveApiResponse(APIResponse response) {
        this.lastApiResponse = response;
    }

    public void saveScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public void log(String message) {
        if (scenario != null) {
            scenario.log(message);
        }
    }

    private static String require(String value, String description) {
        if (value == null) {
            throw new IllegalStateException("No hay " + description + " en el contexto del escenario.");
        }
        return value;
    }
}
