package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DiagramSurfacePackageBoundaryTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas"
    );

    @Test
    void canonicalSurfaceMustNotDependOnConcreteDiagramFamilies() throws IOException {
        List<String> forbiddenFragments = List.of(
                ".presentation.modulemap.",
                ".presentation.behavior.",
                ".presentation.architecture.",
                ".presentation.umlclass.",
                ".presentation.wireframe.",
                ".presentation.screenflow.",
                ".presentation.rolespermissions.",
                ".presentation.datadictionary.",
                ".presentation.canvas."
        );
        List<String> violations = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(PACKAGE)) {
            for (Path file : stream.filter(path -> path.toString().endsWith(".java")).toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                for (String fragment : forbiddenFragments) {
                    if (source.contains(fragment)) {
                        violations.add(file + " importa o menciona " + fragment);
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(), "Superficie canónica contaminada:\n" + String.join("\n", violations));
    }

    @Test
    void appLightMustImportSurfaceBetweenInteractiveCanvasAndWorkbench() throws IOException {
        String source = Files.readString(Path.of("src/main/resources/css/app-light.css"), StandardCharsets.UTF_8);

        int interactiveCanvas = source.indexOf("interactive-canvas.css");
        int surface = source.indexOf("diagram-surface.css");
        int workbench = source.indexOf("workbench.css");

        assertTrue(interactiveCanvas >= 0, "Debe existir import de interactive-canvas.css.");
        assertTrue(surface > interactiveCanvas, "diagram-surface.css debe cargar después de interactive-canvas.css.");
        assertTrue(workbench > surface, "workbench.css debe cargar después de diagram-surface.css.");
    }
}
