package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InteractiveCanvasDragPreviewSourceTest {

    private static final Path REGISTRY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeVisualRegistry.java"
    );
    private static final Path PREVIEW_PORT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasDragPreviewPort.java"
    );
    private static final Path UML_ADAPTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"
    );

    @Test
    void canvasShouldAllowSpecializedGroupDragPreview() throws IOException {
        String registry = Files.readString(REGISTRY, StandardCharsets.UTF_8);
        String port = Files.readString(PREVIEW_PORT, StandardCharsets.UTF_8);

        assertTrue(port.contains("previewNodeIdsForDraggedNode"),
                "El canvas debe tener un puerto explícito para previsualizar nodos que se mueven juntos.");
        assertTrue(registry.contains("CanvasDragPreviewPort"),
                "El registro visual debe consultar el puerto de previsualización cuando el adapter lo implemente.");
        assertTrue(registry.contains("renderedNodeById"),
                "La previsualización por grupo requiere conocer los nodos JavaFX renderizados por id.");
    }

    @Test
    void umlModulesShouldPreviewTheirVisibleChildrenWhenDragged() throws IOException {
        String adapter = Files.readString(UML_ADAPTER, StandardCharsets.UTF_8);

        assertTrue(adapter.contains("implements InteractiveCanvasAdapter") && adapter.contains("CanvasDragPreviewPort"),
                "El adapter UML debe declarar que sabe previsualizar arrastres agrupados.");
        assertTrue(adapter.contains("addClassIdsForModule"),
                "Al arrastrar un módulo UML, sus clases visibles deben acompañarlo en la previsualización.");
        assertTrue(adapter.contains("moveModuleAndVisibleClassesBy"),
                "Al soltar un módulo UML, sus clases visibles también deben moverse de forma persistente.");
    }
}
