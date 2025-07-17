package org.lenze.nupano.suite.helpers;

import net.serenitybdd.screenplay.actors.Stage;

public class SuiteStage extends SuiteCast {
    public Stage stageAPI(String serviceContext) { return new Stage(castAPI(serviceContext)); }
    public Stage stageUI() { return new Stage(castUI()); }
    public Stage stageAuth() { return new Stage(castAuth()); }
}
