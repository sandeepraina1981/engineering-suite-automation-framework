package org.lenze.nupano.suite.uitargets;

import net.serenitybdd.screenplay.targets.Target;

public interface LoginTargets {
    Target txt_username = Target.the("Enter username").locatedBy("#signInName");
    Target txt_password = Target.the("Enter password").locatedBy("#password");
    Target bttn_continue = Target.the("Press continue").locatedBy("#continue");
    Target bttn_next = Target.the("Press login").locatedBy("#next");
}
