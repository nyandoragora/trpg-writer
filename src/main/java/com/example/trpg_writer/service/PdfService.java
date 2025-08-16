package com.example.trpg_writer.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class PdfService {

    private static final String FONT_PATH = "static/fonts/ipaexm.ttf";

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
            // Use ClassPathResource to get a reliable path to the font file
            File fontFile = new ClassPathResource(FONT_PATH).getFile();
            renderer.getFontResolver().addFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            // Set base URL to resolve relative paths for CSS, images, etc.
            String baseUrl = new ClassPathResource("static/").getURL().toString();
            renderer.setDocumentFromString(htmlContent, baseUrl);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            // Wrap specific exceptions in a generic runtime exception
            throw new RuntimeException("Error during PDF generation", e);
        }
    }
}
