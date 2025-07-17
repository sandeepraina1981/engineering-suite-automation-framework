package org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce;

import com.microsoft.aad.msal4j.*;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.junit.jupiter.api.DisplayName;
import org.lenze.nupano.suite.properties.SuiteProperties;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Azure {
    @DisplayName("Perform SSO login using Microsoft Azure B2C")
    public void B2C() {
        String clientId = Serenity.environmentVariables().getProperty("azureb2c_clientId");
        String tenant = Serenity.environmentVariables().getProperty("azureb2c_tenant");
        String policy = Serenity.environmentVariables().getProperty("azureb2c_policy");
        String redirectUri = Serenity.environmentVariables().getProperty("azureb2c_redirectUri");
        String scope = Serenity.environmentVariables().getProperty("azureb2c_scope");

        AtomicBoolean closeWebDriver = new AtomicBoolean(false);

        String authority = String.format(
                "https://%s.b2clogin.com/%s.onmicrosoft.com/%s",
                tenant, tenant, policy
        );

        Set<String> scopes = Collections.singleton(scope);

        PublicClientApplication pca = null;
        try {
            pca = PublicClientApplication.builder(clientId)
                    .b2cAuthority(authority)
                    .build();
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }

        OpenBrowserAction browserAction = url -> {
            try {
                Serenity.environmentVariables().setProperty("nupanosuite_url", String.valueOf(url.toURI()));
                closeWebDriver.set(new org.lenze.nupano.suite.authentiction.ui.Azure().SignIn());

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        InteractiveRequestParameters parameters = null;
        try {
            parameters = InteractiveRequestParameters.builder(new URI(redirectUri))
                    .scopes(scopes)
                    .prompt(Prompt.LOGIN)
                    .systemBrowserOptions(SystemBrowserOptions.builder().openBrowserAction(browserAction).build())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        CompletableFuture<IAuthenticationResult> future = pca.acquireToken(parameters);
        IAuthenticationResult result = null;
        try {
            result = future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        }

        Serenity.environmentVariables().setProperty("azureb2c_accesstoken", result.accessToken());
        Serenity.environmentVariables().setProperty("azureb2c_tokenID", result.idToken());

        if (closeWebDriver.get())
            Serenity.getWebdriverManager().closeAllDrivers();
    }
}
