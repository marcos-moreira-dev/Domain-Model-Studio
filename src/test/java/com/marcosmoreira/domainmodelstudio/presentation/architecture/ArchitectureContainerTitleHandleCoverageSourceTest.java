package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 51: los contenedores se arrastran por una banda de título completa, no por el relleno transparente. */
class ArchitectureContainerTitleHandleCoverageSourceTest {

    @Test
    void transparentContainerShouldKeepOnlyTitleHandleAsDragTarget() throws Exception {
        String renderKit = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java");
        String coordinator = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java");

        assertTrue(renderKit.contains("interactive-canvas-handle-only-node"),
                "El grupo del contenedor debe avisar que solo su handle captura drag.");
        assertTrue(coordinator.contains("rendered.setPickOnBounds(!handleOnlyDrag(rendered))"),
                "El coordinador no debe volver a hacer draggable todo el rectángulo transparente.");
    }

    @Test
    void titleHandleShouldCoverWrappedTitleAndDetail() throws Exception {
        String renderKit = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java");

        assertTrue(renderKit.contains("titleHandleHeight(titleText, detailText, handleTextWidth)"));
        assertTrue(renderKit.contains("title.setWrapText(true)"));
        assertTrue(renderKit.contains("detail.setWrapText(true)"));
        assertTrue(renderKit.contains("title.setPrefWidth(handleTextWidth)"));
        assertTrue(renderKit.contains("detail.setPrefWidth(handleTextWidth)"));
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
