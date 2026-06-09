package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SimplePdfDocumentNavigationTest {

    @TempDir
    Path tempDir;

    @Test
    void writesInternalGoToAnnotationsForLinkedTableRows() throws Exception {
        Path target = tempDir.resolve("navegable.pdf");
        SimplePdfDocument pdf = new SimplePdfDocument("Prueba navegable");

        pdf.heading("Indice");
        pdf.linkedTable(List.of("Destino", "Descripcion"), List.of(
                PdfTableRow.linked(List.of("Resumen", "Ir al resumen"), "summary")
        ), 0.35, 0.65);
        pdf.heading("Resumen", "summary");
        pdf.paragraph("Contenido destino para probar navegacion interna.");

        pdf.writeTo(target);

        String rawPdf = Files.readString(target, StandardCharsets.ISO_8859_1);
        assertTrue(rawPdf.contains("/Annots [ "));
        assertTrue(rawPdf.contains("/Subtype /Link"));
        assertTrue(rawPdf.contains("/S /GoTo"));
        assertTrue(rawPdf.contains("/Border [0 0 0]"));
        assertTrue(Pattern.compile("/D \\[\\d+ 0 R /XYZ ").matcher(rawPdf).find());
    }

    @Test
    void rejectsLinkedRowsPointingToMissingDestinations() {
        Path target = tempDir.resolve("roto.pdf");
        SimplePdfDocument pdf = new SimplePdfDocument("Prueba rota");

        pdf.heading("Indice");
        pdf.linkedTable(List.of("Destino"), List.of(
                PdfTableRow.linked(List.of("No existe"), "missing-destination")
        ), 1.0);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> pdf.writeTo(target));
        assertTrue(exception.getMessage().contains("missing-destination"));
    }
}
