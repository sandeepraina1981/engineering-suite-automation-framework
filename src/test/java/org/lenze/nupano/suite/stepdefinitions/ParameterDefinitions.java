package org.lenze.nupano.suite.stepdefinitions;

import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import org.lenze.nupano.suite.helpers.SuiteStage;
import org.lenze.nupano.suite.properties.SuiteProperties;

public class ParameterDefinitions extends SuiteStage {
    @ParameterType(".*")
    public Actor actorAPI(String actorName) {
        SuiteProperties.activeStage = stageAPI();
        return SuiteProperties.activeStage.shineSpotlightOn(actorName);
    }

    @ParameterType(".*")
    public Actor actorUI(String actorName) {
        SuiteProperties.activeStage = stageUI();
        return SuiteProperties.activeStage.shineSpotlightOn(actorName);
    }
}