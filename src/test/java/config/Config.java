package config;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public final class Config {

    private static final String CONFIG_FILE = "/config.properties";

    // Snapshot
    private final Properties fileProps;
    private final Properties resolved;


    private Config() {
        this.fileProps = loadFileProps();
        this.resolved = new Properties();

        // Resolve once, per key (order: system -> env -> file -> default)
        set(ConfigKeys.BASE_URL,          "https://practicetestautomation.com/practice-test-login/");
        set(ConfigKeys.API_BASE_URL,      "https://rahulshettyacademy.com");
        set(ConfigKeys.BROWSER,           "chromium");
        set(ConfigKeys.HEADLESS,          "false");
        set(ConfigKeys.TRACE,             "off"); // off|on|retain-on-failure|on-first-retry
        set(ConfigKeys.SCREENSHOT_POLICY, "only-on-failure"); // off|on|only-on-failure
        set(ConfigKeys.DEFAULT_TIMEOUT,   "30000"); // ms
        set(ConfigKeys.TAG_EXPRESSION,    "@smoke");
        set(ConfigKeys.VIDEO,             "off"); // off|on|retain-on-failure
        set(ConfigKeys.SLOW_MO,           "0");   // ms
    }


    private void set(String key, String hardDefault) {
        // 1) -Dkey
        String sysKey =  switch (key) {
            case ConfigKeys.BASE_URL          -> ConfigKeys.ENV_BASE_URL;
            case ConfigKeys.BROWSER           -> ConfigKeys.ENV_BROWSER;
            case ConfigKeys.HEADLESS          -> ConfigKeys.ENV_HEADLESS;
            case ConfigKeys.TRACE             -> ConfigKeys.ENV_TRACE;
            case ConfigKeys.SCREENSHOT_POLICY -> ConfigKeys.ENV_SCREENSHOT_POLICY;
            case ConfigKeys.DEFAULT_TIMEOUT   -> ConfigKeys.ENV_DEFAULT_TIMEOUT;
            case ConfigKeys.TAG_EXPRESSION    -> ConfigKeys.ENV_TAG_EXPRESSION;
            case ConfigKeys.VIDEO             -> ConfigKeys.ENV_VIDEO;
            case ConfigKeys.SLOW_MO           -> ConfigKeys.ENV_SLOW_MO;
            default -> null;
        };
        if(sysKey!=null) {
            String sys = System.getProperty(sysKey);
            if (notBlank(sys)) {
                resolved.setProperty(key, sys.trim());
                return;
            }
        }

            // 2) ENV_VAR (upper snake)
        String envKey = switch (key) {
            case ConfigKeys.BASE_URL          -> ConfigKeys.ENV_BASE_URL;
            case ConfigKeys.BROWSER           -> ConfigKeys.ENV_BROWSER;
            case ConfigKeys.HEADLESS          -> ConfigKeys.ENV_HEADLESS;
            case ConfigKeys.TRACE             -> ConfigKeys.ENV_TRACE;
            case ConfigKeys.SCREENSHOT_POLICY -> ConfigKeys.ENV_SCREENSHOT_POLICY;
            case ConfigKeys.DEFAULT_TIMEOUT   -> ConfigKeys.ENV_DEFAULT_TIMEOUT;
            case ConfigKeys.TAG_EXPRESSION    -> ConfigKeys.ENV_TAG_EXPRESSION;
            case ConfigKeys.VIDEO             -> ConfigKeys.ENV_VIDEO;
            case ConfigKeys.SLOW_MO           -> ConfigKeys.ENV_SLOW_MO;
            default -> null;
        };
        if (envKey != null) {
            String env = System.getenv(envKey);
            if (notBlank(env)) { resolved.setProperty(key, env.trim()); return; }
        }

        // 3) config.properties
        String fromFile = fileProps.getProperty(key);
        if (notBlank(fromFile))
        { resolved.setProperty(key, fromFile.trim()); return; }

        // 4) Built-in default
        resolved.setProperty(key, hardDefault);
    }


    private static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }


    private Properties loadFileProps() {
        Properties p = new Properties();
        try (InputStream in = Config.class.getResourceAsStream(CONFIG_FILE)) {
            if (in != null) p.load(in);
        } catch (IOException ignored) {
        }
        return p;
    }


    // ---- Typed getters ----
    public String baseUrl()          { return resolved.getProperty(ConfigKeys.BASE_URL);    }
    public String apiBaseUrl()       { return resolved.getProperty(ConfigKeys.API_BASE_URL); }
    public String browser()          { return resolved.getProperty(ConfigKeys.BROWSER); }
    public boolean headless()        { return Boolean.parseBoolean(resolved.getProperty(ConfigKeys.HEADLESS)); }
    public String trace()            { return resolved.getProperty(ConfigKeys.TRACE); }
    public String screenshotPolicy() { return resolved.getProperty(ConfigKeys.SCREENSHOT_POLICY); }
    public long defaultTimeoutMs()   { return Long.parseLong(resolved.getProperty(ConfigKeys.DEFAULT_TIMEOUT)); }
    public String tagExpression()    { return resolved.getProperty(ConfigKeys.TAG_EXPRESSION); }
    public String video()            { return resolved.getProperty(ConfigKeys.VIDEO); }
    public int slowMoMs()            { return Integer.parseInt(resolved.getProperty(ConfigKeys.SLOW_MO)); }


    // ---- Singleton access ----
    private static class Holder {
        static final Config INSTANCE = new Config();
    }

    public static Config get() { return Holder.INSTANCE; }


    @Override public String toString() {
        return "Config{"
                + "baseUrl=" + baseUrl()
                + ", browser=" + browser()
                + ", headless=" + headless()
                + ", trace=" + trace()
                + ", screenshotPolicy=" + screenshotPolicy()
                + ", defaultTimeoutMs=" + defaultTimeoutMs()
                + ", tagExpression=" + tagExpression()
                + ", video=" + video()
                + ", slowMoMs=" + slowMoMs()
                + '}';
    }
}
