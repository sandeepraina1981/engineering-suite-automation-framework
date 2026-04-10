package com.lenze.engineeringsuites.qa.framework.actor;

public enum BrowserType {
    MSEDGE("msedge"),
    CHROME("chrome");

    private final String channel;

    BrowserType(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
