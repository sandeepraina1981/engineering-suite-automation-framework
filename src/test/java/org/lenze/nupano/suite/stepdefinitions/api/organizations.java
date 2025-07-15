package org.lenze.nupano.suite.stepdefinitions.api;

import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.lenze.nupano.suite.annotations.AzureB2CAuthentication;

public class organizations {
    @When("{actorOrgAPI} read list of organizations authenticated through {string} of organization {string}")
    @AzureB2CAuthentication(type = "PKCE")
    public void listorganizations(Actor actorOrgApi, String user, String organization) {
        SerenityRest.clear();
        SerenityRest.setUrlEncodingEnabled(false);

        actorOrgApi.attemptsTo(Get.resource("/organizations")
                .with(request -> request.header("User-Agent", "Apache")
                        .auth().oauth2(Serenity.environmentVariables().getProperty("azureb2c_accesstoken"))
                        .contentType("application/vnd.nupano.organizations.organization+json;version=1;flavor=magenta")
                        .urlEncodingEnabled(false)));
    }
}
