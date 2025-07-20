package org.lenze.nupano.suite.stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.lenze.nupano.suite.annotations.AzureB2CAuthentication;

import java.util.List;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class organizations {
    @When("{actorOrgAPI} receives list of organizations authenticated through {string} of organization {string}")
    @AzureB2CAuthentication(type = "PKCE")
    public void receiveListOfOrganizations(Actor actorOrgApi, String user, String organization) {
        listOfOrganizations(actorOrgApi);

        actorOrgApi.should(
                seeThatResponse(actorOrgApi.getName()
                                .concat(" should see complete list of organizations"),
                        response -> response.statusCode(200)));
    }

    @When("{actorOrgAPI} sees access denied to receive list of organizations authenticated through {string} of organization {string}")
    @AzureB2CAuthentication(type = "PKCE")
    public void notreceiveListOfOrganizations(Actor actorOrgApi, String user, String organization) {
        listOfOrganizations(actorOrgApi);

        actorOrgApi.should(
                seeThatResponse(actorOrgApi.getName()
                                .concat(" should not see the list of organizations access denied message"),
                        response -> response.statusCode(403).body("type", is("access-denied"))));
    }

    @Given("{actorOrgAPI} chooses content with flavor as {string} to receive list of organizations")
    public void listorganizationsContent(Actor actorOrgApi, String content_type) {
        Serenity.environmentVariables().setProperty("content_type", "application/vnd.nupano.organizatons.organization+json;version=1;flavor=".concat(content_type));
    }

    @Given("{actorOrgAPI} looks for the name of the organizations from the list of organizations")
    public void readsListOfOrganizations(Actor actorOrgApi, List<String> expectedOrganizationNames) {
        for (String expectedOrganizationName : expectedOrganizationNames){
            actorOrgApi.should(
                    seeThatResponse(actorOrgApi.getName()
                                    .concat(" sees organization name ")
                                    .concat(expectedOrganizationName)
                                    .concat(" in the list of organization"),
                            response -> response.body("organizations.name", hasItem(expectedOrganizationName)))
            );
        }
    }

    private void listOfOrganizations(Actor actor) {
        SerenityRest.clear();
        SerenityRest.setUrlEncodingEnabled(false);

        actor.attemptsTo(Get.resource("/organizations")
                .with(request -> request.header("User-Agent", "Apache")
                        .auth().oauth2(Serenity.environmentVariables().getProperty("azureb2c_accesstoken"))
                        .contentType(Serenity.environmentVariables().getProperty("content_type"))
                        .urlEncodingEnabled(false)));
    }
}
