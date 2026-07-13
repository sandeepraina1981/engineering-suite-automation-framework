package util;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;


public class ExcelToFeatureGenerator {


    // Priority: lower number = higher priority in sorting
    private static final Map<String, Integer> TAG_PRIORITY = new HashMap<>();
    static {
        TAG_PRIORITY.put("smoke", 1);
        TAG_PRIORITY.put("p1", 2);
        TAG_PRIORITY.put("p2", 3);
        TAG_PRIORITY.put("p3", 4);
    }


    private static class ScenarioRow {
        String scenarioName;
        List<String> steps = new ArrayList<>();
        Set<String> tags = new LinkedHashSet<>();
        int priority = Integer.MAX_VALUE; // computed from tags

        String formattedTagLine() {
            if (tags.isEmpty()) return "";
            String joined = tags.stream()
                    .filter(t -> t != null && !t.isBlank())
                    .map(t -> t.startsWith("@") ? t : "@" + t.trim())
                    .collect(Collectors.joining(" "));
            return joined;
        }
    }


    public static void main(String[] args) throws Exception {

//        Path featureFolder = Paths.get(System.getProperty("user.dir")+"\\src\\test\\resources\\features");
//        Path parentFolder =  Paths.get(System.getProperty("user.dir")+"\\reports");
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(parentFolder)) {
//            for (Path p : stream) {
//                if (Files.isDirectory(p)) {
//                    deleteDirectory(p);   // use method from Option 1
//                }
//            }
//        }
//        if (Files.exists(featureFolder) && Files.isDirectory(featureFolder)) {
//            try (DirectoryStream<Path> stream = Files.newDirectoryStream(featureFolder)) {
//                for (Path file : stream) {
//                    if (Files.isRegularFile(file)) {
//                        Files.delete(file);
//                        System.out.println("Deleted file: " + file);
//                    }
//                }
//            }
//        } else {
//            System.out.println("Invalid directory: " + featureFolder);
//        }
//        if (args.length < 2) {
//            System.err.println("USAGE: java ExcelToFeatureGenerator <excelPath> <outputDir> [featurePrefix]");
//            System.exit(1);
//        }
        String web_ExcelPath = System.getProperty("user.dir") + "\\src\\test\\resources\\WEB_TestScenarios.xlsx"; //args[0];
        String webOutDir = System.getProperty("user.dir") + "\\src\\test\\resources\\features\\WebFeatures";//args[1];
        String api_ExcelPath = System.getProperty("user.dir") + "\\src\\test\\resources\\API_TestScenarios.xlsx"; //args[0];
        String apiOutDir = System.getProperty("user.dir") + "\\src\\test\\resources\\features\\ApiFeatures";//args[1];
        String featurePrefix = (args.length >= 3) ? args[2] : "";

        generateFeatures(web_ExcelPath, webOutDir, featurePrefix);
        generateFeatures(api_ExcelPath, apiOutDir, featurePrefix);
        System.out.println("Feature files generated successfully in: " + webOutDir);
    }


    public static void generateFeatures(String excelPath, String outputDir, String featurePrefix) throws IOException {
        Path out = Paths.get(outputDir);
        Files.createDirectories(out);

        try (InputStream is = Files.newInputStream(Paths.get(excelPath));
             Workbook wb = new XSSFWorkbook(is)) {

            for (int s = 0; s < wb.getNumberOfSheets(); s++) {
                Sheet sheet = wb.getSheetAt(s);
                if (sheet == null) continue;
                String sheetName = sheet.getSheetName();
                // Expect header row containing: Scenario Name, Step 1..Step N, Tags
                Row header = sheet.getRow(0);
                if (header == null) continue;
                int colScenario = findColumnIndex(header, "Scenario Name");
                int colTags = findColumnIndex(header, "Tags");
                List<Integer> stepColumns = findStepColumns(header);
                if (colScenario < 0 || stepColumns.isEmpty()) {
                    System.err.println("Skipping sheet '" + sheetName + "' due to missing columns.");
                    continue;
                }
                List<ScenarioRow> scenarios = new ArrayList<>();

                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;
                    String scenarioName = getString(row.getCell(colScenario));
                    if (scenarioName == null || scenarioName.isBlank()) continue;
                    ScenarioRow sr = new ScenarioRow();
                    // collect the scenario name
                    sr.scenarioName = scenarioName.trim();
                    // Steps - get all steps
                    for (int c : stepColumns) {
                        String step = getString(row.getCell(c));
                        if (step != null && !step.isBlank()) {
                            sr.steps.add(normalizeStep(step));
                        }
                    }

                    // Tags (may be "smoke" or "smoke, p1")
                    if (colTags >= 0) {
                        String tagCell = getString(row.getCell(colTags));
                        if (tagCell != null && !tagCell.isBlank()) {
                            for (String t : tagCell.split(",")) {
                                String cleaned = t.trim().toLowerCase(Locale.ROOT);
                                if (!cleaned.isBlank()) sr.tags.add(cleaned);
                            }
                        }
                    }

                    // Compute priority from tags (minimum priority wins)
                    sr.priority = sr.tags.stream()
                            .map(t -> TAG_PRIORITY.getOrDefault(t, Integer.MAX_VALUE))
                            .min(Integer::compareTo)
                            .orElse(Integer.MAX_VALUE);

                    // Default tag if none (optional): comment out if you don't want this
                    if (sr.tags.isEmpty()) {
                        sr.tags.add("p3"); // or leave empty
                        sr.priority = TAG_PRIORITY.get("p3");
                    }

                    scenarios.add(sr);
                }

                // Sort by priority, then stable by input order
                scenarios.sort(Comparator.comparingInt(a -> a.priority));

                // Write feature file for this sheet
                String fileName = (featurePrefix.isBlank() ? "" : featurePrefix + "_")
                        + safeFileName(sheetName) + ".feature";
                Path featurePath = out.resolve(fileName);
                writeFeature(featurePath, sheetName, scenarios);
            }
        }
    }


    private static int findColumnIndex(Row header, String expectedName) {
        for (int c = 0; c < header.getLastCellNum(); c++) {
            String name = getString(header.getCell(c));
            if (name != null && name.trim().equalsIgnoreCase(expectedName)) {
                return c;
            }
        }
        return -1;
    }


    private static List<Integer> findStepColumns(Row header) {
        List<Integer> cols = new ArrayList<>();
        for (int c = 0; c < header.getLastCellNum(); c++) {
            String name = getString(header.getCell(c));
            if (name != null && name.trim().toLowerCase(Locale.ROOT).startsWith("step")) {
                cols.add(c);
            }
        }
        return cols;
    }


    private static String getString(Cell cell) {
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        String val = cell.getStringCellValue();
        return (val == null) ? null : val.trim();
    }


    private static String normalizeStep(String raw) {
        // Trim and ensure the keyword starts properly (Given/When/Then/And/But)
        String s = raw.trim();
        // In case user wrote in lowercase or missing space
        // We'll trust the text is already a correct Gherkin step line.
        return s;
    }

    private static String safeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]+", "_");
    }

    private static void writeFeature(Path featurePath, String sheetName, List<ScenarioRow> scenarios) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature: ").append(sheetName).append("\n\n");

        for (ScenarioRow sr : scenarios) {
            String tagLine = sr.formattedTagLine();
            if (!tagLine.isBlank()) {
                sb.append(tagLine).append("\n");
            }
            sb.append("Scenario: ").append(sr.scenarioName).append("\n");
            for (String step : sr.steps) {
                sb.append("  ").append(step).append("\n");
            }
            sb.append("\n");
        }

        Files.createDirectories(featurePath.getParent());
        Files.write(featurePath, sb.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
