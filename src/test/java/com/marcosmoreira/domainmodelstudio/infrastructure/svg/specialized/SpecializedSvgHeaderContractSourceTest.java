package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SpecializedSvgHeaderContractSourceTest {

    private static final Path DOCUMENT_WRITER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgDocumentWriter.java"
    );

    @Test
    void svgHeaderShouldUseSharedExportHeaderPolicy() throws IOException {
        String source = Files.readString(DOCUMENT_WRITER, StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramExportHeaderPolicy.forSpecializedSvg"),
                "El SVG especializado debe usar el mismo contrato textual de encabezado que PNG.");
        assertTrue(source.contains("id=\\\"export-header\\\""),
                "El encabezado SVG debe quedar identificado como grupo documental export-header.");
        assertTrue(source.contains("metadata.compactSubtitle()"),
                "El subtítulo del SVG debe combinar vista/tipo sin duplicar lógica local.");
    }

    @Test
    void svgHeaderPanelShouldBeStraightEvenIfOtherNotationShapesRemainSemantic() throws IOException {
        String source = Files.readString(DOCUMENT_WRITER, StandardCharsets.UTF_8);
        int headerIndex = source.indexOf("void appendHeader");
        assertTrue(headerIndex >= 0, "Debe existir un método de encabezado SVG.");
        String headerBlock = source.substring(headerIndex, Math.min(source.length(), headerIndex + 1700));

        assertTrue(headerBlock.contains("diagram-header-panel"),
                "El panel del encabezado debe tener clase propia.");
        assertTrue(!headerBlock.contains("rx=") && !headerBlock.contains("ry="),
                "El encabezado SVG no debe introducir rx/ry ornamentales; los radios restantes pertenecen a deuda visual posterior.");
    }
}
