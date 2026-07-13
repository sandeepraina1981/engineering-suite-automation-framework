package resources;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathBuilder {

    private PathBuilder() {}


    public static String encodeSegment(String segment) {
        if (segment == null) throw new IllegalArgumentException("Path segment cannot be null");
        // Encode path segment, keep / for separators by encoding segments individually
        return URLEncoder.encode(segment, StandardCharsets.UTF_8)
                .replace("+", "%20"); // spaces should be %20 in paths
    }


    /** Joins after encoding each path segment (except prebuilt base path). */
    public static String joinEncoded(String basePath, String... segmentsToEncode) {

        List<String> all = new ArrayList<>();
        all.add(basePath.replaceAll("^/+", "").replaceAll("/+$", ""));

        for (String s : segmentsToEncode) {
            all.add(encodeSegment(s));
        }

        return all.stream()
                .filter(seg -> seg != null && !seg.isBlank())
                .map(seg -> seg.replaceAll("^/+", "").replaceAll("/+$", ""))
                .collect(Collectors.joining("/"));

    }

}
