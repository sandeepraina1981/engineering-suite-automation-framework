package com.lenze.engineeringsuites.qa.framework.api;

import com.lenze.engineeringsuites.qa.framework.actor.UserLevel;

/**
 * Enterprise Utility to provide credentials from Secure Environment Variables
 * or a central configuration, avoiding hardcoded strings.
 */
public class CredentialsProvider {

    public static String getUsernameFor(UserLevel level) {
        String envVar = (level == UserLevel.ADMIN) ? "ADMIN_USER" : "NORMAL_USER";
        return System.getenv().getOrDefault(envVar, level.name().toLowerCase() + "@lenze.com");
    }

    public static String getPasswordFor(UserLevel level) {
        String envVar = (level == UserLevel.ADMIN) ? "ADMIN_PASS" : "NORMAL_PASS";
        return System.getenv().getOrDefault(envVar, "DefaultPass123!");
    }
}
