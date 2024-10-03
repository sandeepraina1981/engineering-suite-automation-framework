package org.lenze.nupano.suite.helpers;

import net.serenitybdd.screenplay.actors.Stage;

public class SuiteStage extends SuiteCast {
    public Stage stageAPI() { return new Stage(castAPI()); }
    public Stage stageUI() { return new Stage(castUI()); }
}
