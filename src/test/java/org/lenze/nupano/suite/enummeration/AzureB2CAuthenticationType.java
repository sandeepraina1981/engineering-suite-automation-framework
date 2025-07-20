package org.lenze.nupano.suite.enummeration;

public enum AzureB2CAuthenticationType {
    PKCE("PKCE"),
    UI("UI");

    private final String type;

    AzureB2CAuthenticationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
