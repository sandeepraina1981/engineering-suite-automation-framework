package com.lenze.engineeringsuites.qa.framework.reporting;

import java.io.File;

/**
 * CLI entry point for PDF report generation in CI/CD.
 */
public class ReportGeneratorMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -cp ... ReportGeneratorMain <json_path> <pdf_output_path>");
            return;
        }

        File jsonFile = new File(args[0]);
        File outputFile = new File(args[1]);

        PdfReportGenerator generator = new PdfReportGenerator();
        generator.generatePdfFromJson(jsonFile, outputFile);
    }
}
