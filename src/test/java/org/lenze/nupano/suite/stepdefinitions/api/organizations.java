package org.lenze.nupano.suite.stepdefinitions.api;

import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.lenze.nupano.suite.annotations.AzureB2CPKCELogin;
import org.lenze.nupano.suite.authentiction.oauth2.authorizationcode.pkce.Azure;

public class organizations {
    @AzureB2CPKCELogin(value = "get token")
    @When("{actorOrgAPI} read list of organizations")
    public void listusers(Actor actorOrgApi) {
        new Azure().B2C();
        SerenityRest.clear();
        SerenityRest.setUrlEncodingEnabled(false);

        actorOrgApi.attemptsTo(Get.resource("/organizations")
                .with(request -> request.header("User-Agent", "Apache")
                        .auth().oauth2(Serenity.environmentVariables().getProperty("azureb2c_accesstoken"))
                        .contentType("application/vnd.nupano.organizations.organization+json;version=1;flavor=magenta")
                        .urlEncodingEnabled(false)));
    }
}
