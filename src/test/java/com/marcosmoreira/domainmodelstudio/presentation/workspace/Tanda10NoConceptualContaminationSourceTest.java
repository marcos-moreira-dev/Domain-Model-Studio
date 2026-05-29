package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Frontera fuerte: el modelo conceptual queda congelado y no contamina workbenches nuevos. */
class Tanda10NoConceptualContaminationSourceTest {

    private static final Path PRESENTATION = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation");
    private static final Pattern FORBIDDEN_CONCEPTUAL_CLASS = Pattern.compile(
            "\\b(ConceptualCanvasAdapter|DiagramCanvasView|ModelTreeView|InspectorView)\\b");
    private static final List<String> ACTIVE_PACKAGES = List.of(
            "workbench",
            "sidedock",
            "interactivecanvas",
            "diagramcanvas",
            "modulemap",
            "umlclass",
            "screenflow",
            "wireframe",
            "architecture",
            "behavior",
            "freegraph"
    );

    @Test
    void activeWorkspacesMustNotImportConceptualCanvasSidebarOrInspector() throws IOException {
        for (String packageName : ACTIVE_PACKAGES) {
            Path packageDir = PRESENTATION.resolve(packageName);
            assertTrue(Files.isDirectory(packageDir), packageName + " debe existir como paquete de presentación activo.");
            try (Stream<Path> files = Files.walk(packageDir)) {
                for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                    String source = Files.readString(file, StandardCharsets.UTF_8);
                    assertFalse(source.contains("presentation.canvas."), file + " no debe importar el canvas conceptual.");
                    assertFalse(source.contains("presentation.sidebar."), file + " no debe importar el sidebar conceptual.");
                    assertFalse(source.contains("presentation.inspector."), file + " no debe importar el inspector conceptual.");
                    assertFalse(FORBIDDEN_CONCEPTUAL_CLASS.matcher(source).find(),
                            file + " no debe referenciar clases del modelo conceptual congelado.");
                }
            }
        }
    }
}
