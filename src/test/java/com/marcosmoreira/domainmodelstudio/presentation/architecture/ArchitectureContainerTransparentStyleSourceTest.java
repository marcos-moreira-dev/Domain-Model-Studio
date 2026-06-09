package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ArchitectureContainerTransparentStyleSourceTest {

    @Test
    void architectureContainerNodesAreEmptyFramesInCanvasCss() throws Exception {
        String css = Files.readString(Path.of("src/main/resources/css/architecture-diagram.css"));
        String primitives = Files.readString(Path.of("src/main/resources/css/diagram-academic-primitives-shapes.css"));

        assertTrue(css.contains(".architecture-canvas-zone {\n    -fx-fill: transparent;"));
        assertTrue(css.contains(".architecture-canvas-zone-environment {\n    -fx-fill: transparent;"));
        assertTrue(css.contains(".architecture-canvas-zone-network {\n    -fx-fill: transparent;"));
        assertTrue(primitives.contains(".architecture-canvas-zone,")
                && primitives.contains("-fx-fill: transparent;"));
    }

    @Test
    void specializedSvgKeepsArchitectureContainersTransparent() throws Exception {
        String writer = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgDocumentWriter.java"));

        assertTrue(writer.contains(".node-architecture-boundary rect.body, .node-architecture-environment rect.body, .node-architecture-network rect.body { fill: none; fill-opacity: 0;"));
        assertTrue(writer.contains(".node-architecture-boundary rect.header, .node-architecture-environment rect.header, .node-architecture-network rect.header { fill: none; fill-opacity: 0;"));
    }
}
