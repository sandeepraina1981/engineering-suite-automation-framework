package org.lenze.nupano.suite;

import io.cucumber.java.BeforeAll;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("/features")
public class LenzeSuiteTestExecutor {
//    @BeforeAll
//    public static void LoadExecutor() {
//        SuiteElementLoader suiteElementLoader = new SuiteElementLoader();
//        SuiteProperties.suitePageElements = suiteElementLoader.getSuitePageElements();
//    }
}