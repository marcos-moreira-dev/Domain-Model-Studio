package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasPngExporterHeaderSourceTest {

    private static final Path EXPORTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java"
    );

    @Test
    void pngExporterShouldReserveHeaderSpaceBeforeDiagramContent() throws IOException {
        String source = Files.readString(EXPORTER, StandardCharsets.UTF_8);

        assertTrue(source.contains("DiagramExportHeaderPolicy.PNG_HEADER_HEIGHT"),
                "El PNG común debe reservar altura de encabezado con la política transversal.");
        assertTrue(source.contains("diagram.setTranslateY(headerHeight - exportSurface.exportY())"),
                "El diagrama debe desplazarse bajo el encabezado para evitar solaparse.");
        assertTrue(source.contains("exportHeader(headerMetadata, exportWidth)"),
                "El PNG debe dibujar un encabezado textual antes del contenido visual.");
        assertTrue(source.contains("headerMetadataSupplier"),
                "Los workspaces visuales deben poder aportar metadatos de proyecto al exportador PNG.");
    }

    @Test
    void pngHeaderShouldUseStraightPanelWithoutOrnamentalRadius() throws IOException {
        String source = Files.readString(EXPORTER, StandardCharsets.UTF_8);
        int headerIndex = source.indexOf("private Group exportHeader");
        assertTrue(headerIndex >= 0, "Debe existir un método dedicado a dibujar el encabezado PNG.");
        String headerBlock = source.substring(headerIndex, Math.min(source.length(), headerIndex + 1600));

        assertTrue(headerBlock.contains("new Rectangle(0.0, 0.0, width, height)"),
                "El encabezado PNG debe ser un panel rectangular explícito.");
        assertTrue(!headerBlock.contains("setArcWidth") && !headerBlock.contains("setArcHeight"),
                "El encabezado PNG no debe introducir bordes redondeados ornamentales.");
    }
}
