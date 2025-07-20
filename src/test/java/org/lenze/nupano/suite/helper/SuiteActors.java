package org.lenze.nupano.suite.helper;

import net.serenitybdd.screenplay.Actor;

public class SuiteActors {
    public Actor apiActor() { return new Actor("Riley"); }
    public Actor uiActor() { return new Actor("Derek"); }
    public Actor performanceActor() { return new Actor("Ben"); }
}