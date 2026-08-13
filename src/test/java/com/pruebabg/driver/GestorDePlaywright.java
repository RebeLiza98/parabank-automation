package com.pruebabg.driver;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.pruebabg.config.Configuracion;

public class GestorDePlaywright {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private APIRequestContext apiClient;

    public Page page() {
        if (page == null) {
            browser = playwright().chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(Configuracion.headless())
                    .setSlowMo(Configuracion.slowMoMs()));
            browserContext = browser.newContext();
            browserContext.setDefaultTimeout(Configuracion.timeoutMs());
            page = browserContext.newPage();
        }
        return page;
    }

    public APIRequestContext apiClient() {
        if (apiClient == null) {
            apiClient = playwright().request().newContext(new APIRequest.NewContextOptions()
                    .setBaseURL(Configuracion.baseUrl()));
        }
        return apiClient;
    }

    public boolean browserStarted() {
        return page != null;
    }

    public void close() {
        if (apiClient != null) {
            apiClient.dispose();
            apiClient = null;
        }
        if (browserContext != null) {
            browserContext.close();
            browserContext = null;
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
        page = null;
    }

    private Playwright playwright() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        return playwright;
    }
}
