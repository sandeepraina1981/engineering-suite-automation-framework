package org.lenze.nupano.suite.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.serenitybdd.core.Serenity;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

public class SuiteElementLoader {
    private JsonReader suiteRepositoryData;
    private Map<String, String> suitePageElements;
    private Type suiteRepositoryType = new TypeToken<Map<String, String>>() {}.getType();

    private String currentWorkingDirectory = System.getProperty("user.dir");
    private String currentTestRoot = Serenity.environmentVariables().getProperty("test_root");
    private String currentSuiteRepository = Serenity.environmentVariables().getProperty("suite_repoistory");

    public SuiteElementLoader() {
        this.load();
    }

    void load() {
        try {
            String repistoryPath = this.currentWorkingDirectory.concat(this.currentTestRoot).concat(this.currentSuiteRepository);
            this.suiteRepositoryData = new JsonReader(new FileReader(repistoryPath));
            this.suitePageElements = (new Gson()).fromJson(this.suiteRepositoryData, this.suiteRepositoryType);
        }
        catch(Exception ex) {
        }
    }

    public Map<String, String> getSuitePageElements() {
        return this.suitePageElements;
    }
}
