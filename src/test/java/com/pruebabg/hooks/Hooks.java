package com.pruebabg.hooks;

import com.microsoft.playwright.Page;
import com.pruebabg.context.ContextoDelEscenario;
import com.pruebabg.driver.GestorDePlaywright;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private final GestorDePlaywright playwright;
    private final ContextoDelEscenario context;

    public Hooks(GestorDePlaywright playwright, ContextoDelEscenario context) {
        this.playwright = playwright;
        this.context = context;
    }

    @Before
    public void publishScenario(Scenario scenario) {
        context.saveScenario(scenario);
    }

    @After(order = 100)
    public void attachEvidenceOnFailure(Scenario scenario) {
        if (scenario.isFailed() && playwright.browserStarted()) {
            byte[] screenshot = playwright.page()
                    .screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", "Evidencia del fallo");
        }
    }

    @After(order = 1)
    public void releaseResources() {
        playwright.close();
    }
}
