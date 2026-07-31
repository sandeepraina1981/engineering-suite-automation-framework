package org.lenze.nupano.suite.stepdefinitions.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.lenze.nupano.suite.annotations.AzureB2CAuthentication;
import org.lenze.nupano.suite.enummeration.AzureB2CAuthenticationType;

import java.util.List;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;

public class users {
    @When("{actorOrgAPI} receives list of users authenticated through {string} of users {string}")
    public void receiveListOfUsers(Actor actorOrgApi, String user, String organization) {
        listOfUsers(actorOrgApi);

        actorOrgApi.should(
                seeThatResponse(actorOrgApi.getName()
                                .concat(" should see complete list of users"),
                        response -> response.statusCode(200)));
    }

    @Given("{actorOrgAPI} chooses content with flavor as {string} to receive list of users")
    public void listusersContent(Actor actorOrgApi, String content_type) {
        Serenity.environmentVariables().setProperty("content_type", "application/vnd.nupano.users.user+json;version=1;flavor=".concat(content_type));
    }

    @Given("{actorOrgAPI} looks for the name of the users from the list of users")
    public void readsListOfUsers(Actor actorOrgApi, List<String> expectedUserNames) {
        for (String expectedUserName : expectedUserNames){
            actorOrgApi.should(
                    seeThatResponse(actorOrgApi.getName()
                                    .concat(" sees user name ")
                                    .concat(expectedUserName)
                                    .concat(" in the list of organization"),
                            response -> response.body("content.findAll { it.firstName != null }.firstName", hasItem(expectedUserName)))
            );
        }
    }

    @Then("{actorOrgAPI} verifies the user list fields from the list of users")
    public void readsUserList(Actor actorUserApi, List<String> expectedUserFields) {
        for (String expectedUserField : expectedUserFields){
            actorUserApi.should(
                    seeThatResponse("Verify field '"
                                    .concat(expectedUserField)
                                    .concat("' exists in user list"),
                            response -> response.body("content.findAll { it.containsKey('" + expectedUserField + "') }", not(empty())))
            );
        }
    }

    @And("{actorOrgAPI} confirms that the list of users is not empty")
    public void verifyUserListNotEmpty(Actor actorUserApi) {
        actorUserApi.should(
                seeThatResponse("Verify user list contains user names",
                        response -> response.body("content.size()", greaterThan(0)))
        );
    }

    @AzureB2CAuthentication(type = AzureB2CAuthenticationType.PKCE)
    public void listOfUsers(Actor actor) {
        SerenityRest.clear();
        SerenityRest.setUrlEncodingEnabled(false);

        actor.attemptsTo(Get.resource("/users")
                .with(request -> request.header("User-Agent", "Apache")
                        .auth().oauth2(Serenity.environmentVariables().getProperty("azureb2c_accesstoken"))
                        .urlEncodingEnabled(false)));
    }
}