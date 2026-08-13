package com.pruebabg.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

public class ServicioBancario {

    private static final String WITHDRAW = "/parabank/services/bank/withdraw";
    private static final String ACCOUNTS = "/parabank/services/bank/accounts/";

    private static final Duration MAX_WAIT = Duration.ofSeconds(15);
    private static final long POLL_INTERVAL_MS = 500;

    private final APIRequestContext client;

    public ServicioBancario(APIRequestContext client) {
        this.client = client;
    }

    public APIResponse withdraw(String account, String amount) {
        return client.post(WITHDRAW, RequestOptions.create()
                .setQueryParam("accountId", account)
                .setQueryParam("amount", amount));
    }

    public BigDecimal balanceOf(String account) {
        APIResponse response = getAccount(account);
        if (!response.ok()) {
            throw new IllegalStateException("No se pudo consultar la cuenta " + account
                    + ". Status: " + response.status() + ". Respuesta: " + response.text());
        }
        JsonObject accountJson = JsonParser.parseString(response.text()).getAsJsonObject();
        return accountJson.get("balance").getAsBigDecimal();
    }

    public APIResponse getAccount(String account) {
        return client.get(ACCOUNTS + account,
                RequestOptions.create().setHeader("Accept", "application/json"));
    }

    public BigDecimal awaitBalanceOf(String account, BigDecimal expected) {
        Instant deadline = Instant.now().plus(MAX_WAIT);
        BigDecimal balance = balanceOf(account);
        while (balance.compareTo(expected) != 0 && Instant.now().isBefore(deadline)) {
            pause();
            balance = balanceOf(account);
        }
        return balance;
    }

    private static void pause() {
        try {
            Thread.sleep(POLL_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Espera de saldo interrumpida", e);
        }
    }
}
