package org.lenze.nupano.suite.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import org.lenze.nupano.suite.helper.SuiteStage;
import org.lenze.nupano.suite.helper.SuiteProperties;
import org.openqa.selenium.WebDriver;

public class ParameterDefinitions extends SuiteStage {
    @ParameterType(".*")
    public Actor actorOrgAPI(String actorName) {
        SuiteProperties.activeStage = stageAPI("orgs");
        return SuiteProperties.activeStage.shineSpotlightOn(actorName);
    }

    @ParameterType(".*")
    public Actor actorUI(String actorName) {
        SuiteProperties.activeStage = stageUI();
        return SuiteProperties.activeStage.shineSpotlightOn(actorName);
    }
}