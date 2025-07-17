package org.lenze.nupano.suite.helpers;

import net.serenitybdd.screenplay.Actor;

public class SuiteActors {
    public Actor apiActor() { return new Actor("Riley Tester"); }
    public Actor uiActor() { return new Actor("Drew UI"); }
    public Actor authActor() { return new Actor("Toby"); }
    public Actor performanceActor() { return new Actor("Morgan Latency"); }
}
