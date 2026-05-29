package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DrawingPackageBoundaryTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing"
    );

    @Test
    void drawingFacadeMustNotDependOnConcreteDiagramFamilies() throws IOException {
        List<String> forbiddenFragments = List.of(
                ".presentation.modulemap.",
                ".presentation.behavior.",
                ".presentation.architecture.",
                ".presentation.umlclass.",
                ".presentation.wireframe.",
                ".presentation.screenflow.",
                ".presentation.rolespermissions.",
                ".presentation.datadictionary.",
                ".presentation.canvas.",
                ".presentation.shell."
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
        assertTrue(violations.isEmpty(), "Drawing contaminado con familias concretas:\n" + String.join("\n", violations));
    }

    @Test
    void drawingFacadeMustNotIntroduceFormControlsInsideCanvas() throws IOException {
        List<String> forbiddenFragments = List.of(
                "new Button(",
                "new TextField(",
                "new ComboBox",
                "new TextArea(",
                "new TableView",
                "new ListView",
                "new ScrollPane"
        );
        List<String> violations = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(PACKAGE)) {
            for (Path file : stream.filter(path -> path.toString().endsWith(".java")).toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                for (String fragment : forbiddenFragments) {
                    if (source.contains(fragment)) {
                        violations.add(file + " usa control de formulario prohibido: " + fragment);
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(), "Drawing no debe crear formularios dentro del canvas:\n" + String.join("\n", violations));
    }

    @Test
    void appLightImportsDrawingBetweenSurfaceAndWorkbench() throws IOException {
        String source = Files.readString(Path.of("src/main/resources/css/app-light.css"), StandardCharsets.UTF_8);

        int surface = source.indexOf("diagram-surface.css");
        int drawing = source.indexOf("diagram-drawing.css");
        int workbench = source.indexOf("workbench.css");

        assertTrue(surface >= 0, "Debe existir import de diagram-surface.css.");
        assertTrue(drawing > surface, "diagram-drawing.css debe cargar después de diagram-surface.css.");
        assertTrue(workbench > drawing, "workbench.css debe cargar después de diagram-drawing.css.");
    }
}
