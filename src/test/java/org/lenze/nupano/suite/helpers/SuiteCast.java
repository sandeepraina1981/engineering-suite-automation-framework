package org.lenze.nupano.suite.helpers;

import net.serenitybdd.screenplay.actors.Cast;

public class SuiteCast extends SuiteAbilities {
    Cast castAPI() {
        Cast castSuite = new Cast(apiAbility());
        castSuite.actorNamed(apiActor().getName(), apiAbility());

        return castSuite;
    }

    Cast castUI() {
        Cast castSuite = new Cast(uiAbility());
        castSuite.actorNamed(uiActor().getName(), uiAbility());

        return castSuite;
    }
}
