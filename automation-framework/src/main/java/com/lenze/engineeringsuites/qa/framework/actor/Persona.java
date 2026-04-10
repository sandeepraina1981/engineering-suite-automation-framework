package com.lenze.engineeringsuites.qa.framework.actor;

import lombok.Getter;

@Getter
public enum Persona {
    ALEX_ADMIN_UI_EDGE("Alex", UserLevel.ADMIN, BrowserType.MSEDGE),
    CHRIS_ADMIN_UI_CHROME("Chris", UserLevel.ADMIN, BrowserType.CHROME),
    BLAKE_USER_UI_EDGE("Blake", UserLevel.NORMAL_USER, BrowserType.MSEDGE),
    CASEY_USER_UI_CHROME("Casey", UserLevel.NORMAL_USER, BrowserType.CHROME),
    CHARLIE_ADMIN_API("Charlie", UserLevel.ADMIN, null),
    DANA_USER_API("Dana", UserLevel.NORMAL_USER, null);

    private final String realName;
    private final UserLevel userLevel;
    private final BrowserType preferredBrowser;

    Persona(String realName, UserLevel userLevel, BrowserType preferredBrowser) {
        this.realName = realName;
        this.userLevel = userLevel;
        this.preferredBrowser = preferredBrowser;
    }

    public static Persona fromRealName(String name) {
        for (Persona persona : Persona.values()) {
            if (persona.getRealName().equalsIgnoreCase(name)) {
                return persona;
            }
        }
        throw new IllegalArgumentException("No Persona found with real name: " + name);
    }
}
