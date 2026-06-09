package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl UX-1B/Tanda 37: no-radius por alcance sin tocar geometría semántica. */
class CssNoRadiusScopedSourceTest {

    private static final Path CSS = Path.of("src", "main", "resources", "css");

    private static final List<String> SCOPED_CSS = List.of(
            "administrative-editors.css",
            "architecture-diagram.css",
            "behavior-diagram.css",
            "interactive-canvas.css",
            "module-map.css",
            "roles-permissions.css",
            "screen-flow.css",
            "sequence-diagram.css",
            "shell-menu-toolbar-base.css",
            "toolbar-contextual.css",
            "uml-class.css",
            "wireframe.css"
    );

    @Test
    void scopedCssShouldUseStraightCornersForDecorativeRules() throws IOException {
        for (String file : SCOPED_CSS) {
            String source = read(CSS.resolve(file));
            for (String line : source.split("\\R")) {
                String trimmed = line.strip();
                if (isRadiusDeclaration(trimmed)) {
                    assertTrue(trimmed.matches("-fx-(border|background)-radius:\\s*0;"),
                            () -> file + " conserva radius ornamental: " + trimmed);
                }
                if (isArcDeclaration(trimmed)) {
                    assertTrue(trimmed.matches("-fx-arc-(width|height):\\s*0;"),
                            () -> file + " conserva arc ornamental: " + trimmed);
                }
            }
        }
    }

    @Test
    void scopedJavaRenderersShouldUseStraightOrnamentalShapes() throws IOException {
        String styleApplier = read(Path.of(
                "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "interactivecanvas", "CanvasStyleApplier.java"));
        assertTrue(styleApplier.contains("CornerRadii.EMPTY"));
        assertTrue(styleApplier.contains("-fx-background-radius: 0"));
        assertTrue(styleApplier.contains("-fx-border-radius: 0"));

        String freeGraph = read(Path.of(
                "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "freegraph", "FreeGraphRenderKit.java"));
        assertFalse(freeGraph.contains("setArcWidth(22.0)"));
        assertFalse(freeGraph.contains("setArcHeight(22.0)"));
        assertTrue(freeGraph.contains("body.setArcWidth(0.0)"));
        assertTrue(freeGraph.contains("body.setArcHeight(0.0)"));

        String adminShapeKit = read(Path.of(
                "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "presentation", "drawing", "admin", "AdminShapeKit.java"));
        assertFalse(adminShapeKit.contains("private static Rectangle rounded"));
        assertTrue(adminShapeKit.contains("private static Rectangle straight"));

        String rolesSvg = read(Path.of(
                "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
                "infrastructure", "svg", "rolespermissions", "RolesPermissionsMatrixSvgExporter.java"));
        assertFalse(rolesSvg.contains("rx=\\\"12\\\""));
        assertFalse(rolesSvg.contains("rx=\\\"7\\\""));
    }

    @Test
    void semanticShapeExceptionsShouldRemainExplicitlyDocumented() throws IOException {
        String doc = read(Path.of("docs", "desarrollo", "TANDA_037_CSS_NO_RADIUS_POR_ALCANCE.md"));
        assertTrue(doc.contains("No se tocan óvalos UML"));
        assertTrue(doc.contains("eventos BPMN"));
        assertTrue(doc.contains("entidades Chen"));
        assertTrue(doc.contains("pantalla de inicio congelada"));
    }

    private static boolean isRadiusDeclaration(String line) {
        return line.startsWith("-fx-border-radius:") || line.startsWith("-fx-background-radius:");
    }

    private static boolean isArcDeclaration(String line) {
        return line.startsWith("-fx-arc-width:") || line.startsWith("-fx-arc-height:");
    }

    private static String read(Path path) throws IOException {
        return java.nio.file.Files.readString(path, StandardCharsets.UTF_8);
    }
}
