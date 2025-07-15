package org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce;

import com.microsoft.aad.msal4j.*;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class Azure {
    public void B2C() {
        String clientId = Serenity.environmentVariables().getProperty("azureb2c_clientId");
        String tenant = Serenity.environmentVariables().getProperty("azureb2c_tenant");
        String policy = Serenity.environmentVariables().getProperty("azureb2c_policy");
        String redirectUri = Serenity.environmentVariables().getProperty("azureb2c_redirectUri");
        String scope = Serenity.environmentVariables().getProperty("azureb2c_scope");

        AtomicReference<WebDriver> driver = new AtomicReference<>();

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
                System.out.println("Opening browser to: " + url);
                driver.set(new EdgeDriver());
                WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofMinutes(3));
                driver.get().get(url.toURI().toString());

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signInName")));
                driver.get().findElement(By.id("signInName")).sendKeys(Serenity.environmentVariables().getProperty("nupanosuite_user"));
                driver.get().findElement(By.id("continue")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
                driver.get().findElement(By.id("password")).sendKeys(Serenity.environmentVariables().getProperty("nupanosuite_password"));
                driver.get().findElement(By.cssSelector("button[type='submit']")).click();

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

        if (driver != null)
            driver.get().quit();
    }
}
