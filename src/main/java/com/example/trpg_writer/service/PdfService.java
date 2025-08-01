package com.example.trpg_writer.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

@Service
public class PdfService {

    private static final String FONT_PATH = "src/main/resources/static/fonts/HannariMincho-Regular.otf";

    /**
     * Generates a PDF from the given HTML content.
     *
     * @param htmlContent The HTML content processed by Thymeleaf.
     * @return A byte array representing the generated PDF.
     * @throws RuntimeException If there is an error during PDF generation.
     */
    public byte[] generatePdfFromHtml(String htmlContent) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            // Add font for Japanese characters
            renderer.getFontResolver().addFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            // Wrap specific exceptions in a generic runtime exception
            throw new RuntimeException("Error during PDF generation", e);
        }
    }
}
