package org.lenze.nupano.suite.stepdefinitions.api;

import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class DemoDefinitions {
    @When("{actorAPI} opens url")
    public void OpenUrl(Actor actorApi) {
        SerenityRest.clear();
        SerenityRest.setUrlEncodingEnabled(false);

        System.out.println(Serenity.environmentVariables().getProperty("webdriver.base.url"));

        actorApi.attemptsTo(Get.resource("/users/1")
                .with(request -> request.header("User-Agent", "Apache")
                        .urlEncodingEnabled(false)));
    }

    @When("{actorAPI} sees url opens successfully")
    public void VerifiesUrl(Actor actorApi) {
        actorApi.should(seeThatResponse(Serenity.environmentVariables().getProperty("suite_url") + " opens successfully", response -> response.statusCode(200)));
    }
}
