package config;

public class ConfigKeys {


    private ConfigKeys() {}

    // public key constants to avoid stringly-typed lookups
    public static final String BASE_URL          = "baseUrl";
    public static final String API_BASE_URL      = "apiBaseUrl";
    public static final String BROWSER           = "browser";
    public static final String HEADLESS          = "headless";
    public static final String TRACE             = "trace";
    public static final String SCREENSHOT_POLICY = "screenshotPolicy";
    public static final String DEFAULT_TIMEOUT   = "defaultTimeout";
    public static final String TAG_EXPRESSION    = "tagExpression";
    public static final String VIDEO             = "video";
    public static final String SLOW_MO           = "slowMo";

    // Matching environment variable names (upper snake)
    public static final String ENV_BASE_URL          = "BASE_URL";
    public static final String ENV_BROWSER           = "BROWSER";
    public static final String ENV_HEADLESS          = "HEADLESS";
    public static final String ENV_TRACE             = "TRACE";
    public static final String ENV_SCREENSHOT_POLICY = "SCREENSHOT_POLICY";
    public static final String ENV_DEFAULT_TIMEOUT   = "DEFAULT_TIMEOUT";
    public static final String ENV_TAG_EXPRESSION    = "TAG_EXPRESSION";
    public static final String ENV_VIDEO             = "VIDEO";
    public static final String ENV_SLOW_MO           = "SLOW_MO";

}
