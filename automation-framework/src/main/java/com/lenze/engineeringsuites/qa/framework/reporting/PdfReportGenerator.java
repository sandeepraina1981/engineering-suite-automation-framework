package com.lenze.engineeringsuites.qa.framework.reporting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PdfReportGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a Cucumber JSON report into a professional PDF artifact with audit log and screenshots.
     */
    public void generatePdfFromJson(File jsonFile, File pdfOutputFile) {
        try {
            JsonNode root = mapper.readTree(jsonFile);
            StringBuilder html = new StringBuilder();

            // Minimal HTML styling for a professional "Audit Log" look
            html.append("<html><head><style>")
                .append("body { font-family: Arial, sans-serif; margin: 40px; }")
                .append("h1 { color: #0056b3; border-bottom: 2px solid #0056b3; }")
                .append("h2 { color: #333; margin-top: 30px; }")
                .append(".scenario { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; border-radius: 5px; }")
                .append(".step { margin-left: 20px; padding: 5px; border-bottom: 1px solid #f0f0f0; }")
                .append(".PASSED { color: green; font-weight: bold; }")
                .append(".FAILED { color: red; font-weight: bold; }")
                .append(".screenshot { max-width: 100%; margin-top: 10px; border: 1px solid #ddd; }")
                .append("</style></head><body>")
                .append("<h1>Engineering Suite Test Execution Audit Log</h1>");

            for (JsonNode feature : root) {
                html.append("<h2>Feature: ").append(feature.get("name").asText()).append("</h2>");

                if (feature.has("elements")) {
                    for (JsonNode element : feature.get("elements")) {
                        html.append("<div class='scenario'>")
                            .append("<strong>Scenario: ").append(element.get("name").asText()).append("</strong>");

                        if (element.has("steps")) {
                            for (JsonNode step : element.get("steps")) {
                                String status = step.get("result").get("status").asText().toUpperCase();
                                html.append("<div class='step'>")
                                    .append("Step: ").append(step.get("name").asText())
                                    .append(" - <span class='").append(status).append("'>").append(status).append("</span>")
                                    .append("</div>");

                                // Embed screenshots if available
                                if (step.has("embeddings")) {
                                    for (JsonNode embedding : step.get("embeddings")) {
                                        if (embedding.get("mime_type").asText().contains("image")) {
                                            String base64Data = embedding.get("data").asText();
                                            html.append("<img class='screenshot' src='data:image/png;base64,")
                                                .append(base64Data).append("'/>");
                                        }
                                    }
                                }
                            }
                        }
                        html.append("</div>");
                    }
                }
            }
            html.append("</body></html>");

            // Build PDF from generated HTML
            try (OutputStream os = new FileOutputStream(pdfOutputFile)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(html.toString(), "/");
                builder.toStream(os);
                builder.run();
                System.out.println("Professional PDF Audit Log generated: " + pdfOutputFile.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF audit log from Cucumber JSON", e);
        }
    }
}
