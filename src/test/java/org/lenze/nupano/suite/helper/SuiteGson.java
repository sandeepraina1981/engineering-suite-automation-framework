package org.lenze.nupano.suite.helper;

import com.google.gson.Gson;
import java.util.List;

public class SuiteGson {
    private static final Gson gson = new Gson();

    public static String listToJsonArray(List<String> list) {
        return gson.toJson(list);
    }

}
