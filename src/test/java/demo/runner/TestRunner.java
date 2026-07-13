package demo.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.*;
import static io.cucumber.core.options.Constants.FILTER_TAGS_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/ApiFeatures")
@SelectClasspathResource("features/WebFeatures")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "demo.steps,demo.hooks")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@smoke or @p1 or @p2 or @p3 or @api")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, summary, "
                + "html:target/cucumber-report.html, "
                + "json:target/cucumber.json, "
                + "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:,"
                + "demo.reporting.StepStatusLoggerPlugin,"
                + "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm,"
                + "json:target/cucumber-reports/cucumber.json")
public class TestRunner {


}

