package demo.locatorOperation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;


public class LocatorReader {

    String pageName;

    public LocatorReader(String pageName) {
        this.pageName = pageName;
    }

    private List<LocatorData> cache;

    /**
     * Fetches a locator value from the JSON file based on the given key.
     * Loads and parses the JSON, searches for the key, and returns its value.
     * Throws an exception if the key is not found or JSON is invalid.
     */
    public String getLocatorValue(String id) {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\objectFactory\\" + pageName + ".json";

        try {
            if (cache == null) {
                ObjectMapper mapper = new ObjectMapper();
                cache = mapper.readValue(
                        new File(filePath),
                        new TypeReference<List<LocatorData>>() {
                        }
                );
            }
            return cache.stream()
                    .filter(x -> x.getId().equalsIgnoreCase(id))
                    .map(LocatorData::getValue)
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON", e);
        }

    }
}
