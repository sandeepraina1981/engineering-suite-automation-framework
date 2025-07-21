package org.lenze.nupano.suite.uitargets;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.targets.Target;

public interface LoginTargets extends Interaction {
    Target txt_username = Target.the("Enter username").locatedBy("#signInName");
    Target txt_password = Target.the("Enter password").locatedBy("#password");
    Target bttn_continue = Target.the("Press continue").locatedBy("#continue");
    Target bttn_next = Target.the("Press login").locatedBy("#next");

    @Step("{0} authenticates Azure B2C login page")
    <T extends Actor> void performAs(T actor);
}
