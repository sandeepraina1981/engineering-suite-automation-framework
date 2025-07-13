package org.lenze.nupano.suite.helpers;

import net.serenitybdd.screenplay.actors.Cast;

public class SuiteCast extends SuiteAbilities {
    Cast castAPI(String serviceContext) {
        Cast castSuite = new Cast(apiAbility(serviceContext));
        castSuite.actorNamed(apiActor().getName(), apiAbility(serviceContext));

        return castSuite;
    }

    Cast castUI() {
        Cast castSuite = new Cast(uiAbility());
        castSuite.actorNamed(uiActor().getName(), uiAbility());

        return castSuite;
    }
}
